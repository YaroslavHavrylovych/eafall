package com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description;

public class BuildingDescriptionShowEvent {
    private int mObjectId;
    private String mTeamName;

    public BuildingDescriptionShowEvent(int objectId, String teamName) {
        mObjectId = objectId;
        mTeamName = teamName;
    }

    public int getObjectId() {
        return mObjectId;
    }

    public String getTeamName() {
        return mTeamName;
    }
}
