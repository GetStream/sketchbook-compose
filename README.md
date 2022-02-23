<h1 align="center">Sketchbook</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/GetStream/sketchbook-compose/actions/workflows/android.yml"><img alt="Build Status" src="https://github.com/GetStream/sketchbook-compose/actions/workflows/android.yml/badge.svg"/></a>
</p><br>

<p align="center">
🎨 Jetpack Compose canvas library that helps you to draw paths, images on canvas with color pickers and palettes. Sketchbooks also provide useful components and functions that can easily interact with canvas.
</p><br>

## Preview
<p align="center">
<img src="https://user-images.githubusercontent.com/24237865/154213747-438e4e27-57d8-4fe2-98bd-e29d89a6bb8c.gif" width="32.3%"/>
<img src="https://user-images.githubusercontent.com/24237865/154213763-47af36de-61b3-446e-9d6c-563bae87bfda.gif" width="32.3%"/>
<img src="/preview/preview1.gif" width="32.3%"/>
</p>

## Download
[![Maven Central](https://img.shields.io/maven-central/v/io.getstream/sketchbook.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.getstream%22%20AND%20a:%sketchbook%22)

### Gradle
Add the dependency below to your **module**'s `build.gradle` file:
```gradle
dependencies {
    implementation "io.getstream:sketchbook:1.0.0"
}
```

## SNAPSHOT 

<details>
 <summary>See how to import the snapshot</summary>

### Including the SNAPSHOT
Snapshots of the current development version of Sketchbook are available, which track [the latest versions](https://oss.sonatype.org/content/repositories/snapshots/io/getstream/sketchbook/).

To import snapshot versions on your project, add the code snippet below on your gradle file.
```Gradle
repositories {
   maven { url 'https://oss.sonatype.org/content/repositories/snapshots/' }
}
```

Next, add the below dependency to your **module**'s `build.gradle` file.
```gradle
dependencies {
    implementation "io.getstream:sketchbook:1.0.1-SNAPSHOT"
}
```

</details>

## Usage

First, you should initialize `SketchbookController`, which allows you to control the `Sketchbook` and all components.

```kotlin
private val sketchbookController = rememberSketchbookController()
```

Next, you can implement your drawing canvas with the `Sketchbook` composable function:

```kotlin
Sketchbook(
    modifier = Modifier.fillMaxSize(),
    controller = sketchbookController,
    backgroundColor = Color.White
)
```

### Sketchbook

**Sketchbook** implements canvas, which allows you to draw paths with custom properties. It interacts with **SketchbookController** to control other components. 
You can use the `Sketchbook` as the following example:

```kotlin
Sketchbook(
    modifier = Modifier.fillMaxSize(),
    controller = sketchbookController,
    backgroundColor = Color.White,
    onEventListener = { x, y -> .. },
    onRevisedListener = { canUndo, canRedo -> .. }
)
```

### SketchbookController

**SketchbookController** interacts with `Sketchbook` and it allows you to control the canvas and all of the components with it.

#### Undo and Redo

`SketchbookController` supports `undo` and `redo` to cancel or reverse drawn paths and recover the previous canvas state.
You can implement it with the following functions below:

```kotlin
sketchbookController.undo() // undo the drawn path if possible.
sketchbookController.redo() // redo the drawn path if possible.
```

<img src="https://user-images.githubusercontent.com/24237865/154213789-c5f6b885-e890-44d2-91b8-81e09177f678.gif" width="27%" align="right" />

#### Erase Mode

By enabling Erase Mode, you can erase the colored paths following the transparent path. You can set the mode with the function below:

```kotlin
sketchbookController.setEraseMode(true)
```

Also, you can toggle the erase mode with the following function below:

```kotlin
sketchbookController.toggleEraseMode()
```

You can get the `State` of the erase mode with the following function below:

```kotlin
val isEraseMode = sketchbookController.isEraseMode.value
```

> Note: If you use the erase mode, make sure you set the `backgroundColor` on the `Sketchbook` properly.

You can changes the radius size of the erase circle:

```kotlin
sketchbookController.setEraseRadius(50f)
```

<img src="https://user-images.githubusercontent.com/24237865/154213763-47af36de-61b3-446e-9d6c-563bae87bfda.gif" width="27%" align="right" />

#### Customize Paint

You can custom the paint to support various drawing options as the following:

```kotlin
.setPaintColor(color) 
.setPaintAlpha(alpha) 
.setPaintStrokeWidth(stroke)
.setPaintShader(shader)
.setLinearShader(colorList)
.setPaintingStyle(paintStyle)
.setPathEffect(pathEffect)
```

Also, you can set your own paint with the `setPaint` method:

```kotlin
sketchbookController.setPaint(paint)
```

You can set the rainbow shader to the paint with the `setRainbowShader` method:

```kotlin
sketchbookController.setRainbowShader()
```

#### ImageBitmap

<img src="/preview/preview1.gif" width="27%" align="right" />

Sketchbook supports to set an `ImageBitmp` on the canvas and draw paths over the bitmap. You can set an initial `ImageBitmap` on the `Sketchbook` composable as the following:

```kotlin
Sketchbook(
    imageBitmap = ImageBitmap.imageResource(R.drawable.poster),
    ..
)
```

Also, you can set an `ImageBitmap` dynamically with the `SketchbookController` as the following:

```kotlin
sketchbookController.setImageBitmap(imageBitmap)
```

You can clear the image bitmap on canvas with the `clearImageBitmap` method:

```kotlin
sketchbookController.clearImageBitmap()
```

> Note: This demo project demonstrates an image picker with [ModernStorage's Photo Picker](https://google.github.io/modernstorage/photopicker/).

#### Sketchbook Bitmap

You can get the final `ImageBitmap` of the current canvas of the `Sketchbook` with the following:

```kotlin
val imageBitmap = sketchbookController.getSketchbookBitmap()
```

If you'd like to get the Android's bitmap, you can get it as the following: 

```kotlin
val bitmap = sketchbookController.getSketchbookBitmap().asAndroidBitmap()
```

#### Clear

You can clear all the drawn paths and paths and the image bitmap as the following:

```kotlin
sketchbookController.clear()
```

Also, you can clear only the drawn paths and redo paths as the following:

```kotlin
sketchbookController.clearPaths()
```

### PaintColorPalette

<img src="/preview/preview0.png" align="right" width="33%">

**PaintColorPalette** provides a color palette to let users choose desired colors from a provided color list.
It provides default color palettes and shapes, which are fully customizable. You can simply implement this as the following example:

```kotlin
PaintColorPalette(
    controller = sketchbookController,
    borderColor = MaterialTheme.colors.onPrimary
)
```

You can customize UI themes with `PaintColorPaletteTheme` as the following example:

```kotlin
PaintColorPalette(
    theme = PaintColorPaletteTheme(
        shape = CircleShape,
        itemSize = 48.dp,
        selectedItemSize = 58.dp,
        borderColor = Color.White,
        borderWidth = 2.dp,
        borderColor = MaterialTheme.colors.onPrimary
    ),
```

Also, you can set an index for selecting a color initially with the `initialSelectedIndex` parameter:

```kotlin
initialSelectedIndex = 2
```

#### onColorSelected

You can track the selected color's index and color value with the `onColorSelected` listener:

```kotlin
PaintColorPalette(
    onColorSelected = { index, color -> .. },
)
```

#### Header and Footer

You can add your own composable on the very first of the palette or on the end as the following example:

```kotlin
PaintColorPalette(
    header = { Text("Header") },
    footer = { Text("Footer") }
)
```

#### Custom Content

You can customize the entire content with your own composable as the following example:

```kotlin
PaintColorPalette(
    content = { index, color ->
        Box(modifier = Modifier
            .size(60.dp)
            .background(color)
            .clickable { .. }
        )
    }
)
```

> Note: Make sure if you use the custom content, you should implement all interactions and listeners by yourself.


<img src="https://user-images.githubusercontent.com/24237865/154215023-14c50460-f55d-417e-b11a-a6e3961c864c.gif" align="right" width="30%" />

### ColorPickerDialog

Sketchbook supports `ColorPickerDialog`, which lets you choose your desired color from an HSV color palette.
You can implement `ColorPickerDialog` with the following codes: 

```kotlin
val expandColorPickerDialog = remember { mutableStateOf(false) }
Image(
    modifier = 
        Modifier.clickable { expandColorPickerDialog.value = true },
    bitmap = ImageBitmap.imageResource(R.drawable.palette),
    contentDescription = null
)

ColorPickerDialog(
    controller = sketchbookController,
    expanded = expandColorPickerDialog,
    initialColor = controller.currentPaintColor.value,
    onColorSelected = { color ->
        controller.setPaintShader(null)
        controller.setSelectedColorIndex(-1)
    }
)
```

### ColorPickerPaletteIcon

`ColorPickerPaletteIcon` implements the example code above internally, so you can use like an icon, which shows up a color picker dialog as the following example:

```kotlin
ColorPickerPaletteIcon(
     modifier = Modifier
        .size(60.dp)
        .padding(6.dp),
    controller = sketchbookController,
    bitmap = ImageBitmap.imageResource(R.drawable.palette)
)
```

You can use the `ColorPickerPaletteIcon` with `hearder` or `footer` custom composable for the `PaintColorPalette`.

```kotlin
PaintColorPalette(
    header = {
        ColorPickerPaletteIcon(
            ..
        )
    }
)
```

 <a href="https://getstream.io/tutorials/android-chat/"><img src="https://user-images.githubusercontent.com/24237865/146505581-a79e8f7d-6eda-4611-b41a-d60f0189e7d4.jpeg" align="right" /></a>

## Find this library useful? :heart:

Support it by joining __[stargazers](https://github.com/getStream/sketchbook-compose/stargazers)__ for this repository. :star: <br>
Also, follow **[Stream](https://twitter.com/getstream_io)** on Twitter for our next creations!

# License
```xml
Copyright 2022 Stream.IO, Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
