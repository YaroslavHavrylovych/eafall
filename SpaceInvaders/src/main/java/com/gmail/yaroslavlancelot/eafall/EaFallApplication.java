package com.gmail.yaroslavlancelot.eafall;

import android.app.Application;
import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

public class EaFallApplication extends Application {
    private static volatile Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Config.init(sContext);
        LocaleImpl.init(sContext);
    }
}
