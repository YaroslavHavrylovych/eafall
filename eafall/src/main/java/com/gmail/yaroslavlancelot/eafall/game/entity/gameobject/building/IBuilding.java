package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.StaticObject;

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
    int getAmount();

    /** return building entity (for drawing) */
    StaticObject getEntity();

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
