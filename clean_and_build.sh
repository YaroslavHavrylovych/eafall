#!/bin/bash

result=":eafall:assembleDebug --stacktrace"

toEvalStr="./gradlew :eafall:clean"

echo ${toEvalStr}
eval "${toEvalStr}"

toEvalStr="./gradlew ${result}"
echo ${toEvalStr}
eval "${toEvalStr}"
