#!/bin/sh
adb uninstall com.gmail.yaroslavlancelot.eafall
adb install eafall/build/outputs/apk/eafall-debug.apk
adb shell am start -n com.gmail.yaroslavlancelot.eafall/.android.activities.StartupActivity
