package com.gmail.yaroslavlancelot.eafall.game.alliance;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.TextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import java.util.SortedSet;

/**
 * abstract alliance interface
 *
 * @author Yaroslav Havrylovych
 */
public interface IAlliance {
    String getAllianceName();

    int getBuildingsAmount();

    /** returns buildings texture atlas */
    TextureAtlas getBuildingTextureAtlas();

    /** returns all buildings ids without upgrades */
    SortedSet<Integer> getBuildingsIds();

    /** returns all buildings ids without upgrades */
    SortedSet<Integer> getUnitsIds();

    int getBuildingCost(BuildingId buildingId);

    /** load alliance (player independent) resources */
    void loadAllianceResources(TextureManager textureManager);

    /**
     * Creates texture atlas and loads units (sprites) to it.
     * <br/>
     * Unit texture save in {@link com.gmail.yaroslavlancelot.eafall.game.entity
     * .TextureRegionHolder} with the key assigned by unit dummy (for now it's
     * value which you can get by {@link UnitDummy#getTextureRegionKey(String)})
     *
     * @param playerName       player name to get unit color for create unit texture
     * @param textureManager texture manager to create texture atlas (which returns)
     * @return loaded texture atlas (with units for particular player in it)
     */
    BitmapTextureAtlas loadUnitsToTexture(String playerName, TextureManager textureManager);

    /**
     * get the particular unit dummy
     *
     * @param unitId id to identify the unit dummy
     * @return unit dummy instance
     */
    UnitDummy getUnitDummy(int unitId);

    BuildingDummy getBuildingDummy(BuildingId buildingId);

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
