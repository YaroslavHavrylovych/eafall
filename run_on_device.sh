#!/bin/sh
adb uninstall com.yaroslavlancelot.eafall
adb install eafall/build/outputs/apk/eafall-debug.apk
adb shell am start -n com.yaroslavlancelot.eafall/.android.activities.StartupActivity
