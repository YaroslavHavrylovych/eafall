package com.gmail.yaroslavlancelot.eafall.game.configuration;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.configuration.game.ApplicationConfig;

import org.andengine.util.adt.color.Color;

/**
 * General game configurations. Encapsulates {@link ApplicationConfig}'s
 * and a few other config values
 */
public class Config implements IConfig {
    private final ApplicationConfig mApplicationConfig;

    private Config(Context context) {
        mApplicationConfig = new ApplicationConfig(context);
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

    public ApplicationConfig.UnitHealthBarBehavior getUnitHealthBarBehavior() {
        return mApplicationConfig.getHealthBarBehavior();
    }

    public static IConfig createConfig(Context context) {
        return new Config(context);
    }
}
