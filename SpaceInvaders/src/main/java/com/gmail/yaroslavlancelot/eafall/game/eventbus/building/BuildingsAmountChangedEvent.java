package com.gmail.yaroslavlancelot.eafall.game.eventbus.building;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;

/** after building was created */
public class BuildingsAmountChangedEvent {
    private final BuildingId mBuildingId;
    private final int mNewBuildingsAmount;
    private final String mPlayerName;

    /**
     * building created event
     *
     * @param playerName           player name to create a building
     * @param buildingId         building buildingId in player
     * @param newBuildingsAmount new buildings amount
     */
    public BuildingsAmountChangedEvent(final String playerName, BuildingId buildingId, int newBuildingsAmount) {
        mBuildingId = buildingId;
        mPlayerName = playerName;
        mNewBuildingsAmount = newBuildingsAmount;
    }

    /** returns building key/id in player */
    public BuildingId getBuildingId() {
        return mBuildingId;
    }

    /** player to create building */
    public String getPlayerName() {
        return mPlayerName;
    }

    public int getNewBuildingsAmount() {
        return mNewBuildingsAmount;
    }
}
