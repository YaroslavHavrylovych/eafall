package com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.BuildingId;

/** showPopup unit in description popup */
public class UnitByBuildingDescriptionShowEvent {
    private BuildingId mUnitId;
    private String mTeamName;

    public UnitByBuildingDescriptionShowEvent(BuildingId unitId, String teamName) {
        mUnitId = unitId;
        mTeamName = teamName;
    }

    public BuildingId getBuildingId() {
        return mUnitId;
    }

    public String getTeamName() {
        return mTeamName;
    }
}
