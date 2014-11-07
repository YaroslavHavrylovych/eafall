package com.gmail.yaroslavlancelot.spaceinvaders.eventbus;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;

/** holds data need for building creation */
public class CreateBuildingEvent {
    private final BuildingId mBuildingId;
    private final String mTeamName;

    /**
     * building creation event
     *
     * @param teamName   team name to create a building
     * @param buildingId building buildingId in team
     */
    public CreateBuildingEvent(final String teamName, BuildingId buildingId) {
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