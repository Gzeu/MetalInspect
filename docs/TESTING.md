# Testing Guide

This module includes:
- Unit tests for DAOs (Room in-memory), UseCases, and ViewModels
- Instrumented test for PDF export size and success

Run locally:
- Unit tests: `./gradlew testDebugUnitTest`
- Android tests: `./gradlew connectedDebugAndroidTest`

Coverage:
- JaCoCo configured with 70% threshold (per build.gradle)

Structure:
```
app/src/test/           # unit tests
app/src/androidTest/    # instrumented tests
```
