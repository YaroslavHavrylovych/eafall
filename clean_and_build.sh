#!/bin/bash
DEMO="Demo";
DEV="Dev";

build_variant=$1

if [ "$build_variant" = "" ]; then
   build_variant=$DEMO 
else 
    if [ "$build_variant" = "1" ]; then
        build_variant=$DEV
    else 
        if [ "$build_variant" = "2" ]; then
            build_variant=$DEMO
        fi
    fi
fi

result=":eafall:assemble${build_variant}Debug"

eval "./gradlew :eafall:clean"
eval "./gradlew ${result} --no-rebuild"
