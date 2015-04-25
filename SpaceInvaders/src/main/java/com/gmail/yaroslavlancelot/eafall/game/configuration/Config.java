package com.gmail.yaroslavlancelot.eafall.game.configuration;

/** game configuration params */
public class Config {
    private static final Config sConfig = new Config();
    /*
    * Graphic
    */
    private final boolean mUnitsHealthBarEnabled = true;
    private final boolean mTeamColorAreaEnabled = false;
    /*
     * Game
     */
    private final int mMovableUnitsLimit = 200;
    private final int mCreepBuildingsLimit = 7;
    private final int mWealthBuildingsLimit = 5;
    private final int mMaxZoomFactor = 7;
    private final int mPlanetHealth = 300000;
    /*
     * Sound and music
     */
    private final boolean mSoundsEnabled = true;
    private final boolean mMusicEnabled = true;
    private final float mMusicVolumeMax = 1.0f;
    private final float mSoundVolumeMax = 0.3f;
    /*
     * Additional
     */
    private final boolean mProfilingEnabled = true;

    public static Config getConfig() {
        return sConfig;
    }

    public int getMaxZoomFactor() {
        return mMaxZoomFactor;
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

    public boolean isTeamColorAreaEnabled() {
        return mTeamColorAreaEnabled;
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
}
