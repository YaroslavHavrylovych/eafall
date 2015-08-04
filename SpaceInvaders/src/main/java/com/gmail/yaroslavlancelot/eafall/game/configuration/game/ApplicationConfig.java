package com.gmail.yaroslavlancelot.eafall.game.configuration.game;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import org.andengine.util.adt.color.Color;

/**
 * General application (e.g. sounds, zoom, display characteristics) configuration
 *
 * @author Yaroslav Havrylovych
 */
public class ApplicationConfig {
    // ===========================================================
    // Constants
    // ===========================================================
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
    private final boolean mProfilingEnabled = true;

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public ApplicationConfig(final Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        mDisplayWidth = Math.max(metrics.widthPixels, metrics.heightPixels);
        mDisplayHeight = Math.min(metrics.widthPixels, metrics.heightPixels);
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    public int getMaxZoomFactor() {
        return mMaxZoomFactor;
    }

    public int getMaxSimultaneousSoundStreams() {
        return mMaxSimultaneousSoundStreams;
    }

    public boolean isProfilingEnabled() {
        return mProfilingEnabled;
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

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
