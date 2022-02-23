/*
 * Copyright 2022 Stream.IO, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.getstream.sketchbook

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.IntSize
import io.getstream.sketchbook.internal.SketchPath
import io.getstream.sketchbook.internal.copy
import io.getstream.sketchbook.internal.defaultPaint
import io.getstream.sketchbook.internal.initialSelectedIndex
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Stack

/** Creates and remembers a [SketchbookController] on the current composer. */
@Composable
public fun rememberSketchbookController(): SketchbookController {
    return remember { SketchbookController() }
}

/**
 * SketchbookController interacts with [Sketchbook] and it allows you to control the canvas and
 * all of the components with it.
 */
public class SketchbookController {

    /** A [Paint] to draws paths on canvas. */
    private val drawPaint: MutableState<Paint> = mutableStateOf(defaultPaint())

    /** A radius of the erase circle. */
    internal var eraseRadius: Float = 50f

    /** A [Paint] to erases paths on canvas, which properties are based on the [drawPaint]. */
    private val erasePaint: Paint = Paint().apply {
        shader = null
        color = backgroundColor
        style = PaintingStyle.Fill
        asFrameworkPaint().xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    /** The current [Paint] to draws paths on canvas. */
    public val currentPaint: Paint
        get() = if (isEraseMode.value) {
            erasePaint
        } else {
            drawPaint.value
        }

    private val _currentPaintColor: MutableState<Color> = mutableStateOf(drawPaint.value.color)

    /** [MutableState] of the current color of the [Paint]. */
    public val currentPaintColor: State<Color> = _currentPaintColor

    /** Background color to be used erasing colored paths. */
    private var backgroundColor: Color = Color.Transparent

    /** An [ImageBitmap] to draw paths on the canvas. */
    internal var pathBitmap: ImageBitmap? = null

    /** An [ImageBitmap] to draw a bitmap on the canvas as a background. */
    internal var imageBitmap: ImageBitmap? = null

    private val _selectedColorIndex: MutableState<Int> = mutableStateOf(initialSelectedIndex)

    /** State of the selected color index. */
    internal val selectedColorIndex: State<Int> = _selectedColorIndex

    private val _isEraseMode: MutableState<Boolean> = mutableStateOf(false)

    /** Indicates whether erase mode or not. */
    public val isEraseMode: State<Boolean> = _isEraseMode

    /** Stack of the drawn [Path]s. */
    internal val drawPaths = Stack<SketchPath>()

    /** Stack of the redo [Path]s. */
    private val redoPaths = Stack<SketchPath>()

    private val _canUndo: MutableState<Boolean> = mutableStateOf(false)

    /** Indicates can execute undo or not. */
    public val canUndo: State<Boolean> = _canUndo

    private val _canRedo: MutableState<Boolean> = mutableStateOf(false)

    /** Indicates can execute redo or not. */
    public val canRedo: State<Boolean> = _canRedo

    internal val bitmapSize: MutableState<IntSize> = mutableStateOf(IntSize(0, 0))

    internal val imageBitmapMatrix: MutableState<Matrix> = mutableStateOf(Matrix())

    internal var reviseTick = MutableStateFlow(0)

    /** Sets a background color. */
    public fun setBackgroundColor(color: Color) {
        backgroundColor = color
    }

    /** Sets an [ImageBitmap] to draw on the canvas as a background. */
    public fun setImageBitmap(bitmap: ImageBitmap?) {
        imageBitmap = bitmap
    }

    /** Sets an index of the selected color. */
    public fun setSelectedColorIndex(index: Int) {
        _selectedColorIndex.value = index
    }

    /** Sets a new [Paint]. */
    public fun setPaint(paint: Paint) {
        drawPaint.value = paint
        _currentPaintColor.value = paint.color
    }

    /** Sets an alpha to the [drawPaint]. */
    public fun setPaintAlpha(alpha: Float) {
        drawPaint.value.alpha = alpha
    }

    /** Sets a [Color] to the [drawPaint]. */
    public fun setPaintColor(color: Color) {
        drawPaint.value.color = color
        _currentPaintColor.value = color
    }

    /** Sets a stroke width to the [drawPaint]. */
    public fun setPaintStrokeWidth(strokeWidth: Float) {
        drawPaint.value.strokeWidth = strokeWidth
    }

    /** Sets a [Shader] to the [drawPaint]. */
    public fun setPaintShader(shader: Shader?) {
        drawPaint.value.shader = shader
    }

    /** Sets a color list as a linear shader to the paint. */
    public fun setLinearShader(colors: List<Color>) {
        val size = bitmapSize.value.takeIf { it.width != 0 && it.height != 0 } ?: return
        setPaintShader(
            LinearGradientShader(
                Offset(size.width.toFloat() / 2, 0f),
                Offset(size.width.toFloat() / 2, size.height.toFloat()),
                colors,
            )
        )
    }

    /** Sets a rainbow shader to the paint. */
    public fun setRainbowShader() {
        setLinearShader(defaultColorList())
        setSelectedColorIndex(initialSelectedIndex)
    }

    /** Sets a [PaintingStyle] to the [drawPaint]. */
    public fun setPaintingStyle(paintingStyle: PaintingStyle) {
        drawPaint.value.style = paintingStyle
    }

    /** Sets a [PathEffect] to the [drawPaint]. */
    public fun setPathEffect(pathEffect: PathEffect?) {
        drawPaint.value.pathEffect = pathEffect
    }

    internal fun addDrawPath(path: Path) {
        drawPaths.add(SketchPath(path, Paint().copy(currentPaint)))
    }

    internal fun clearRedoPath() {
        redoPaths.clear()
    }

    internal fun updateRevised() {
        _canUndo.value = drawPaths.isNotEmpty()
        _canRedo.value = redoPaths.isNotEmpty()
    }

    /** Executes undo the drawn path if possible. */
    public fun undo() {
        if (canUndo.value) {
            redoPaths.push(drawPaths.pop())
            reviseTick.value++
            updateRevised()
        }
    }

    /** Executes redo the drawn path if possible. */
    public fun redo() {
        if (canRedo.value) {
            drawPaths.push(redoPaths.pop())
            reviseTick.value++
            updateRevised()
        }
    }

    /**
     * Sets the erase mode or not.
     *
     * @param isEraseMode Flag to set erase mode.
     */
    public fun setEraseMode(isEraseMode: Boolean) {
        _isEraseMode.value = isEraseMode
    }

    /**
     * Sets the radius size of the erase circle.
     *
     * @param eraseRadius Radius of the erase circle.
     */
    public fun setEraseRadius(eraseRadius: Float) {
        this.eraseRadius = eraseRadius
    }

    /** Toggles the erase mode. */
    public fun toggleEraseMode() {
        _isEraseMode.value = !isEraseMode.value
    }

    /** Clear the drawn paths and redo paths on canvas.. */
    public fun clearPaths() {
        drawPaths.clear()
        redoPaths.clear()
        updateRevised()
        reviseTick.value++
    }

    /** Clear the image bitmap. */
    public fun clearImageBitmap() {
        setImageBitmap(null)
        imageBitmapMatrix.value = Matrix()
    }

    /** Clear drawn paths and the bitmap image. */
    public fun clear() {
        clearPaths()
        clearImageBitmap()
    }

    /** Returns the current [Sketchbook]'s bitmap. */
    public fun getSketchbookBitmap(): ImageBitmap {
        val size = bitmapSize.value
        val combinedBitmap = ImageBitmap(size.width, size.height, ImageBitmapConfig.Argb8888)
        val canvas = Canvas(combinedBitmap)
        imageBitmap?.let {
            val immutableBitmap = it.asAndroidBitmap().copy(Bitmap.Config.ARGB_8888, false)
            canvas.nativeCanvas.drawBitmap(immutableBitmap, imageBitmapMatrix.value, null)
            immutableBitmap.recycle()
        }
        pathBitmap?.let { canvas.drawImage(it, Offset.Zero, Paint()) }
        return combinedBitmap
    }

    internal fun releaseBitmap() {
        pathBitmap?.asAndroidBitmap()?.recycle()
        pathBitmap = null
    }
}
