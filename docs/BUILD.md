# Build & Release Guide

## Requirements
- Android Studio Giraffe+ (AGP 8.1.2)
- JDK 17 (recomandat), minimum 11
- Android SDK API 21–33

## Build
```bash
# Debug
./gradlew clean assembleDebug

# Release (minify, shrinkResources, ProGuard)
./gradlew clean assembleRelease
```

## Keystore (Release)
1. Android Studio > Build > Generate Signed Bundle/APK
2. Alegeți APK sau App Bundle
3. Configurați keystore-ul (nu commit-uiți fișierele .jks)

## ProGuard/R8
- Reguli în `app/proguard-rules.pro`
- Evită stripping pentru Room, Hilt, iText, Glide (deja inclus)

## Troubleshooting
- D8/R8 errors → verificați duplicate META-INF (packagingOptions ok)
- OutOfMemory during Dex → măriți Gradle memory în gradle.properties
- Safe Args errors → re-sync Gradle, curățați `.gradle/` și `.idea/`
