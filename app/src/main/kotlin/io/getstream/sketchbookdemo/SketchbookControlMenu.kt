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

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.getstream.sketchbook.SketchbookController

@Composable
fun SketchbookControlItems(
    controller: SketchbookController,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .clickable { controller.undo() }
                .size(46.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_backup_restore),
            tint = if (controller.canUndo.value) {
                MaterialTheme.colors.primary
            } else {
                MaterialTheme.colors.onPrimary
            },
            contentDescription = null
        )

        Icon(
            modifier = Modifier
                .clickable { controller.redo() }
                .size(46.dp)
                .rotate(180f),
            imageVector = ImageVector.vectorResource(R.drawable.ic_backup_restore),
            tint = if (controller.canRedo.value) {
                MaterialTheme.colors.primary
            } else {
                MaterialTheme.colors.onPrimary
            },
            contentDescription = null
        )

        Icon(
            modifier = Modifier
                .clickable {
                    controller.setEraseMode(false)
                    controller.setRainbowShader()
                    Toast
                        .makeText(context, "Rainbow Shader", Toast.LENGTH_SHORT)
                        .show()
                }
                .size(40.dp),
            tint = MaterialTheme.colors.onPrimary,
            imageVector = ImageVector.vectorResource(R.drawable.ic_brush),
            contentDescription = null
        )

        Box {
            val expanded = remember { mutableStateOf(false) }
            val widths = listOf(10f, 20f, 30f, 40f, 50f)
            Icon(
                modifier = Modifier
                    .clickable { expanded.value = true }
                    .size(40.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_line_weight),
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = null
            )
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                widths.forEach { width ->
                    DropdownMenuItem(
                        onClick = {
                            controller.setPaintStrokeWidth(width)
                            expanded.value = false
                        }
                    ) {
                        Text(text = width.toString(), color = MaterialTheme.colors.onPrimary)
                    }
                }
            }
        }

        Box {
            val expanded = remember { mutableStateOf(false) }

            Icon(
                modifier = Modifier
                    .clickable { expanded.value = true }
                    .size(40.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_line_style),
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = null
            )

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {

                DropdownMenuItem(
                    onClick = {
                        controller.setPathEffect(PathEffect.cornerPathEffect(60f))
                        expanded.value = false
                    }
                ) {
                    Text(text = "Normal", color = MaterialTheme.colors.onPrimary)
                }

                DropdownMenuItem(
                    onClick = {
                        controller.setPathEffect(
                            PathEffect.dashPathEffect(
                                floatArrayOf(20f, 40f),
                                40f
                            )
                        )
                        expanded.value = false
                    }
                ) {
                    Text(text = "Dash Effect", color = MaterialTheme.colors.onPrimary)
                }
            }
        }

        Image(
            modifier = Modifier
                .clickable {
                    controller.toggleEraseMode()
                    Toast
                        .makeText(context, "Erase Mode", Toast.LENGTH_SHORT)
                        .show()
                }
                .size(40.dp),
            bitmap = ImageBitmap.imageResource(R.drawable.eraser),
            contentDescription = null
        )

        Icon(
            modifier = Modifier
                .clickable { controller.clear() }
                .size(46.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_clear),
            tint = Color.LightGray,
            contentDescription = null
        )
    }
}
