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

    /** return true if buildings creates units which use top path to go and false in other case */
    boolean isTopPath();

    /** set true if this building need to produce units which will go by the top path and false in the other case */
    void setPath(boolean isTop);
}
