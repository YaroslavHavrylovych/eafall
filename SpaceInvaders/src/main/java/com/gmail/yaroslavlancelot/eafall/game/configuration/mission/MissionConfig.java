package com.gmail.yaroslavlancelot.eafall.game.configuration.mission;

import android.os.Parcel;
import android.os.Parcelable;

import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.mission.DefinitionLoader;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.mission.MissionDataLoader;
import com.gmail.yaroslavlancelot.eafall.general.SelfCleanable;

/**
 * Mission config. Used in single game (mission) to evaluate game rules and
 * constraints (i.e. planet health, max units amount etc).
 *
 * @author Yaroslav Havrylovych
 */
public class MissionConfig extends SelfCleanable implements Parcelable {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final int NO_VALUE = -1;

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

    // ===========================================================
    // Fields
    // ===========================================================
    private int mMovableUnitsLimit;
    private int mPlanetHealth;
    private int mMaxOxygenAmount;
    private int mTime;
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
        mTime = in.readInt();
        mPlanetHealth = in.readInt();
        mMaxOxygenAmount = in.readInt();
        mValue = in.readInt();
        mMissionType = (MissionType) in.readSerializable();
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

    public boolean isTimerEnabled() {
        return mTime != NO_VALUE;
    }

    public int getTime() {
        return mTime;
    }

    public int getValue() {
        return mValue;
    }

    public MissionType getMissionType() {
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
        dest.writeInt(mTime);
        dest.writeInt(mPlanetHealth);
        dest.writeInt(mMaxOxygenAmount);
        dest.writeInt(mValue);
        dest.writeSerializable(mMissionType);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /** sets default mission values */
    private void resetToDefault() {
        mMovableUnitsLimit = 200;
        mPlanetHealth = 3000;
        mMaxOxygenAmount = 2000;
        mMissionType = MissionType.WIN;
        mValue = NO_VALUE;
        mTime = NO_VALUE;
    }

    private void init(MissionDataLoader loadedData) {
        initType(loadedData.definition);
        if (loadedData.definition.time_limit != null) mTime = loadedData.definition.time_limit;
        if (loadedData.max_oxygen != null) mMaxOxygenAmount = loadedData.max_oxygen;
        if (loadedData.planet_health != null) mPlanetHealth = loadedData.planet_health;
        if (loadedData.movable_units_limit != null)
            mMovableUnitsLimit = loadedData.movable_units_limit;
    }

    private void initType(DefinitionLoader definition) {
        String type = definition.type;
        if (type != null) {
            mMissionType = MissionType.valueOf(type.toUpperCase());
            if (definition.value != null) mValue = definition.value;
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    public enum MissionType {
        /** destroy you opponent planet (can use timing) */
        WIN,
        /** survive for give period of a time (timing is mandatory) */
        SURVIVE,
        /** collect particular amount of oxygen (timing is mandatory) */
        COLLECT
    }
}
