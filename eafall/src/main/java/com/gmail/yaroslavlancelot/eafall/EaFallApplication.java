package com.gmail.yaroslavlancelot.eafall;

import android.app.Application;
import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.configuration.IConfig;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

public class EaFallApplication extends Application {
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
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sConfig = Config.createConfig(sContext);
        LocaleImpl.init(sContext);
    }
}
