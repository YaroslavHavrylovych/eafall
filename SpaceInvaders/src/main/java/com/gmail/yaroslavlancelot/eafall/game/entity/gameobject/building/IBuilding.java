package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building;

import org.andengine.entity.IEntity;

/** General interface for existing buildings. Used by planet to simplify buildings usage. */
public interface IBuilding {
    /** return current building type */
    BuildingType getBuildingType();

    /**
     * Return current building income as value or in percent depending on
     * building type (i.e. WEALTH_BUILDING return it's income value in percents)
     */
    int getIncome();

    /** return buildings amount */
    public int getAmount();

    /** return building entity (for drawing) */
    public IEntity getEntity();

    /** return building upgrade */
    int getUpgrade();

    /** byu new building */
    boolean buyBuilding();

    /** upgrade all buildings of the current type */
    boolean upgradeBuilding();

    /** get building abscissa on the planet (starting from the planet bottom left) */
    float getX();

    /** get building ordinate on the planet (starting from the planet bottom left) */
    float getY();
}
