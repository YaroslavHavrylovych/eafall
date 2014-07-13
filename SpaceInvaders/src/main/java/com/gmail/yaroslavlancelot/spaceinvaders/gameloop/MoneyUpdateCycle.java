package com.gmail.yaroslavlancelot.spaceinvaders.gameloop;

import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import java.util.Map;

/** update money amount in the game */
public class MoneyUpdateCycle implements ITimerCallback {
    public static final int MONEY_UPDATE_TIME = 10;
    private Map<String, ITeam> mTeams;

    /**
     * create new timer
     *
     * @param teams teams money of which will be updated after each iteration
     */
    public MoneyUpdateCycle(Map<String, ITeam> teams) {
        mTeams = teams;
    }

    @Override
    public void onTimePassed(final TimerHandler pTimerHandler) {
        for (ITeam team : mTeams.values()) {
            team.incomeTime();
        }
    }
}
