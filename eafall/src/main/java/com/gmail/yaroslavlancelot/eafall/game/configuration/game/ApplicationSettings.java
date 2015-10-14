package com.gmail.yaroslavlancelot.eafall.game.configuration.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.general.SelfCleanable;

import java.util.HashMap;
import java.util.Map;

/**
 * Give access to application settings (stored in shared preferences)
 *
 * @author Yaroslav Havrylovych
 */
public class ApplicationSettings extends SelfCleanable
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    /*
     * Settings constant
     */
    public final String KEY_PREF_SOUNDS;
    public final String KEY_PREF_SOUNDS_VOLUME;
    public final String KEY_PREF_MUSIC;
    public final String KEY_PREF_MUSIC_VOLUME;
    public final String KEY_PREF_UNIT_HEALTH_BAR_BEHAVIOR;
    public final String KEY_PREF_DEVELOPERS_MODE;
    // ===========================================================
    // Constants
    // ===========================================================
    /*
     * Additional
     */
    private boolean mProfilingEnabled = false;
    /*
     * Sound and music
     */
    private volatile boolean mSoundsEnabled = true;
    private volatile boolean mMusicEnabled = true;
    private volatile float mMusicVolumeMax = 0.8f;
    private volatile float mSoundVolumeMax = 0.4f;
    /*
     * Graphic
     */
    private UnitHealthBarBehavior mUnitsHealthBarEnabled = UnitHealthBarBehavior.DEFAULT;

    // ===========================================================
    // Fields
    // ===========================================================
    /** application settings value changed */
    private Map<String, ISettingsChangedListener> mSettingsChangedListeners = new HashMap<>(5);

    // ===========================================================
    // Constructors
    // ===========================================================

    public ApplicationSettings(final Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.registerOnSharedPreferenceChangeListener(this);
        //settings
        KEY_PREF_DEVELOPERS_MODE = context.getString(R.string.pref_dev_mode);
        mProfilingEnabled = preferences.getBoolean(KEY_PREF_DEVELOPERS_MODE, false);
        KEY_PREF_MUSIC = context.getString(R.string.pref_music_enabled);
        mMusicEnabled = preferences.getBoolean(KEY_PREF_MUSIC, true);
        KEY_PREF_MUSIC_VOLUME = context.getString(R.string.pref_music_volume);
        mMusicVolumeMax = preferences.getFloat(KEY_PREF_MUSIC_VOLUME, .9f);
        KEY_PREF_SOUNDS = context.getString(R.string.pref_sounds_enabled);
        mSoundsEnabled = preferences.getBoolean(KEY_PREF_SOUNDS, true);
        KEY_PREF_SOUNDS_VOLUME = context.getString(R.string.pref_sounds_volume);
        mSoundVolumeMax = preferences.getFloat(KEY_PREF_SOUNDS_VOLUME, .5f);
        KEY_PREF_UNIT_HEALTH_BAR_BEHAVIOR = context.getString(R.string.pref_health_bar_behaviour);
        String val = preferences.getString(KEY_PREF_UNIT_HEALTH_BAR_BEHAVIOR,
                UnitHealthBarBehavior.DEFAULT.name());
        mUnitsHealthBarEnabled = UnitHealthBarBehavior.valueOf(val);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public boolean isProfilingEnabled() {
        return mProfilingEnabled;
    }

    public UnitHealthBarBehavior getHealthBarBehavior() {
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

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Object objVal = null;
        if (key.equals(KEY_PREF_DEVELOPERS_MODE)) {
            objVal = mProfilingEnabled = sharedPreferences.getBoolean(key, mProfilingEnabled);
        } else if (key.equals(KEY_PREF_MUSIC)) {
            objVal = mMusicEnabled = sharedPreferences.getBoolean(key, mMusicEnabled);
        } else if (key.equals(KEY_PREF_MUSIC_VOLUME)) {
            objVal = mMusicVolumeMax = sharedPreferences.getFloat(key, mMusicVolumeMax);
        } else if (key.equals(KEY_PREF_SOUNDS)) {
            objVal = mSoundsEnabled = sharedPreferences.getBoolean(key, mSoundsEnabled);
        } else if (key.equals(KEY_PREF_SOUNDS_VOLUME)) {
            objVal = mSoundVolumeMax = sharedPreferences.getFloat(key, mSoundVolumeMax);
        } else if (key.equals(KEY_PREF_UNIT_HEALTH_BAR_BEHAVIOR)) {
            String val = sharedPreferences.getString(KEY_PREF_UNIT_HEALTH_BAR_BEHAVIOR,
                    UnitHealthBarBehavior.DEFAULT.name());
            objVal = val;
            mUnitsHealthBarEnabled = UnitHealthBarBehavior.valueOf(val);
        }
        ISettingsChangedListener listener = mSettingsChangedListeners.get(key);
        if (listener != null && objVal != null) {
            listener.configChanged(objVal);
        }
    }

    @Override
    public void clear() {
        mSettingsChangedListeners.clear();
    }

    public void setOnConfigChangedListener(String key, ISettingsChangedListener listener) {
        if (listener == null) {
            removeOnConfigChangedListener(key);
            return;
        }
        mSettingsChangedListeners.put(key, listener);
    }

    public void removeOnConfigChangedListener(String key) {
        mSettingsChangedListeners.remove(key);
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

    /** used to trigger when application config were changed */
    public interface ISettingsChangedListener {
        void configChanged(Object value);
    }
}
