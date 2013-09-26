package com.gmail.yaroslavlancelot.spaceinvaders.gameloop;

import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import java.util.List;

/** update money amount in the game */
public abstract class MoneyUpdateCycle implements ITimerCallback {
    private List<ITeam> mTeams;

    /**
     * create new timer
     *
     * @param teams teams money of which will be updated after each iteration
     */
    public MoneyUpdateCycle(List<ITeam> teams) {
        mTeams = teams;
    }

    @Override
    public void onTimePassed(final TimerHandler pTimerHandler) {
        for (ITeam team : mTeams) {
            team.incomeTime();
        }
        postUpdate();
    }

    /** called after money update */
    public abstract void postUpdate();
}
