package com.yaroslavlancelot.eafall.game.events.aperiodic;

/**
 * Event contains text to display on HUD.
 *
 * @author Yaroslav Havrylovych
 */
public class ShowHudTextEvent {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private int[] mTextId;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * Event to show a on hud text
     *
     * @param text text to display. Can multiple text. The first argument will be the string
     *             which will contain (String.format) next strings.
     */
    public ShowHudTextEvent(int... text) {
        mTextId = text;
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public int[] getTextId() {
        return mTextId;
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
