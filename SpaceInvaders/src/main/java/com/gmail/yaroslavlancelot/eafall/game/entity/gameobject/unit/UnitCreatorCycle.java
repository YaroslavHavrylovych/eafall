package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.unit.CreateMovableUnitEvent;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import de.greenrobot.event.EventBus;

/**
 * Handles how much unit to create and make it.
 */
public class UnitCreatorCycle implements ITimerCallback {
    private final int mUnitKey;
    private final String mTeamName;
    private volatile int mUnitsAmount;
    private volatile boolean mIsTopPath;

    public UnitCreatorCycle(String teamName, int unitKey, boolean isTopPath) {
        this(teamName, unitKey, 0, isTopPath);
    }

    public UnitCreatorCycle(String teamName, int unitKey, int unitsAmount, boolean isTopPath) {
        mTeamName = teamName;
        mUnitKey = unitKey;
        mUnitsAmount = unitsAmount;
        mIsTopPath = isTopPath;
    }

    public void setUnitMovementPath(boolean isTopPath) {
        mIsTopPath = isTopPath;
    }

    public void increaseUnitsAmount() {
        mUnitsAmount++;
    }

    @SuppressWarnings("unused")
    public int getUnitsAmount() {
        return mUnitsAmount;
    }

    @Override
    public void onTimePassed(final TimerHandler pTimerHandler) {
        for (int i = 0; i < mUnitsAmount; i++) {
            //prevents cycle continuing when max amount reached (but its not the final check)
            if (TeamsHolder.getTeam(mTeamName).getUnitsAmount() >=
                    Config.getConfig().getMovableUnitsLimit()) {
                return;
            }
            EventBus.getDefault().post(new CreateMovableUnitEvent(mUnitKey, mTeamName, mIsTopPath));
        }
    }
}
