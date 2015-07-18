package com.gmail.yaroslavlancelot.eafall.game.configuration;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import org.andengine.util.adt.color.Color;

/** game configuration params */
public class Config {
    private static Config sConfig;
    /*
     * Display
     */
    private final int mDisplayWidth;
    private final int mDisplayHeight;
    /*
     * Graphic
     */
    private final boolean mUnitsHealthBarEnabled = true;
    private final int mMaxZoomFactor = 6;
    private final float mHudAlpha = 0.85f;
    /*
     * Game
     */
    private final int mMovableUnitsLimit = 200;
    private final int mCreepBuildingsLimit = 7;
    private final int mWealthBuildingsLimit = 5;
    private final int mPlanetHealth = 3000;
    private final int mMaxOxygenAmount = 2000;
    private final Color mPlayerSwapColor = new Color(46.0f / 255.0f, 37.0f / 255.0f, 118.0f / 255.0f);
    /*
     * Sound and music
     */
    private final int mMaxSimultaneousSoundStreams = 4;
    private final boolean mSoundsEnabled = true;
    private final boolean mMusicEnabled = true;
    private final float mMusicVolumeMax = 0.4f;
    private final float mSoundVolumeMax = 0.9f;
    /*
     * Additional
     */
    private final boolean mProfilingEnabled = false;

    public Config(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        mDisplayWidth = Math.max(metrics.widthPixels, metrics.heightPixels);
        mDisplayHeight = Math.min(metrics.widthPixels, metrics.heightPixels);
    }

    public static Config getConfig() {
        return sConfig;
    }

    public int getMaxZoomFactor() {
        return mMaxZoomFactor;
    }

    public int getMaxSimultaneousSoundStreams() {
        return mMaxSimultaneousSoundStreams;
    }

    public boolean isProfilingEnabled() {
        return mProfilingEnabled;
    }

    public int getPlanetHealth() {
        return mPlanetHealth;
    }

    public boolean isUnitsHealthBarEnabled() {
        return mUnitsHealthBarEnabled;
    }

    public boolean isSoundsEnabled() {
        return mSoundsEnabled;
    }

    public float getMusicVolumeMax() {
        return mMusicVolumeMax;
    }

    public float getSoundVolumeMax() {
        return mSoundVolumeMax;
    }

    public boolean isMusicEnabled() {
        return mMusicEnabled;
    }

    public int getMovableUnitsLimit() {
        return mMovableUnitsLimit;
    }

    public int getCreepBuildingsLimit() {
        return mCreepBuildingsLimit;
    }

    public int getWealthBuildingsLimit() {
        return mWealthBuildingsLimit;
    }

    public int getDisplayWidth() {
        return mDisplayWidth;
    }

    public int getDisplayHeight() {
        return mDisplayHeight;
    }

    public Color getPlayerSwapColor() {
        return mPlayerSwapColor;
    }

    public float getHudAlpha() {
        return mHudAlpha;
    }

    public int getMaxOxygenAmount() {
        return mMaxOxygenAmount;
    }

    public static void init(Context context) {
        sConfig = new Config(context);
    }
}
