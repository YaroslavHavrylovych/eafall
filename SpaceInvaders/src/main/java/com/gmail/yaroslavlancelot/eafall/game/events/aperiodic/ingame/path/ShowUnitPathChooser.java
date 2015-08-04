package com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.path;

public class ShowUnitPathChooser {
    private String mPlayerName;
    private int mBuildingId;

    public ShowUnitPathChooser(String playerName, int buildingId) {
        mPlayerName = playerName;
        mBuildingId = buildingId;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public int getBuildingId() {
        return mBuildingId;
    }
}
