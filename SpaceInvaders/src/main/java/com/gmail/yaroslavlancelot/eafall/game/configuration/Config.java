package com.gmail.yaroslavlancelot.eafall.game.configuration;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.configuration.game.ApplicationConfig;

import org.andengine.util.adt.color.Color;

/** game configuration params */
//TODO rename to ConfigFacade (maybe move to EaFallApplication)
public class Config implements IConfig {
    private static Config sConfig;
    private final ApplicationConfig mApplicationConfig;

    public Config(Context context) {
        mApplicationConfig = new ApplicationConfig(context);
    }

    public static IConfig getConfig() {
        return sConfig;
    }

    public int getMaxZoomFactor() {
        return mApplicationConfig.getMaxZoomFactor();
    }

    public int getMaxSimultaneousSoundStreams() {
        return mApplicationConfig.getMaxSimultaneousSoundStreams();
    }

    public boolean isProfilingEnabled() {
        return mApplicationConfig.isProfilingEnabled();
    }

    public boolean isUnitsHealthBarEnabled() {
        return mApplicationConfig.isUnitsHealthBarEnabled();
    }

    public boolean isSoundsEnabled() {
        return mApplicationConfig.isSoundsEnabled();
    }

    public float getMusicVolumeMax() {
        return mApplicationConfig.getMusicVolumeMax();
    }

    public float getSoundVolumeMax() {
        return mApplicationConfig.getSoundVolumeMax();
    }

    public boolean isMusicEnabled() {
        return mApplicationConfig.isMusicEnabled();
    }

    public int getCreepBuildingsLimit() {
        return 7;
    }

    public int getWealthBuildingsLimit() {
        return 5;
    }

    public int getDisplayWidth() {
        return mApplicationConfig.getDisplayWidth();
    }

    public int getDisplayHeight() {
        return mApplicationConfig.getDisplayHeight();
    }

    public Color getPlayerSwapColor() {
        return mApplicationConfig.getPlayerSwapColor();
    }

    public float getHudAlpha() {
        return mApplicationConfig.getHudAlpha();
    }

    public static void init(Context context) {
        sConfig = new Config(context);
    }
}
