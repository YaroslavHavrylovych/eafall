package com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.CreepBuildingDummy;

public class ShowBuildingDescriptionEvent {
    public CreepBuildingDummy mCreepBuildingDummy;
    public int mAmount = Integer.MIN_VALUE;

    public ShowBuildingDescriptionEvent(CreepBuildingDummy creepBuildingDummy, int amount) {
        mCreepBuildingDummy = creepBuildingDummy;
        mAmount = amount;
    }
}
