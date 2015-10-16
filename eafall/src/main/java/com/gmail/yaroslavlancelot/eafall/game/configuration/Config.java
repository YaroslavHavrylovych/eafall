package com.gmail.yaroslavlancelot.eafall.game.configuration;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.gmail.yaroslavlancelot.eafall.game.configuration.game.ApplicationSettings;

import org.andengine.util.adt.color.Color;

/**
 * General game configurations. Encapsulates {@link ApplicationSettings}'s
 * and a few other config values
 */
public class Config implements IConfig {
    public final int mDisplayWidth;
    public final int mDisplayHeight;
    public final int mMaxZoomFactor = 6;
    public final float mHudAlpha = 0.85f;
    public final int mMaxSimultaneousSoundStreams = 4;
    private final Color mPlayerSwapColor = new Color(46.0f / 255.0f, 37.0f / 255.0f, 118.0f / 255.0f);
    /** Access to application settings */
    private final ApplicationSettings mSettings;

    //protected (not private) because of use in testing. Later has to be changed as config has to be in config-files
    protected Config(Context context) {
        mSettings = new ApplicationSettings(context);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        mDisplayWidth = Math.max(metrics.widthPixels, metrics.heightPixels);
        mDisplayHeight = Math.min(metrics.widthPixels, metrics.heightPixels);
    }

    @Override
    public int getMaxZoomFactor() {
        return mMaxZoomFactor;
    }

    @Override
    public int getMaxSimultaneousSoundStreams() {
        return mMaxSimultaneousSoundStreams;
    }

    @Override
    public int getUnitBuildingsLimit() {
        return 7;
    }

    @Override
    public int getWealthBuildingsLimit() {
        return 5;
    }

    @Override
    public int getDisplayWidth() {
        return mDisplayWidth;
    }

    @Override
    public int getDisplayHeight() {
        return mDisplayHeight;
    }

    @Override
    public Color getPlayerSwapColor() {
        return mPlayerSwapColor;
    }

    @Override
    public float getHudAlpha() {
        return mHudAlpha;
    }

    @Override
    public ApplicationSettings getSettings() {
        return mSettings;
    }

    public static IConfig createConfig(Context context) {
        return new Config(context);
    }
}
