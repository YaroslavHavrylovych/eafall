package com.gmail.yaroslavlancelot.eafall.game.eventbus.description;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;

public class BuildingDescriptionShowEvent {
    private BuildingId mBuildingId;
    private String mTeamName;

    public BuildingDescriptionShowEvent(BuildingId buildingId, String teamName) {
        mBuildingId = buildingId;
        mTeamName = teamName;
    }

    public BuildingId getObjectId() {
        return mBuildingId;
    }

    public String getTeamName() {
        return mTeamName;
    }
}
