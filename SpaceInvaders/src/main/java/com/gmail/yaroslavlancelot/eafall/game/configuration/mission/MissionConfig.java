package com.gmail.yaroslavlancelot.eafall.game.configuration.mission;

import android.os.Parcel;
import android.os.Parcelable;

import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.mission.DefinitionLoader;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.mission.MissionDataLoader;
import com.gmail.yaroslavlancelot.eafall.general.SelfCleanable;

/**
 * @author Yaroslav Havrylovych
 */
public class MissionConfig extends SelfCleanable implements Parcelable {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final Creator<MissionConfig> CREATOR = new Creator<MissionConfig>() {
        @Override
        public MissionConfig createFromParcel(Parcel in) {
            return new MissionConfig(in);
        }

        @Override
        public MissionConfig[] newArray(int size) {
            return new MissionConfig[size];
        }
    };

    private static final int NO_VALUE = -1;

    // ===========================================================
    // Fields
    // ===========================================================
    private int mMovableUnitsLimit;
    private int mPlanetHealth;
    private int mMaxOxygenAmount;
    private MissionType mMissionType;
    private int mValue;

    // ===========================================================
    // Constructors
    // ===========================================================
    public MissionConfig() {
        resetToDefault();
    }

    public MissionConfig(MissionDataLoader missionData) {
        this();
        init(missionData);
    }

    protected MissionConfig(Parcel in) {
        mMovableUnitsLimit = in.readInt();
        mPlanetHealth = in.readInt();
        mMaxOxygenAmount = in.readInt();
        mValue = in.readInt();
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public int getPlanetHealth() {
        return mPlanetHealth;
    }

    public int getMovableUnitsLimit() {
        return mMovableUnitsLimit;
    }

    public int getMaxOxygenAmount() {
        return mMaxOxygenAmount;
    }

    private int getValue() {
        return mValue;
    }

    private MissionType getMissionType() {
        return mMissionType;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void clear() {
        resetToDefault();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(mMovableUnitsLimit);
        dest.writeInt(mPlanetHealth);
        dest.writeInt(mMaxOxygenAmount);
        dest.writeInt(mValue);
    }

    // ===========================================================
    // Methods
    // ===========================================================
    private void resetToDefault() {
        mMovableUnitsLimit = 200;
        mPlanetHealth = 3000;
        mMaxOxygenAmount = 2000;
        mMissionType = MissionType.WIN;
        mValue = NO_VALUE;
    }

    private void init(MissionDataLoader loadedData) {
        initType(loadedData.definition);
        if (loadedData.max_oxygen != null) mMaxOxygenAmount = loadedData.max_oxygen;
        if (loadedData.planet_health != null) mPlanetHealth = loadedData.planet_health;
            mMovableUnitsLimit = loadedData.movable_units_limit;
    }

    private void initType(DefinitionLoader definition) {
        String type = definition.type;
        if (type != null) {
            mMissionType = MissionType.valueOf(type.toUpperCase());
            mValue = definition.time_limit;
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    public enum MissionType {
        WIN, SURVIVE, COLLECT;
    }
}
