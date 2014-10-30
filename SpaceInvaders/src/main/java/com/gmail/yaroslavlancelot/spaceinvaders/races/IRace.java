package com.gmail.yaroslavlancelot.spaceinvaders.races;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dummies.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.UnitDummy;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.util.color.Color;

import java.util.SortedSet;

/** abstract race interface */
public interface IRace {
    String getRaceName();

    int getBuildingsAmount();

    int getBuildingCost(BuildingId buildingId);

    Unit getUnit(int unitId, Color teamColor);

    void loadResources(TextureManager textureManager, Context context);

    UnitDummy getUnitDummy(int unitId);

    CreepBuildingDummy getBuildingDummy(BuildingId buildingId);

    /** returns all buildings ids without upgrades */
    SortedSet<Integer> getBuildingsIds();

    /**
     * calculate the building upgrade cost consuming that only one building needs the upgrade
     *
     * @param buildingId id of the building which we want upgrade
     * @return the upgrade cost value
     */
    int getUpgradeCost(BuildingId buildingId);

    /**
     * check upgrade possibility of the building with given buildingId
     *
     * @param buildingId id of the building the upgrade of the which you want to check
     * @return true if you can upgrade current building and false in other case
     */
    boolean isUpgradeAvailable(BuildingId buildingId);
}
