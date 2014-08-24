package com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.CreepBuildingDummy;

public class ShowBuildingDescriptionEvent {
    public ShowBuildingDescriptionEvent(CreepBuildingDummy creepBuildingDummy) {
        mCreepBuildingDummy = creepBuildingDummy;
    }

    public CreepBuildingDummy mCreepBuildingDummy;
}
