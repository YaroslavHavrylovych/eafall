package com.gmail.yaroslavlancelot.eafall.android;

import android.util.Log;

/**
 * Class creating for facilitate logger operations.
 * Contains only static methods and use default android log.
 */
public final class LoggerHelper {
    public static final String MESSAGE_FROM_SERVER_PREFIX = "Server: ";
    public static final String MESSAGE_FROM_CLIENT_PREFIX = "Client: ";
    public static final String PERFORMANCE_TAG = "Performance";

    /**
     * Print in debug notification about method invocation </br>
     * (i.e. same as you write Log.v(tag, methodName + "(params) has been invoked")
     *
     * @param tag        log TAG
     * @param methodName invoked method name
     */
    public static void methodInvocation(String tag, String methodName) {
        printDebugMessage(tag, methodName + "(params) has been invoked");
    }

    /**
     * Print in debug some message
     *
     * @param tag     log TAG
     * @param message message text
     */
    public static void printDebugMessage(String tag, String message) {
        Log.d(tag, message);
    }

    /**
     * Print in information message
     *
     * @param tag     log TAG
     * @param message message text
     */
    public static void printInformationMessage(String tag, String message) {
        Log.i(tag, message);
    }

    /**
     * Print in information message from server
     *
     * @param tag     log TAG
     * @param message message text
     */
    public static void printInformationMessageInServer(String tag, String message) {
        printInformationMessage(tag, MESSAGE_FROM_SERVER_PREFIX + message);
    }

    /**
     * Print in information message from client
     *
     * @param tag     log TAG
     * @param message message text
     */
    public static void printInformationMessageInClient(String tag, String message) {
        printInformationMessage(tag, MESSAGE_FROM_CLIENT_PREFIX + message);
    }

    /**
     * Print in verbose some message
     *
     * @param tag     log TAG
     * @param message message text
     */
    public static void printVerboseMessage(String tag, String message) {
        Log.v(tag, message);
    }

    /**
     * Print in verbose message from server
     *
     * @param tag     log TAG
     * @param message message text
     */
    public static void printVerbosMessageInServer(String tag, String message) {
        printVerboseMessage(tag, MESSAGE_FROM_SERVER_PREFIX + message);
    }

    /**
     * Print in verbose message from client
     *
     * @param tag     log TAG
     * @param message message text
     */
    public static void printVerboseMessageFromClient(String tag, String message) {
        printVerboseMessage(tag, MESSAGE_FROM_CLIENT_PREFIX + message);
    }

    /**
     * Print in error some message
     *
     * @param tag     log TAG
     * @param message message text
     */
    public static void printErrorMessage(String tag, String message) {
        Log.e(tag, message);
    }
}
