package com.gmail.yaroslavlancelot.eafall.game.events.periodic.unit;

import com.gmail.yaroslavlancelot.eafall.game.events.periodic.IPeriodic;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

/**
 * Used to trigger units positions update
 *
 * @author Yaroslav Havrylovych
 */
public class UnitPositionUpdater implements IPeriodic {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final float UNIT_UPDATE_TIME = 0.25f;

    // ===========================================================
    // Fields
    // ===========================================================
    private final TimerHandler mTimerHandler;
    private final IPlayer mPlayer;

    // ===========================================================
    // Constructors
    // ===========================================================

    public UnitPositionUpdater(IPlayer player) {
        mPlayer = player;
        mTimerHandler = new TimerHandler(UNIT_UPDATE_TIME, true, new Callback());
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

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    private class Callback implements ITimerCallback {
        @Override
        public void onTimePassed(final TimerHandler pTimerHandler) {
            mPlayer.updateUnitsPositions();
        }
    }
}
