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
    private boolean mWithoutBackground;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * invokes {@link ShowToastEvent#ShowToastEvent(boolean, boolean, int...)} where
     * #withoutBackground argument true
     */
    public ShowToastEvent(boolean longToast, int... text) {
        this(longToast, true, text);
    }

    /**
     * Event to show a toast
     *
     * @param longToast contains true if toast has to be shown for a long period of a time and false
     *                  in other cas
     * @param text      text to display. Can multiple text. The first argument will be the string
     *                  which will contain (String.format) next strings.
     */
    public ShowToastEvent(boolean longToast, boolean withoutBackground, int... text) {
        mTextId = text;
        mIsLongShowedToast = longToast;
        mWithoutBackground = withoutBackground;
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

    public boolean isWithoutBackground() {
        return mWithoutBackground;
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
