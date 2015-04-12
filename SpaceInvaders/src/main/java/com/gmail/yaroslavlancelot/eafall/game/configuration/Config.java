package com.gmail.yaroslavlancelot.eafall.game.configuration;

/** game configuration params */
public class Config {
    private static final Config sConfig = new Config();
    private final boolean mUnitsHealthBarEnabled = true;
    private final boolean mSoundsEnabled = true;
    private final boolean mTeamColorAreaEnabled = true;
    private final int mUnitMaximumAmount = 200;
    private final int mPlanetHealth = 300000;

    public static Config getConfig() {
        return sConfig;
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

    public boolean isTeamColorAreaEnabled() {
        return mTeamColorAreaEnabled;
    }

    public int getUnitMaximumAmount() {
        return mUnitMaximumAmount;
    }
}
