package com.yaroslavlancelot.eafall.android.logger;

import android.util.Log;

import timber.log.Timber;

/** A tree which logs important information for crash reporting. */
public class CrashReportingTree extends Timber.Tree {
    private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
    private static final String CRASHLYTICS_KEY_TAG = "tag";
    private static final String CRASHLYTICS_KEY_MESSAGE = "message";

    @Override
    protected void log(final int priority, final String tag, final String message, final Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return;
        }
    }
}
