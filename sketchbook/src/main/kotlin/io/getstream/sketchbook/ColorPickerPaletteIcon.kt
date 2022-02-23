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

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap

/**
 * Basic color palette icon to show up color picker dialog and apply selected colors to [SketchbookController].
 * ColorPickerPaletteItem also can be used to header and footer for the [PaintColorPalette].
 *
 * @param modifier [Modifier] to decorate the color palette item.
 * @param controller [SketchbookController] to be applied selected colors/
 * @param bitmap [ImageBitmap] to be used for this icon.
 */
@Composable
public fun ColorPickerPaletteIcon(
    modifier: Modifier,
    controller: SketchbookController,
    bitmap: ImageBitmap
) {
    val expandColorPickerDialog = remember { mutableStateOf(false) }
    Image(
        modifier = modifier.clickable { expandColorPickerDialog.value = true },
        bitmap = bitmap,
        contentDescription = null
    )

    ColorPickerDialog(
        controller = controller,
        expanded = expandColorPickerDialog,
        initialColor = controller.currentPaintColor.value,
        onColorSelected = {
            controller.setPaintShader(null)
            controller.setSelectedColorIndex(-1)
        }
    )
}
