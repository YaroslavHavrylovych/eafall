#!/bin/sh
adb uninstall com.gmail.yaroslavlancelot.spaceinvaders
adb install SpaceInvaders/build/apk/SpaceInvaders-debug-unaligned.apk
adb shell am start -n com.gmail.yaroslavlancelot.spaceinvaders/.activities.StartupActivity
