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

package io.getstream.sketchbookdemo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import io.getstream.sketchbook.ColorPickerPaletteIcon
import io.getstream.sketchbook.PaintColorPalette
import io.getstream.sketchbook.PaintColorPaletteTheme
import io.getstream.sketchbook.rememberSketchbookController
import io.getstream.sketchbookdemo.theme.streamAccent

@Composable
fun MainScreen() {
    val sketchbookController = rememberSketchbookController()

    LaunchedEffect(Unit) {
        sketchbookController.setPaintStrokeWidth(23f)
        sketchbookController.setPaintColor(streamAccent)
    }

    Column {
        PhotoPickerIcon(controller = sketchbookController)

        SketchbookScreen(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f, fill = false),
            controller = sketchbookController
        )

        PaintColorPalette(
            modifier = Modifier.padding(6.dp),
            controller = sketchbookController,
            theme = PaintColorPaletteTheme(borderColor = MaterialTheme.colors.onPrimary),
            initialSelectedIndex = 3,
            onColorSelected = { _, _ -> sketchbookController.setPaintShader(null) },
            header = {
                ColorPickerPaletteIcon(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(6.dp),
                    controller = sketchbookController,
                    bitmap = ImageBitmap.imageResource(R.drawable.palette)
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        SketchbookControlMenu(controller = sketchbookController)

        Spacer(modifier = Modifier.height(20.dp))
    }
}
