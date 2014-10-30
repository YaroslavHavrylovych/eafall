package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects;

import org.andengine.entity.IEntity;

/** general building interface for use by planet */
public interface Building {
    /** return building upgrade */
    int getUpgrade();

    /** return building income */
    int getIncome();

    /** byu new building */
    boolean buyBuilding();

    /** upgrade all buildings of the current type */
    boolean upgradeBuilding();

    /** */
    int getAmount();

    /** return building entity */
    IEntity getEntity();
}
