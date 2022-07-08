# android_media_utils
Library to work with media files in android


## Installation
Add it in your root build.gradle at the end of repositories:
```
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }
```
Step 2. Add the dependency, latest_version: [![](https://jitpack.io/v/lokile/android_media_utils.svg)](https://jitpack.io/#lokile/android_media_utils)
```
dependencies {
    implementation 'com.github.lokile:android_media_utils:latest_version'
}
```

## Usage:
- To load media files:
```
ImageFile.loadAll(context)
VideoFile.loadAll(context)
```
- To show media files:
```
context.loadMediaItem(...)
```
