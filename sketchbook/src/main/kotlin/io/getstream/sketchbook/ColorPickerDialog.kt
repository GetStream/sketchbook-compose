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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.orchestra.colorpicker.AlphaSlideBar
import com.skydoves.orchestra.colorpicker.BrightnessSlideBar
import com.skydoves.orchestra.colorpicker.ColorPicker

/**
 * A color picker dialog to get a desired color from users.
 *
 * @param controller [SketchbookController] to notify color selected.
 * @param backgroundColor A background color of the dialog.
 * @param selectText Text for the select button.
 * @param expanded Indicates expanding dialog or not.
 * @param onColorSelected A color selected listener.
 */
@Composable
public fun ColorPickerDialog(
    controller: SketchbookController? = null,
    expanded: MutableState<Boolean>,
    backgroundColor: Color = Color.White,
    selectText: String = "Select",
    initialColor: Color? = null,
    onColorSelected: ((Color) -> Unit)? = null
) {
    if (expanded.value) {
        Dialog(onDismissRequest = { expanded.value = false }) {
            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(16.dp),
                color = backgroundColor
            ) {
                ColorPickerDialogContent(
                    selectText = selectText,
                    expanded = expanded,
                    initialColor = initialColor,
                    onColorSelected = { selectedColor ->
                        controller?.setPaintColor(selectedColor)
                        onColorSelected?.invoke(selectedColor)
                    }
                )
            }
        }
    }
}

@Composable
private fun ColorPickerDialogContent(
    expanded: MutableState<Boolean>,
    selectText: String,
    initialColor: Color?,
    onColorSelected: (Color) -> Unit
) {
    val (selectedColor, setSelectedColor) = remember { mutableStateOf(ColorEnvelope(0)) }

    ColorPicker(
        modifier = Modifier
            .size(400.dp)
            .padding(horizontal = 12.dp),
        initialColor = initialColor,
        onColorListener = { envelope, _ -> setSelectedColor(envelope) },
        children = {
            Column(
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.padding(vertical = 6.dp)) {
                    AlphaSlideBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(26.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        colorPickerView = it
                    )
                }
                Box(modifier = Modifier.padding(vertical = 6.dp)) {
                    BrightnessSlideBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(26.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        colorPickerView = it
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    onColorSelected.invoke(Color(selectedColor.color))
                    expanded.value = false
                }) {
                    Text(text = selectText)
                }
            }
        }
    )
}
