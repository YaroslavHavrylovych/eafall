package com.gmail.yaroslavlancelot.eafall.game.events.periodic.time;

import com.gmail.yaroslavlancelot.eafall.game.events.SharedEvents;
import com.gmail.yaroslavlancelot.eafall.game.events.periodic.IPeriodic;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

/**
 * Game time tracker.
 * <br/>
 * Each second sends an event with the key #GAME_TIMER_TICK_KEY
 * through {@link SharedEvents}.
 * <br/>
 * When the timer is over send game over event with the key #GAME_TIME_OVER_KEY
 * through {@link SharedEvents}.
 *
 * @author Yaroslav Havrylovych
 */
public class GameTime implements IPeriodic {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final String GAME_TIMER_TICK_KEY = "game_timer_tick";
    public static final String GAME_TIME_OVER_KEY = "game_time_over";
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
    @Override
    public boolean stoppableWhenGameOver() {
        return true;
    }

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
    public static IPeriodic getPeriodic(int timeInSeconds) {
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
                SharedEvents.valueChanged(GAME_TIME_OVER_KEY, true);
                return;
            }
            SharedEvents.valueChanged(GAME_TIMER_TICK_KEY, --mCurrentTime);
        }
    }
}
