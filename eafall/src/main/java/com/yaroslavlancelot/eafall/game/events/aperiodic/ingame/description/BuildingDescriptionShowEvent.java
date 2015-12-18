package com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description;

import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;

public class BuildingDescriptionShowEvent {
    private BuildingId mBuildingId;
    private String mPlayerName;

    public BuildingDescriptionShowEvent(BuildingId buildingId, String playerName) {
        mBuildingId = buildingId;
        mPlayerName = playerName;
    }

    public BuildingId getObjectId() {
        return mBuildingId;
    }

    public String getPlayerName() {
        return mPlayerName;
    }
}
