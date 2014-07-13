package com.gmail.yaroslavlancelot.spaceinvaders.gameloop;

import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.CreateUnitEvent;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import de.greenrobot.event.EventBus;

/**
 * Handles how much unit to create and make it.
 */
public class UnitCreatorCycle implements ITimerCallback {
    private final int mUnitKey;
    private final String mTeamName;
    private volatile int mUnitAmount;

    public UnitCreatorCycle(String teamName, int unitKey) {
        mTeamName = teamName;
        mUnitKey = unitKey;
    }

    public void increaseUnitAmount() {
        mUnitAmount++;
    }

    @SuppressWarnings("unused")
    public int getUnitAmount() {
        return mUnitAmount;
    }

    @Override
    public void onTimePassed(final TimerHandler pTimerHandler) {
        for (int i = 0; i < mUnitAmount; i++) {
            EventBus.getDefault().post(new CreateUnitEvent(mUnitKey, mTeamName));
        }
    }
}
