package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy;

/**
 * @author Yaroslav Havrylovych
 */
public abstract class UnitBuildingDummy extends BuildingDummy {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public UnitBuildingDummy(final int width, final int height, final int upgrades) {
        super(width, height, upgrades);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    public abstract int getUnitCreationTime(int upgrade);

    public abstract int getUnitId(int upgrade);

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
