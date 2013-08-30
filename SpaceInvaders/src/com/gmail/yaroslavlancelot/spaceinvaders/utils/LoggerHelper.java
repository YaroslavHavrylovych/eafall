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
        printDebugMessage(tag, methodName + "(params) has been invoked");
    }

    /**
     * Print in verbose some message
     *
     * @param tag log TAG
     * @param message message text
     */
    @SuppressWarnings("unused")
    public static void printVerboseMessage(String tag, String message) {
        Log.v(tag, message);
    }

    /**
     * Print in debug some message
     *
     * @param tag log TAG
     * @param message message text
     */
    public static void printDebugMessage(String tag, String message) {
        Log.d(tag, message);
    }

    /**
     * Print in error some message
     *
     * @param tag log TAG
     * @param message message text
     */
    public static void printErrorMessage(String tag, String message) {
        Log.e(tag, message);
    }
}
