package com.gmail.yaroslavlancelot.eafall.game;

/**
 * Contains methods to determine current game state.
 * <br/>
 * Only classes from the same package can change this state (i.e. {@link EaFallActivity}).
 *
 * @author Yaroslav Havrylovych
 */
public class GameState {
    private static final GameState GAME_STATE = new GameState();
    protected State mState;

    private GameState() {
        reset();
    }

    public static synchronized boolean isPaused() {
        return GAME_STATE.mState == State.PAUSED;
    }

    public static synchronized boolean isResumed() {
        return GAME_STATE.mState == State.RESUMED;
    }

    /** @return true if game state passed INITIALIZATION and RESOURCE_LOADING states */
    public static synchronized boolean isResourcesLoaded() {
        return GAME_STATE.mState.ordinal() > State.RESOURCE_LOADING.ordinal();
    }

    public static synchronized boolean isGameEnded() {
        return GAME_STATE.mState == State.ENDED;
    }

    /** @return current game state */
    public static synchronized State getState() {
        return GAME_STATE.mState;
    }

    /**
     * Change the game state.
     * <p/>
     * Throws IllegalArgumentException if state parameter equal
     * to RESOURCE_LOADING or INITIALIZATION
     * <p/>
     * {@throws IllegalArgumentException}
     */
    static synchronized void setState(State state) {
        if (state == State.RESOURCE_LOADING || state == State.INITIALIZATION) {
            throw new IllegalArgumentException("Prohibited state for manual state change");
        }
        GAME_STATE.mState = state;
    }

    private void reset() {
        mState = State.INITIALIZATION;
    }

    static synchronized void resourceLoading() {
        GAME_STATE.mState = State.RESOURCE_LOADING;
    }

    static synchronized void resetState() {
        GAME_STATE.reset();
    }

    /**
     * Contains available game states. States positioned in their natural order (i.e.
     * INITIALIZATION then goes RESOURCE_LOADING then game become RESUMED and etc.)
     */
    public enum State {
        INITIALIZATION, RESOURCE_LOADING, RESUMED, PAUSED, ENDED
    }
}
