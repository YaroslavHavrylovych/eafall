package com.gmail.yaroslavlancelot.spaceinvaders;

import android.app.Application;

import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;

public class SpaceInvadersApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocaleImpl.init(getApplicationContext());
    }
}
