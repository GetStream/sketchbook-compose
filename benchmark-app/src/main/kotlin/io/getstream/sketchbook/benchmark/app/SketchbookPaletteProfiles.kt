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

package io.getstream.sketchbook.benchmark.app

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import io.getstream.sketchbook.ColorPickerPaletteIcon
import io.getstream.sketchbook.PaintColorPalette
import io.getstream.sketchbook.PaintColorPaletteTheme
import io.getstream.sketchbook.SketchbookController

@Composable
fun SketchbookPaletteIconProfiles(
    sketchbookController: SketchbookController
) {
    ColorPickerPaletteIcon(
        modifier = Modifier
            .size(60.dp)
            .padding(6.dp),
        controller = sketchbookController,
        bitmap = ImageBitmap.imageResource(R.drawable.palette)
    )
}

@Composable
fun PaintColorPaletteProfiles(
    sketchbookController: SketchbookController
) {
    PaintColorPalette(controller = sketchbookController)
}

@Composable
fun PaintColorPaletteThemeProfiles() {
    PaintColorPaletteTheme()
}
