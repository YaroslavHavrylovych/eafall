#!/bin/bash
DEMO="Demo";
DEV="Dev";

build_variant=$1

if [ "$build_variant" = "" ]; then
   build_variant=$DEV
else 
    if [ "$build_variant" = "1" ]; then
        build_variant=$DEV
    else 
        if [ "$build_variant" = "2" ]; then
            build_variant=$DEMO
        fi
    fi
fi

#:eafall:assemble${build_variant}Debug 
result=":eafall:assemble${build_variant}DebugAndroidTest"

toEvalStr="./gradlew :eafall:clean"

echo ${toEvalStr}
eval "${toEvalStr}"

toEvalStr="./gradlew ${result} --no-rebuild"
echo ${toEvalStr}
eval "${toEvalStr}"

runTest="adb shell am instrument -w com.yaroslavlancelot.eafall.test/com.yaroslavlancelot.eafall.test.DefaultInstrumentationTestRunner"
eval "${runTest}"
