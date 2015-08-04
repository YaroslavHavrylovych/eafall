package com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.endgame;

/**
 * Event which signalize that the current game is over.
 * <br/>
 * contains result data
 *
 * @author Yaroslav Havrylovych
 */
public class GameEndedEvent {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    /** if true does means that current game/mission is over and rules are succeed (i.e. you win) */
    private boolean mSuccess;

    // ===========================================================
    // Constructors
    // ===========================================================
    public GameEndedEvent(boolean success) {
        mSuccess = success;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    /**
     * if true than user does win this game and false in other case
     */
    public boolean isSuccess() {
        return mSuccess;
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
