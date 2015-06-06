package com.gmail.yaroslavlancelot.eafall.game.eventbus.building;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;

/** holds data need for building creation */
public class CreateBuildingEvent {
    private final BuildingId mBuildingId;
    private final String mPlayerName;

    /**
     * building creation event
     *
     * @param playerName   player name to create a building
     * @param buildingId building buildingId in player
     */
    public CreateBuildingEvent(final String playerName, BuildingId buildingId) {
        mBuildingId = buildingId;
        mPlayerName = playerName;
    }

    /** returns building key in player */
    public BuildingId getBuildingId() {
        return mBuildingId;
    }

    /** player to create building */
    public String getPlayerName() {
        return mPlayerName;
    }
}
