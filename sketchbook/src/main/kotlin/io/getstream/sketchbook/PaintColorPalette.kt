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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * PaintColorPalette provides a list of color palette.
 * With [SketchbookController] it will take and apply the selected color to the [Sketchbook] automatically.
 *
 * @param modifier [Modifier] to decorate the color palette.
 * @param controller [SketchbookController] to bind [Sketchbook].
 * @param colorList List of color-palette to show them as a row.
 * @param theme A theme that has a collection parameters to customize [PaintColorPalette].
 * @param initialSelectedIndex Sets a selected index initially.
 * @param onColorSelected Listener to track a selected [Color].
 * @param content Custom content to build the palette item.
 */
@Composable
public fun PaintColorPalette(
    modifier: Modifier = Modifier,
    controller: SketchbookController,
    colorList: List<Color> = defaultColorList(),
    theme: PaintColorPaletteTheme = PaintColorPaletteTheme(),
    initialSelectedIndex: Int = -1,
    onColorSelected: ((Int, Color) -> Unit)? = null,
    header: @Composable (() -> Unit)? = null,
    content: @Composable ((Int, Color) -> Unit)? = null,
    footer: @Composable (() -> Unit)? = null,
) {
    val mutateColorList = remember { mutableStateListOf<Color>() }

    LaunchedEffect(key1 = Unit) {
        mutateColorList.addAll(colorList)
        header?.let { mutateColorList.add(0, Color.Transparent) }
        footer?.let { mutateColorList.add(Color.Transparent) }

        val index = initialSelectedIndex + if (header != null) 1 else 0
        controller.setSelectedColorIndex(index)
        if (index in 1..colorList.lastIndex) {
            controller.setPaintColor(mutateColorList[index])
        }
    }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        itemsIndexed(mutateColorList) { index, color ->
            if (header != null && index == 0) {
                header.invoke()
            } else if (footer != null && index == mutateColorList.lastIndex) {
                footer.invoke()
            } else {
                content?.invoke(index, color) ?: ColorPaletteBox(
                    index = index,
                    selectedIndex = controller.selectedColorIndex.value,
                    color = color,
                    theme = theme,
                    onColorSelected = { selectedIndex, selectedColor ->
                        controller.setSelectedColorIndex(selectedIndex)
                        onColorSelected?.invoke(selectedIndex, selectedColor)
                        controller.setPaintColor(color)
                        if (controller.isEraseMode.value) {
                            controller.setEraseMode(false)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ColorPaletteBox(
    index: Int,
    selectedIndex: Int,
    color: Color,
    theme: PaintColorPaletteTheme,
    onColorSelected: ((Int, Color) -> Unit)
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(theme.selectedItemSize)
            .fillMaxHeight()
            .clickable { onColorSelected.invoke(index, color) }
    ) {
        Box(
            modifier = Modifier
                .border(BorderStroke(theme.borderWidth, theme.borderColor), theme.shape)
                .clip(theme.shape)
                .align(Alignment.Center)
                .background(color)
                .size(
                    if (index == selectedIndex) {
                        theme.selectedItemSize
                    } else {
                        theme.itemSize
                    }
                )
        )
    }
}

internal fun defaultColorList(): List<Color> {
    return listOf(
        Color(0xFFf8130d),
        Color(0xFFb8070d),
        Color(0xFF7a000b),
        Color(0xFFff7900),
        Color(0xFFfff8b3),
        Color(0xFFfcf721),
        Color(0xFFf8df09),
        Color(0xFF8a3a00),
        Color(0xFFc0dc18),
        Color(0xFF88dd20),
        Color(0xFF07ddc3),
        Color(0xFF01a0a3),
        Color(0xFF59cbf0),
        Color(0xFF005FFF),
        Color(0xFF022b6d),
        Color(0xFFfa64e1),
        Color(0xFFfc50a6),
        Color(0xFFd7036a),
        Color(0xFFdb94fe),
        Color(0xFFb035f8),
        Color(0xFF7b2bec),
        Color(0xFFb0aaae),
        Color(0xFF768484),
        Color(0xFF333333),
        Color(0xFF0a0c0b),
    )
}

@Preview
@Composable
private fun PalettePreview() {
    PaintColorPalette(controller = SketchbookController())
}
