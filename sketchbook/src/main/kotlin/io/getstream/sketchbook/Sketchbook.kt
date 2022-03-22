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

@file:Suppress("UNUSED_EXPRESSION")

package io.getstream.sketchbook

import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.RectF
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.unit.toSize
import androidx.core.util.Pools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Sketchbook is a canvas implementation to draw paths with custom properties.
 *
 * [SketchbookController] allows you to control the [Sketchbook].
 *
 * @param modifier [Modifier] to decorate the canvas.
 * @param controller [SketchbookController] to control the [Sketchbook].
 * @param backgroundColor A background color to be used erasing colored paths.
 * @param imageBitmap An [ImageBitmap] to draw on the canvas as a background.
 * @param onPathListener An event listener to track drawing paths.
 * @param onEventListener An event listener to track drawing coordinates.
 * @param onRevisedListener A listener to track whether can execute undo or redo.
 */
@Composable
public fun Sketchbook(
    modifier: Modifier,
    controller: SketchbookController,
    backgroundColor: Color = Color.Transparent,
    imageBitmap: ImageBitmap? = null,
    onPathListener: ((path: Path) -> Unit)? = null,
    onEventListener: ((x: Float, y: Float, motionEvent: Int) -> Unit)? = null,
    onRevisedListener: ((canUndo: Boolean, canRedo: Boolean) -> Unit)? = null
) {
    var path = Path()
    var canvas: Canvas? = null
    val currentPoint = PointF(0f, 0f)
    val touchTolerance = LocalViewConfiguration.current.touchSlop
    val invalidatorTick: MutableState<Int> = remember { mutableStateOf(0) }

    DisposableEffect(key1 = controller) {
        controller.setImageBitmap(imageBitmap)
        controller.setBackgroundColor(backgroundColor)

        onDispose {
            controller.releaseBitmap()
            controller.clear()
        }
    }

    val coroutineScope = rememberCoroutineScope()
    SideEffect {
        coroutineScope.launch(Dispatchers.Main) {
            controller.reviseTick.collect {
                canvas?.nativeCanvas?.drawColor(0, PorterDuff.Mode.CLEAR)
                val drawPaths = controller.drawPaths
                if (drawPaths.isNotEmpty()) {
                    drawPaths.forEach { sketchPath ->
                        canvas?.drawPath(sketchPath.path, sketchPath.paint)
                    }
                }
                invalidatorTick.value++
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { newSize ->
                val size =
                    newSize.takeIf { it.width != 0 && it.height != 0 } ?: return@onSizeChanged
                controller.releaseBitmap()
                controller.pathBitmap =
                    ImageBitmap(size.width, size.height, ImageBitmapConfig.Argb8888)
                        .also {
                            canvas = Canvas(it)
                            controller.bitmapSize.value = size
                        }
            }
            .pointerInteropFilter { event ->
                val motionTouchEventX = event.x
                val motionTouchEventY = event.y

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        path.reset()
                        path.moveTo(motionTouchEventX, motionTouchEventY)
                        currentPoint.x = motionTouchEventX
                        currentPoint.y = motionTouchEventY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = abs(motionTouchEventX - currentPoint.x)
                        val dy = abs(motionTouchEventY - currentPoint.y)
                        if (dx >= touchTolerance || dy >= touchTolerance) {
                            // QuadTo() adds a quadratic bezier from the last point,
                            // approaching control point (x1,y1), and ending at (x2,y2).
                            path.quadraticBezierTo(
                                currentPoint.x,
                                currentPoint.y,
                                (motionTouchEventX + currentPoint.x) / 2,
                                (motionTouchEventY + currentPoint.y) / 2
                            )
                            currentPoint.x = motionTouchEventX
                            currentPoint.y = motionTouchEventY

                            // Draw the path in the extra bitmap to save it.
                            if (controller.isEraseMode.value) {
                                canvas?.drawCircle(
                                    center = Offset(motionTouchEventX, motionTouchEventY),
                                    radius = controller.eraseRadius,
                                    paint = controller.currentPaint
                                )
                            } else {
                                canvas?.drawPath(path, controller.currentPaint)
                            }
                        }
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        controller.clearRedoPath()
                        controller.addDrawPath(path)
                        controller.updateRevised()
                        onPathListener?.invoke(path)
                        path = Path()
                    }
                    else -> false
                }
                onEventListener?.invoke(event.x, event.y, event.action)
                invalidatorTick.value++
                true
            }
    ) {
        drawIntoCanvas { canvas ->
            // draw image bitmap on the canvas.
            controller.imageBitmap?.let { imageBitmap ->
                var dx = 0f
                var dy = 0f
                val scale: Float
                val shaderMatrix = Matrix()
                val shader = ImageShader(imageBitmap, TileMode.Clamp)
                val brush = ShaderBrush(shader)
                val paint = paintPool.acquire() ?: Paint()
                paint.asFrameworkPaint().apply {
                    isAntiAlias = true
                    isDither = true
                    isFilterBitmap = true
                }

                // cache the paint in the internal stack.
                canvas.saveLayer(size.toRect(), paint)

                val mDrawableRect = RectF(0f, 0f, size.width, size.height)
                val bitmapWidth: Int = imageBitmap.asAndroidBitmap().width
                val bitmapHeight: Int = imageBitmap.asAndroidBitmap().height

                if (bitmapWidth * mDrawableRect.height() > mDrawableRect.width() * bitmapHeight) {
                    scale = mDrawableRect.height() / bitmapHeight.toFloat()
                    dx = (mDrawableRect.width() - bitmapWidth * scale) * 0.5f
                } else {
                    scale = mDrawableRect.width() / bitmapWidth.toFloat()
                    dy = (mDrawableRect.height() - bitmapHeight * scale) * 0.5f
                }

                // resize the matrix to scale by sx and sy.
                shaderMatrix.setScale(scale, scale)

                // post translate the matrix with the specified translation.
                shaderMatrix.postTranslate(
                    (dx + 0.5f) + mDrawableRect.left,
                    (dy + 0.5f) + mDrawableRect.top
                )
                // apply the scaled matrix to the shader.
                shader.setLocalMatrix(shaderMatrix)
                // Set the shader matrix to the controller.
                controller.imageBitmapMatrix.value = shaderMatrix
                // draw an image bitmap as a rect.
                drawRect(brush = brush, size = controller.bitmapSize.value.toSize())
                // restore canvas
                canvas.restore()
                // resets the paint and release to the pool.
                paint.asFrameworkPaint().reset()
                paintPool.release(paint)
            }

            // draw path bitmap on the canvas.
            controller.pathBitmap?.let { bitmap ->
                canvas.drawImage(bitmap, Offset.Zero, Paint())
            }
        }
        if (invalidatorTick.value != 0) {
            onRevisedListener?.invoke(controller.canUndo.value, controller.canRedo.value)
        }
    }
}

/** paint pool which caching and reusing [Paint] instances. */
private val paintPool = Pools.SimplePool<Paint>(2)
