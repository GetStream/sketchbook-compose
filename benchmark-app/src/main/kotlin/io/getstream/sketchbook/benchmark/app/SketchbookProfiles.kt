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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import io.getstream.sketchbook.Sketchbook
import io.getstream.sketchbook.SketchbookController

@Composable
fun SketchbookProfiles(
    sketchbookController: SketchbookController
) {
    LaunchedEffect(Unit) {
        sketchbookController.setBackgroundColor(Color.White)
        sketchbookController.setImageBitmap(null)
        sketchbookController.setSelectedColorIndex(0)
        sketchbookController.setPaint(Paint())
        sketchbookController.setPaintAlpha(1.0f)
        sketchbookController.setPaintColor(streamAccent)
        sketchbookController.setPaintStrokeWidth(23f)
        sketchbookController.setPaintShader(null)
        sketchbookController.setLinearShader(listOf(Color.White, Color.White))
        sketchbookController.setRainbowShader()
        sketchbookController.setPaintingStyle(PaintingStyle.Fill)
        sketchbookController.setPathEffect(PathEffect.cornerPathEffect(60f))
        sketchbookController.addDrawPath(Path())
        sketchbookController.addDrawPath(Path(), Paint())
        sketchbookController.undo()
        sketchbookController.redo()
        sketchbookController.setEraseMode(false)
        sketchbookController.setEraseRadius(10f)
        sketchbookController.getSketchbookBitmap()
        sketchbookController.toggleEraseMode()
        sketchbookController.clearPaths()
        sketchbookController.clearImageBitmap()
        sketchbookController.clear()
    }

    Sketchbook(
        modifier = Modifier,
        controller = sketchbookController,
        backgroundColor = Color.White,
        imageBitmap = null,
        onPathListener = { },
        onEventListener = { _, _, _ -> },
        onRevisedListener = { _, _ -> }
    )
}
