package com.yaroslavlancelot.eafall;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.yaroslavlancelot.eafall.android.analytics.FullStacktraceExceptionParser;
import com.yaroslavlancelot.eafall.game.configuration.Config;
import com.yaroslavlancelot.eafall.game.configuration.IConfig;
import com.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/** Custom multi-dex application */
public class EaFallApplication extends MultiDexApplication {
    /** application context */
    private static volatile Context sContext;
    /** application config */
    private static volatile IConfig sConfig;
    /** Application tracker */
    private static Tracker mDefaultTracker;

    public static Context getContext() {
        return sContext;
    }

    public static IConfig getConfig() {
        return sConfig;
    }

    //used for testing and probably will be deleted later
    public static void setConfig(IConfig config) {
        sConfig = config;
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return application tracker
     */
    public static Tracker getDefaultTracker() {
        return mDefaultTracker;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sContext = getApplicationContext();
        sConfig = Config.createConfig(this);
        LocaleImpl.init(this);
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        mDefaultTracker = analytics.newTracker(R.xml.analytics);
        ExceptionReporter customReporter = new ExceptionReporter(
                mDefaultTracker,
                Thread.getDefaultUncaughtExceptionHandler(), // Current default uncaught exception handler
                this);
        customReporter.setExceptionParser(new FullStacktraceExceptionParser());
        Thread.setDefaultUncaughtExceptionHandler(customReporter);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
