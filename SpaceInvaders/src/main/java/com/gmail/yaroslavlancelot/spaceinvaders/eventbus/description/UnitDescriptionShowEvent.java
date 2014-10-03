package com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description;

/** show unit in description popup */
public class UnitDescriptionShowEvent {
    private int mUnitId;
    private String mTeamName;

    public UnitDescriptionShowEvent(int unitId, String teamName) {
        mUnitId = unitId;
        mTeamName = teamName;
    }

    public int getUnitId() {
        return mUnitId;
    }

    public String getTeamName() {
        return mTeamName;
    }
}
