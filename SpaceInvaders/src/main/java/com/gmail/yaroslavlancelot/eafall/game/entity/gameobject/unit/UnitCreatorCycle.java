package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import com.gmail.yaroslavlancelot.eafall.game.eventbus.unit.CreateMovableUnitEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import de.greenrobot.event.EventBus;

/**
 * Handles how much unit to create and make it.
 */
public class UnitCreatorCycle implements ITimerCallback {
    private final int mUnitKey;
    private final String mPlayerName;
    private volatile int mUnitsAmount;
    private volatile boolean mIsTopPath;

    public UnitCreatorCycle(String playerName, int unitKey, boolean isTopPath) {
        this(playerName, unitKey, 0, isTopPath);
    }

    public UnitCreatorCycle(String playerName, int unitKey, int unitsAmount, boolean isTopPath) {
        mPlayerName = playerName;
        mUnitKey = unitKey;
        mUnitsAmount = unitsAmount;
        mIsTopPath = isTopPath;
    }

    @SuppressWarnings("unused")
    public int getUnitsAmount() {
        return mUnitsAmount;
    }

    public void setUnitMovementPath(boolean isTopPath) {
        mIsTopPath = isTopPath;
    }

    @Override
    public void onTimePassed(final TimerHandler pTimerHandler) {
        for (int i = 0; i < mUnitsAmount; i++) {
            //TODO this idea will be totally changed. We will use another layer
            //and will check this layer about possibility to create unit and will
            //add new unit through this layer. It will be some kind of cosmo-port
            //who know can he ship a unit or not. So next limit hardcoded (it's tmp)

            //prevents cycle continuing when max amount reached (but its not the final check)
            if (PlayersHolder.getPlayer(mPlayerName).getUnitsAmount() >= 200) {
                return;
            }
            EventBus.getDefault().post(new CreateMovableUnitEvent(mUnitKey, mPlayerName, mIsTopPath));
        }
    }

    public void increaseUnitsAmount() {
        mUnitsAmount++;
    }
}
