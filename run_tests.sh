#!/bin/bash
result=":eafall:assembleDebugAndroidTest"

toEvalStr="./gradlew :eafall:clean"

echo ${toEvalStr}
eval "${toEvalStr}"

toEvalStr="./gradlew ${result}"
echo ${toEvalStr}
eval "${toEvalStr}"

runTest="adb shell am instrument -w com.yaroslavlancelot.eafall.test/com.yaroslavlancelot.eafall.test.DefaultInstrumentationTestRunner"
eval "${runTest}"
