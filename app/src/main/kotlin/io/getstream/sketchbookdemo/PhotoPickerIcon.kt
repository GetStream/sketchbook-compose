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

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.google.modernstorage.photopicker.PhotoPicker
import io.getstream.sketchbook.SketchbookController

@Composable
@SuppressLint("UnsafeOptInUsageError")
fun ColumnScope.PhotoPickerIcon(
    controller: SketchbookController
) {
    val context = LocalContext.current
    val photoPicker =
        rememberLauncherForActivityResult(PhotoPicker()) { uris ->
            val uri = uris.firstOrNull() ?: return@rememberLauncherForActivityResult

            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }

            controller.setImageBitmap(bitmap.asImageBitmap())
        }

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .align(Alignment.End)
    ) {
        val expanded = remember { mutableStateOf(false) }

        Image(
            modifier = Modifier
                .size(42.dp)
                .clickable { expanded.value = true },
            imageVector = ImageVector.vectorResource(R.drawable.ic_gallery),
            contentDescription = null
        )

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    // Launch the picker with only one image selectable
                    photoPicker.launch(PhotoPicker.Args(PhotoPicker.Type.IMAGES_ONLY, 1))
                    expanded.value = false
                }
            ) {
                Text(text = "Select Photo", color = MaterialTheme.colors.onPrimary)
            }

            DropdownMenuItem(
                onClick = {
                    controller.setImageBitmap(null)
                    expanded.value = false
                }
            ) {
                Text(text = "Clear Photo", color = MaterialTheme.colors.onPrimary)
            }
        }
    }
}
