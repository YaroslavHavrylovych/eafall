package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import android.util.Log;

/**
 * Class creating for facilitate logger operations.
 * Contains only static methods and use default android log.
 */
public final class LoggerHelper {
    /**
     * Print in debug notification about method invocation </br>
     * (i.e. same as you write Log.v(tag, methodName + "(params) has been invoked")
     *
     * @param tag log TAG
     * @param methodName invoked method name
     */
    public static void methodInvocation(String tag, String methodName) {
        Log.v(tag, methodName + "(params) has been invoked");
    }
}
