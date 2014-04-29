package com.gmail.yaroslavlancelot.spaceinvaders.gameloop;

import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

/**
 * Handles how much unit to create and make it.
 */
public class UnitCreatorCycle implements ITimerCallback {
    private final int mUnitKey;
    private final EntityOperations mEntityOperations;
    private final ITeam mUnitTeam;
    private volatile int mUnitAmount;

    public UnitCreatorCycle(ITeam unitTeam, EntityOperations entityOperations, int unitKey) {
        mUnitTeam = unitTeam;
        mEntityOperations = entityOperations;
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
        for (int i = 0; i < mUnitAmount; i++)
            mEntityOperations.createThickUnit(mUnitKey, mUnitTeam);
    }
}
