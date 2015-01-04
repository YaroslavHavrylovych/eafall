package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings;

/** general building interface for use by planet */
public interface ICreepBuilding extends IBuilding {
    /** return true if buildings creates units which use top path to go and false in other case */
    boolean isTopPath();

    /** set true if this building need to produce units which will go by the top path and false in the other case */
    void setPath(boolean isTop);
}
