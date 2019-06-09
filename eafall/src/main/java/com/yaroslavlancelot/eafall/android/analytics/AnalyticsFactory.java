package com.yaroslavlancelot.eafall.android.analytics;

/**
 * Analytics factory.
 * <br/>
 * Initialization has to be done before usage as {@link AnalyticsFactory#init()}
 */
public class AnalyticsFactory {
    private static IAnalytics sAnalytics;

    public static void init() {
        sAnalytics = new LoggerAnalytics();
    }

    public static IAnalytics getInstance() {
        return sAnalytics;
    }
}
