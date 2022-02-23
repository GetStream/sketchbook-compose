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

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A theme that has a collection parameters to customize [PaintColorPalette].
 *
 * @param shape Shape of the palette item.
 * @param itemSize Size of the palette item.
 * @param selectedItemSize Size of the selected palette item.
 * @param borderColor Border color of the palette item.
 * @param borderWidth Border width size of the palette item.
 */
public data class PaintColorPaletteTheme(
    val shape: Shape = CircleShape,
    val itemSize: Dp = 48.dp,
    val selectedItemSize: Dp = 58.dp,
    val borderColor: Color = Color.White,
    val borderWidth: Dp = 2.dp,
)
