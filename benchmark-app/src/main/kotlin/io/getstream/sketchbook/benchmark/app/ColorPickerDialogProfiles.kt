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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.getstream.sketchbook.ColorPickerDialog
import io.getstream.sketchbook.SketchbookController

@Composable
fun ColorPickerDialogProfiles(
    sketchbookController: SketchbookController
) {
    val expanded = remember { mutableStateOf(false) }
    ColorPickerDialog(
        controller = sketchbookController,
        expanded = expanded,
        initialColor = sketchbookController.currentPaintColor.value,
        onColorSelected = {
            sketchbookController.setPaintShader(null)
            sketchbookController.setSelectedColorIndex(-1)
        }
    )
}
