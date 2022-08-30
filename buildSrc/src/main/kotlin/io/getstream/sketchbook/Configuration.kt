package io.getstream.sketchbook

object Configuration {
  const val compileSdk = 33
  const val targetSdk = 33
  const val minSdk = 21
  const val majorVersion = 1
  const val minorVersion = 0
  const val patchVersion = 4
  const val versionName = "$majorVersion.$minorVersion.$patchVersion"
  const val versionCode = 5
  const val snapshotVersionName = "$majorVersion.$minorVersion.${patchVersion + 1}-SNAPSHOT"
  const val artifactGroup = "io.getstream"
}
