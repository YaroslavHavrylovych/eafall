#!/bin/sh
adb uninstall com.gmail.yaroslavlancelot.spaceinvaders
adb install SpaceInvaders/build/outputs/apk/SpaceInvaders-debug.apk
adb shell am start -n com.gmail.yaroslavlancelot.spaceinvaders/.activities.StartupActivity
