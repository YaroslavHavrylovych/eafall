package com.yaroslavlancelot.eafall.game.configuration.mission;

import android.os.Parcel;
import android.os.Parcelable;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.mission.DefinitionLoader;
import com.yaroslavlancelot.eafall.game.mission.MissionDataLoader;
import com.yaroslavlancelot.eafall.general.SelfCleanable;

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
    private int mStarConstellation;
    private int mStarCodeName;
    private int mPlayerPlanet;
    private int mOpponentPlanet;
    private int mStartMoney;

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
        mStarCodeName = in.readInt();
        mStarConstellation = in.readInt();
        mPlayerPlanet = in.readInt();
        mOpponentPlanet = in.readInt();
        mStartMoney = in.readInt();
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

    public int getStarConstellation() {
        return mStarConstellation;
    }

    public int getStarCodeName() {
        return mStarCodeName;
    }

    public int getPlayerPlanet() {
        return mPlayerPlanet;
    }

    public int getOpponentPlanet() {
        return mOpponentPlanet;
    }

    public int getStartMoney() {
        return mStartMoney;
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
        dest.writeInt(mStarCodeName);
        dest.writeInt(mStarConstellation);
        dest.writeInt(mPlayerPlanet);
        dest.writeInt(mOpponentPlanet);
        dest.writeInt(mStartMoney);
        dest.writeSerializable(mMissionType);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /** sets default mission values */
    private void resetToDefault() {
        mMovableUnitsLimit = 200;
        mPlanetHealth = 5000;
        mMaxOxygenAmount = 2000;
        mMissionType = MissionType.WIN;
        mValue = NO_VALUE;
        mTime = NO_VALUE;
        mStarCodeName = R.string.sun;
        mStarConstellation = R.string.no_constellation;
        mPlayerPlanet = R.string.earth;
        mOpponentPlanet = R.string.earth;
        mStartMoney = 1000;
    }

    private void init(MissionDataLoader loadedData) {
        initType(loadedData.definition);
        if (loadedData.definition.time_limit != null) mTime = loadedData.definition.time_limit;
        if (loadedData.max_oxygen != null) mMaxOxygenAmount = loadedData.max_oxygen;
        if (loadedData.max_oxygen != null) mStartMoney = loadedData.start_money;
        if (loadedData.planet_health != null) mPlanetHealth = loadedData.planet_health;
        if (loadedData.player_planet != null)
            mPlayerPlanet = getStringResourceByName(loadedData.player_planet);
        if (loadedData.opponent_planet != null)
            mOpponentPlanet = getStringResourceByName(loadedData.opponent_planet);
        if (loadedData.star_code_name != null)
            mStarCodeName = getStringResourceByName(loadedData.star_code_name);
        if (loadedData.star_constellation != null)
            mStarConstellation = getStringResourceByName(loadedData.star_constellation);
        if (loadedData.offensive_units_limit != null)
            mMovableUnitsLimit = loadedData.offensive_units_limit;
    }

    private int getStringResourceByName(String aString) {
        String packageName = EaFallApplication.getContext().getPackageName();
        return EaFallApplication.getContext().getResources().getIdentifier(aString, "string", packageName);
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
