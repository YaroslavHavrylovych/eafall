package com.gmail.yaroslavlancelot.spaceinvaders.eventbus.unitpath;

public class ShowUnitPathChooser {
    private String mTeamName;
    private int mBuildingId;

    public ShowUnitPathChooser(String teamName, int buildingId) {
        mTeamName = teamName;
        mBuildingId = buildingId;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public int getBuildingId() {
        return mBuildingId;
    }
}
