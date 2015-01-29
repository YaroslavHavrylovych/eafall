package com.gmail.yaroslavlancelot.spaceinvaders.loops;

import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

/** update money amount in the game */
public class MoneyUpdateCycle implements ITimerCallback {
    public static final int MONEY_UPDATE_TIME = 10;

    /** money update cycle timer */
    public MoneyUpdateCycle() {
    }

    @Override
    public void onTimePassed(final TimerHandler pTimerHandler) {
        for (ITeam team : TeamsHolder.getInstance().getElements()) {
            team.incomeTime();
        }
    }
}
