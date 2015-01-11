package com.gmail.yaroslavlancelot.spaceinvaders.alliances;

import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dummies.BuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.UnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.dynamic.MovableUnit;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.dynamic.MovableUnitDummy;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.util.color.Color;

import java.util.SortedSet;

/** abstract race interface */
public interface IAlliance {
    String getAllianceName();

    int getBuildingsAmount();

    int getBuildingCost(BuildingId buildingId);

    Unit getUnit(int unitId, Color teamColor);

    void loadResources(TextureManager textureManager);

    UnitDummy getUnitDummy(int unitId);

    BuildingDummy getBuildingDummy(BuildingId buildingId);

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
