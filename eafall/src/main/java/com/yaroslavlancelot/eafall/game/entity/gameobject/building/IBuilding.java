package com.yaroslavlancelot.eafall.game.entity.gameobject.building;

import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.setlectable.Selectable;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.StaticObject;

/**
 * General interface for existing buildings. Used by planet to simplify buildings usage.
 * is a selectable element on the game scene
 */
public interface IBuilding extends Selectable {
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

    /** returns current building dummy */
    BuildingDummy getBuildingDummy();

    /** set building {@link org.andengine.entity.Entity} to ignore or receive updates */
    void setIgnoreUpdates(boolean stop);

    /** get building abscissa on the planet (starting from the planet bottom left) */
    float getX();

    /** get building ordinate on the planet (starting from the planet bottom left) */
    float getY();

    /** byu new building */
    boolean buyBuilding();

    /** upgrade all buildings of the current type */
    boolean upgradeBuilding();

    /** destroys building and detaches it */
    void destroy();
}
