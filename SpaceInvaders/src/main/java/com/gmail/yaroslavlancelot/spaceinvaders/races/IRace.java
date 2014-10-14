package com.gmail.yaroslavlancelot.spaceinvaders.races;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dummies.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.UnitDummy;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.util.color.Color;

/** abstract race interface */
public interface IRace {
    String getRaceName();

    int getBuildingsAmount();

    int getBuildingCost(BuildingId buildingId);

    Unit getUnit(int unitId, Color teamColor);

    void loadResources(TextureManager textureManager, Context context);

    UnitDummy getUnitDummy(int unitId);

    CreepBuildingDummy getBuildingDummy(BuildingId buildingId);
}
