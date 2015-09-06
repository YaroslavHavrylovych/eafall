package com.gmail.yaroslavlancelot.eafall.game.configuration.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.gmail.yaroslavlancelot.eafall.android.activities.settings.SettingsActivity;

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
    private UnitHealthBarBehavior mUnitsHealthBarEnabled = UnitHealthBarBehavior.DEFAULT;
    private final int mMaxZoomFactor = 6;
    private final float mHudAlpha = 0.85f;
    private final Color mPlayerSwapColor = new Color(46.0f / 255.0f, 37.0f / 255.0f, 118.0f / 255.0f);
    /*
     * Sound and music
     */
    private final int mMaxSimultaneousSoundStreams = 4;
    private final boolean mSoundsEnabled = true;
    private final boolean mMusicEnabled = true;
    private final float mMusicVolumeMax = 0.8f;
    private final float mSoundVolumeMax = 0.4f;
    /*
     * Additional
     */
    private final boolean mProfilingEnabled = true;

    private final SharedPreferences preferences;

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
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
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
        return preferences.getBoolean(SettingsActivity.KEY_PREF_DEVELOPERS_MODE, mProfilingEnabled);
    }

    public UnitHealthBarBehavior getHealthBarBehavior() {
        String unitHealthBarBehaviour = preferences.getString(SettingsActivity.KEY_PREF_UNIT_HEALTH_BAR_BEHAVIOR, "");
        if (unitHealthBarBehaviour.isEmpty()) return mUnitsHealthBarEnabled;
        return UnitHealthBarBehavior.valueOf(unitHealthBarBehaviour);
    }

    public boolean isSoundsEnabled() {
        return preferences.getBoolean(SettingsActivity.KEY_PREF_SOUNDS, mSoundsEnabled);
    }

    public float getMusicVolumeMax() {
        float volumeInPercents = preferences.getFloat(SettingsActivity.KEY_PREF_MUSIC_VOLUME, mMusicVolumeMax * 100);
        return volumeInPercents / 100f;
    }

    public float getSoundVolumeMax() {
        float volumeInPercents = preferences.getFloat(SettingsActivity.KEY_PREF_SOUNDS_VOLUME, mSoundVolumeMax * 100);
        return volumeInPercents / 100f;
    }

    public boolean isMusicEnabled() {
        return preferences.getBoolean(SettingsActivity.KEY_PREF_MUSIC, mMusicEnabled);
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
    public enum UnitHealthBarBehavior {
        /**
         * default behavior means that health bar is invisible until unit was hit
         */
        DEFAULT,
        ALWAYS_HIDDEN,
        ALWAYS_VISIBLE
    }
}
