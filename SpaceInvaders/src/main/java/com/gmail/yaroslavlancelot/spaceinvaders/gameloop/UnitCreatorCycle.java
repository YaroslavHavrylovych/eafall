package com.gmail.yaroslavlancelot.spaceinvaders.gameloop;

import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

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
            mEntityOperations.createUnitForTeam(mUnitKey, mUnitTeam);
    }
}
