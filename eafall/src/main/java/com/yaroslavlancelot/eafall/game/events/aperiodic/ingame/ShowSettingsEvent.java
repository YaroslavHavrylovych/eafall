package com.yaroslavlancelot.eafall.game.events.aperiodic.ingame;

/**
 * Used to trigger settings from the game menu.
 *
 * @author Yaroslav Havrylovych
 */
public class ShowSettingsEvent {
    // ===========================================================
    // Constants
    // ===========================================================
    private static final ShowSettingsEvent SHOW_SETTINGS = new ShowSettingsEvent();

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    private ShowSettingsEvent() {
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public static ShowSettingsEvent getInstance() {
        return SHOW_SETTINGS;
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
