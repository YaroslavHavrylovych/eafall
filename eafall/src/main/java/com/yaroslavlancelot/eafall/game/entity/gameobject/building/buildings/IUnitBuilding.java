package com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings;

import com.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;

/** general building interface for use by planet */
public interface IUnitBuilding extends IBuilding {
    /** return true if buildings creates units which use top path to go and false in other case */
    boolean isTopPath();

    /** returns true if the building does0'nt produce units at present (building process paused) */
    boolean isPaused();

    int getAvailableUnits();

    int getUnit();

    /** set true if this building need to produce units which will go by the top path and false in the other case */
    void setPath(boolean isTop);

    /** stop units production */
    void pause();

    /** restore units production */
    void unPause();

    void tickUpdate();
}
