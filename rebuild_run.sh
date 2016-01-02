#!/bin/bash
val=$1

eval "./clean_and_build.sh $val"
eval "./run_on_device.sh"
