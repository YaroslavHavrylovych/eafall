package com.yaroslavlancelot.eafall.android.analytics;

import com.google.android.gms.analytics.ExceptionParser;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Performs the whole stacktrace without changes.
 *
 * @author Yaroslav Havrylovych
 */
public class FullStacktraceExceptionParser implements ExceptionParser {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    /**
     * (non-Javadoc)
     *
     * @see ExceptionParser#getDescription(java.lang.String, java.lang.Throwable)
     */
    @Override
    public String getDescription(final String s, final Throwable throwable) {
        return "Thread: " + throwable + ", Exception: " + ExceptionUtils.getStackTrace(throwable);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
