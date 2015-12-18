package com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;

/** showPopup unit in description popup */
public class UnitByBuildingDescriptionShowEvent {
    private BuildingId mUnitId;
    private String mPlayerName;

    public UnitByBuildingDescriptionShowEvent(BuildingId unitId, String playerName) {
        mUnitId = unitId;
        mPlayerName = playerName;
    }

    public BuildingId getBuildingId() {
        return mUnitId;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public void setId(BuildingId id) {
        mUnitId = id;
    }
}
