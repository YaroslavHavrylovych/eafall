package com.yaroslavlancelot.eafall.game.configuration.mission;

import android.os.Parcel;
import android.os.Parcelable;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.ai.VeryFirstBot;
import com.yaroslavlancelot.eafall.game.client.thick.single.SinglePlayerGameActivity;
import com.yaroslavlancelot.eafall.game.mission.DefinitionLoader;
import com.yaroslavlancelot.eafall.game.mission.MissionDetailsLoader;

/**
 * Mission config. Used in single game (mission) to evaluate game rules and
 * constraints (i.e. planet health, max units amount etc).
 *
 * @author Yaroslav Havrylovych
 */
public class MissionConfig implements Parcelable {
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
    private int mPlayerStartMoney;
    private int mOpponentStartMoney;
    private boolean mSingleWay;
    private boolean mSuppressor;
    private String mBotLogic;
    private String mGameHandler;
    private int mPlayerBuildingsLimit;
    private int mOpponentBuildingsLimit;

    // ===========================================================
    // Constructors
    // ===========================================================
    public MissionConfig() {
        resetToDefault();
    }

    public MissionConfig(MissionDetailsLoader missionData) {
        this();
        init(missionData);
    }

    protected MissionConfig(Parcel in) {
        mBotLogic = in.readString();
        mGameHandler = in.readString();
        mMovableUnitsLimit = in.readInt();
        mTime = in.readInt();
        mPlanetHealth = in.readInt();
        mMaxOxygenAmount = in.readInt();
        mValue = in.readInt();
        mStarCodeName = in.readInt();
        mStarConstellation = in.readInt();
        mPlayerPlanet = in.readInt();
        mPlayerStartMoney = in.readInt();
        mPlayerBuildingsLimit = in.readInt();
        mOpponentPlanet = in.readInt();
        mOpponentStartMoney = in.readInt();
        mOpponentBuildingsLimit = in.readInt();
        mSingleWay = in.readInt() == 1;
        mSuppressor = in.readInt() == 1;
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

    public int getPlayerStartMoney() {
        return mPlayerStartMoney;
    }

    public int getOpponentStartMoney() {
        return mOpponentStartMoney;
    }

    public boolean isSunPresent() {
        return mStarCodeName != NO_VALUE;
    }

    public boolean isSingleWay() {
        return mSingleWay;
    }

    public boolean isSuppressorEnabled() {
        return mSuppressor;
    }

    public String getBotLogic() {
        return mBotLogic;
    }

    public int getPlayerBuildingsLimit() {
        return mPlayerBuildingsLimit;
    }

    public int getOpponentBuildingsLimit() {
        return mOpponentBuildingsLimit;
    }

    public String getGameHandler() {
        return mGameHandler;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mBotLogic);
        dest.writeString(mGameHandler);
        dest.writeInt(mMovableUnitsLimit);
        dest.writeInt(mTime);
        dest.writeInt(mPlanetHealth);
        dest.writeInt(mMaxOxygenAmount);
        dest.writeInt(mValue);
        dest.writeInt(mStarCodeName);
        dest.writeInt(mStarConstellation);
        dest.writeInt(mPlayerPlanet);
        dest.writeInt(mPlayerStartMoney);
        dest.writeInt(mPlayerBuildingsLimit);
        dest.writeInt(mOpponentPlanet);
        dest.writeInt(mOpponentStartMoney);
        dest.writeInt(mOpponentBuildingsLimit);
        dest.writeInt(mSingleWay ? 1 : 0);
        dest.writeInt(mSuppressor ? 1 : 0);
        dest.writeSerializable(mMissionType);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * sets default mission values
     */
    private void resetToDefault() {
        mMovableUnitsLimit = 200;
        mPlanetHealth = 5000;
        mMaxOxygenAmount = 2000;
        mMissionType = MissionType.WIN;
        mValue = NO_VALUE;
        mTime = NO_VALUE;
        mStarCodeName = NO_VALUE;
        mStarConstellation = R.string.no_constellation;
        mPlayerPlanet = R.string.earth;
        mPlayerStartMoney = 1000;
        mPlayerBuildingsLimit = NO_VALUE;
        mOpponentPlanet = R.string.earth;
        mOpponentStartMoney = 1000;
        mOpponentBuildingsLimit = NO_VALUE;
        mSingleWay = false;
        mSuppressor = true;
        mBotLogic = VeryFirstBot.class.getName();
        mGameHandler = SinglePlayerGameActivity.class.getName();
    }

    private void init(MissionDetailsLoader loadedData) {
        initType(loadedData.definition);
        if (loadedData.definition.time_limit != null) mTime = loadedData.definition.time_limit;
        if (loadedData.single_way != null) mSingleWay = loadedData.single_way;
        if (loadedData.suppressor != null) mSuppressor = loadedData.suppressor;
        if (loadedData.max_oxygen != null) mMaxOxygenAmount = loadedData.max_oxygen;
        if (loadedData.player_start_money != null)
            mPlayerStartMoney = loadedData.player_start_money;
        if (loadedData.player_available_buildings != null) {
            mPlayerBuildingsLimit = loadedData.player_available_buildings;
        } else {
            mPlayerBuildingsLimit = NO_VALUE;
        }
        if (loadedData.opponent_start_money != null)
            mOpponentStartMoney = loadedData.opponent_start_money;
        if (loadedData.opponent_available_buildings != null) {
            mOpponentBuildingsLimit = loadedData.opponent_available_buildings;
        } else {
            mOpponentBuildingsLimit = NO_VALUE;
        }
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
        mBotLogic = loadedData.enemy_logic_handler == null ? VeryFirstBot.class.getName() : loadedData.enemy_logic_handler;
        mGameHandler = loadedData.game_handler == null ? SinglePlayerGameActivity.class.getName() : loadedData.game_handler;
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
        /**
         * destroy you opponent planet (can use timing)
         */
        WIN,
        /**
         * survive for give period of a time (timing is mandatory)
         */
        SURVIVE,
        /**
         * collect particular amount of oxygen (timing is mandatory)
         */
        COLLECT
    }
}
