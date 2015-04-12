package com.gmail.yaroslavlancelot.eafall.game.eventbus.building;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;

/** holds data needed to upgrade the building */
public class UpgradeBuildingEvent {
    private final BuildingId mBuildingId;
    private final String mTeamName;

    /**
     * building upgrade event
     *
     * @param teamName   team name to create a building
     * @param buildingId building buildingId in team
     */
    public UpgradeBuildingEvent(final String teamName, BuildingId buildingId) {
        mBuildingId = buildingId;
        mTeamName = teamName;
    }

    /** returns building key in team */
    public BuildingId getBuildingId() {
        return mBuildingId;
    }

    /** team to create building */
    public String getTeamName() {
        return mTeamName;
    }
}