#!/bin/bash

result=":eafall:assembleDebug"

toEvalStr="./gradlew :eafall:clean"

echo ${toEvalStr}
eval "${toEvalStr}"

toEvalStr="./gradlew ${result}"
echo ${toEvalStr}
eval "${toEvalStr}"
