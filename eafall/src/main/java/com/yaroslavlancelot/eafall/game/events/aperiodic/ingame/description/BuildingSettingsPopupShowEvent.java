package com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description;

import com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.IUnitBuilding;

/**
 * Event to open building settings popup
 *
 * @author Yaroslav Havrylovych
 */
public class BuildingSettingsPopupShowEvent {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    private final IUnitBuilding mUnitBuilding;
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public BuildingSettingsPopupShowEvent(IUnitBuilding unitBuilding) {
        mUnitBuilding = unitBuilding;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public IUnitBuilding getUnitBuilding() {
        return mUnitBuilding;
    }


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
