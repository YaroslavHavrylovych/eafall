package com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;

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
