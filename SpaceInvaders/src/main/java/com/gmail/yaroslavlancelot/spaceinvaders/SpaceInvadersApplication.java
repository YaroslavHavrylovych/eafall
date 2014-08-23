package com.gmail.yaroslavlancelot.spaceinvaders;

import android.app.Application;
import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;

public class SpaceInvadersApplication extends Application {
    private static volatile Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        LocaleImpl.init(sContext);
    }
}
