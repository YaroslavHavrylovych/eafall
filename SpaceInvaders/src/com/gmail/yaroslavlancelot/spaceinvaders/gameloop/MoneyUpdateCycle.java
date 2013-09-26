package com.gmail.yaroslavlancelot.spaceinvaders.gameloop;

import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import java.util.List;

public abstract class MoneyUpdateCycle implements ITimerCallback {
    private List<ITeam> mTeams;

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

    public abstract void postUpdate();
}
