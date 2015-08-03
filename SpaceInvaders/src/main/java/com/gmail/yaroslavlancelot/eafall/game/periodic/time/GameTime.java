package com.gmail.yaroslavlancelot.eafall.game.periodic.time;

import com.gmail.yaroslavlancelot.eafall.game.SharedDataCallbacks;
import com.gmail.yaroslavlancelot.eafall.game.periodic.Periodic;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

/**
 * @author Yaroslav Havrylovych
 */
public class GameTime implements Periodic {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final String GAME_TIME_KEY = "game_time";
    private static final int UPDATE_TIME_IN_SECONDS = 1;

    // ===========================================================
    // Fields
    // ===========================================================
    private final int mTime;
    private final TimerHandler mTimerHandler;
    private int mCurrentTime;

    // ===========================================================
    // Constructors
    // ===========================================================

    /** tracks game time */
    private GameTime(int timeInSeconds) {
        mTime = timeInSeconds;
        mCurrentTime = mTime;
        mTimerHandler = new TimerHandler(UPDATE_TIME_IN_SECONDS, true, new Callback());
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public IUpdateHandler getUpdateHandler() {
        return mTimerHandler;
    }

    // ===========================================================
    // Methods
    // ===========================================================
    public static Periodic getPeriodic(int timeInSeconds) {
        return new GameTime(timeInSeconds);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    private class Callback implements ITimerCallback {
        @Override
        public void onTimePassed(final TimerHandler pTimerHandler) {
            if (mCurrentTime <= 0) {
                mTimerHandler.setTimerCallbackTriggered(true);
                mTimerHandler.setAutoReset(false);
                return;
            }
            SharedDataCallbacks.valueChanged(GAME_TIME_KEY, --mCurrentTime);
        }
    }
}
