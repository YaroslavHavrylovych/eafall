package com.gmail.yaroslavlancelot.eafall.game.eventbus.building;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;

/** after building was created */
public class BuildingsAmountChangedEvent {
    private final BuildingId mBuildingId;
    private final int mNewBuildingsAmount;
    private final String mTeamName;

    /**
     * building created event
     *
     * @param teamName           team name to create a building
     * @param buildingId         building buildingId in team
     * @param newBuildingsAmount new buildings amount
     */
    public BuildingsAmountChangedEvent(final String teamName, BuildingId buildingId, int newBuildingsAmount) {
        mBuildingId = buildingId;
        mTeamName = teamName;
        mNewBuildingsAmount = newBuildingsAmount;
    }

    /** returns building key/id in team */
    public BuildingId getBuildingId() {
        return mBuildingId;
    }

    /** team to create building */
    public String getTeamName() {
        return mTeamName;
    }

    public int getNewBuildingsAmount() {
        return mNewBuildingsAmount;
    }
}
