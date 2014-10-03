package com.gmail.yaroslavlancelot.spaceinvaders.races;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.UnitDummy;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.util.color.Color;

/** abstract race interface */
public interface IRace {
    String getRaceName();

    int getBuildingsAmount();

    String[] getBuildingsNames();

    StaticObject getBuildingById(int buildingId, Color teamColor);

    int getBuildingCostById(int buildingId);

    Unit getUnitForBuilding(int buildingId, Color teamColor);

    void loadResources(TextureManager textureManager, Context context);

    UnitDummy getUnitDummy(int unitId);

    CreepBuildingDummy getBuildingDummy(int buildingId);
}
