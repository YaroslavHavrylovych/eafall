package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import android.util.Log;

public final class LoggerHelper {
    public static void methodInvocation(String tag, String methodName) {
        Log.v(tag, methodName + "() has been invoked");
    }
}
