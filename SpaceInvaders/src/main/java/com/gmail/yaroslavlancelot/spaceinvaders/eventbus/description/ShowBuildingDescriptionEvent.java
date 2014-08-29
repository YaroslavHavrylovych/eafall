package com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description;

public class ShowBuildingDescriptionEvent {
    public int mBuildingId;
    public int mAmount = Integer.MIN_VALUE;

    public ShowBuildingDescriptionEvent(int buildingId, int amount) {
        mBuildingId = buildingId;
        mAmount = amount;
    }
}
