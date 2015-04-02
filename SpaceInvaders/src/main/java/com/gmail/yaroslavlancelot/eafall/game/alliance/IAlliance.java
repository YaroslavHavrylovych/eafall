package com.gmail.yaroslavlancelot.eafall.game.alliance;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.TextureAtlas;

import java.util.SortedSet;

/** abstract race interface */
public interface IAlliance {
    String getAllianceName();

    int getBuildingsAmount();

    int getBuildingCost(BuildingId buildingId);

    /** returns units texture atlas */
    TextureAtlas getUnitTextureAtlas();

    /** returns buildings texture atlas */
    TextureAtlas getBuildingTextureAtlas();

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
