package com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;

/** holds data needed to upgrade the building */
public class UpgradeBuildingEvent {
    private final BuildingId mBuildingId;
    private final String mPlayerName;

    /**
     * building upgrade event
     *
     * @param playerName   player name to create a building
     * @param buildingId building buildingId in player
     */
    public UpgradeBuildingEvent(final String playerName, BuildingId buildingId) {
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
