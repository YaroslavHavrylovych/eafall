package com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description;

import android.content.DialogInterface;

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
    private final DialogInterface.OnDismissListener mDismissListener;

    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public BuildingSettingsPopupShowEvent(IUnitBuilding unitBuilding) {
        this(unitBuilding, null);
    }

    public BuildingSettingsPopupShowEvent(IUnitBuilding unitBuilding,
                                          DialogInterface.OnDismissListener listener) {
        mUnitBuilding = unitBuilding;
        mDismissListener = listener;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public IUnitBuilding getUnitBuilding() {
        return mUnitBuilding;
    }

    public DialogInterface.OnDismissListener getDismissListener() {
        return mDismissListener;
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
