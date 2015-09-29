package com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame;

/**
 * @author Yaroslav Havrylovych
 */
public class PauseGameEvent {
    // ===========================================================
    // Constants
    // ===========================================================
    private static final PauseGameEvent PAUSE_GAME_EVENT = new PauseGameEvent();

    // ===========================================================
    // Fields
    // ===========================================================
    private boolean mPause = false;

    // ===========================================================
    // Constructors
    // ===========================================================
    private PauseGameEvent() {
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public boolean isPause() {
        return mPause;
    }

    public void setPause(final boolean pause) {
        mPause = pause;
    }


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    public static PauseGameEvent getInstance() {
        return PAUSE_GAME_EVENT;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
