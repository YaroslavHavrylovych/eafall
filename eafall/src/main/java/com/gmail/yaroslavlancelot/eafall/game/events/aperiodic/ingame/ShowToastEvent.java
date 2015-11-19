package com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame;

/**
 * Event contains text and time (period) of a toast to show.
 *
 * @author Yaroslav Havrylovych
 */
public class ShowToastEvent {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private int[] mTextId;
    private boolean mIsLongShowedToast;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * Event to show a toast
     *
     * @param longToast contains true if toast has to be shown for a long period of a time and false
     *                  in other cas
     * @param text      text to display. Can multiple text. The first argument will be the string
     *                  which will contain (String.format) next strings.
     */
    public ShowToastEvent(boolean longToast, int... text) {
        mTextId = text;
        mIsLongShowedToast = longToast;
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public int[] getTextId() {
        return mTextId;
    }

    public boolean isLongShowedToast() {
        return mIsLongShowedToast;
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
