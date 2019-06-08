package com.yaroslavlancelot.eafall;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.yaroslavlancelot.eafall.android.logger.CrashReportingTree;
import com.yaroslavlancelot.eafall.android.utils.music.MusicFactory;
import com.yaroslavlancelot.eafall.game.configuration.Config;
import com.yaroslavlancelot.eafall.game.configuration.IConfig;
import com.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import timber.log.Timber;

/** Custom multi-dex application */
public class EaFallApplication extends MultiDexApplication {
    /** application context */
    private static volatile Context sContext;
    /** application config */
    private static volatile IConfig sConfig;

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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sConfig = Config.createConfig(this);
        LocaleImpl.init(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
        MusicFactory.init(this, sConfig.getSettings());
    }
}
