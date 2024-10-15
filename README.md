# emulator.wtf test runtime

Runtime helpers and utilities for running Android tests on emulator.wtf.

This library adds the necessary [JUnit `RunListener`](test-runtime-android/src/main/java/wtf/emulator/EWRunListener.java)
to enable per-test video captures.

## Installation

> [!NOTE]  
> The dependency needs to be added to _all_ modules that have Android tests.

### With Gradle Script Kotlin (`build.gradle.kts`)

Add the following to your `build.gradle.kts` file(s):

```kotlin
androidTestImplementation("wtf.emulator:test-runtime-android:0.2.1")
```

### With Gradle/Groovy (`build.gradle`)

Add the following to your `build.gradle` file(s):

```groovy
androidTestImplementation 'wtf.emulator:test-runtime-android:0.2.1'
```
