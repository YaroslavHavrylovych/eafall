#!/bin/sh
adb uninstall com.gmail.yaroslavlancelot.eafall
adb install SpaceInvaders/build/outputs/apk/SpaceInvaders-debug.apk
adb shell am start -n com.gmail.yaroslavlancelot.eafall/.android.activities.StartupActivity
