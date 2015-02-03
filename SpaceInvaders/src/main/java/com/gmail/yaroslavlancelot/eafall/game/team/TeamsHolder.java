package com.gmail.yaroslavlancelot.eafall.game.team;

import com.gmail.yaroslavlancelot.eafall.general.Holder;

/** ot have access to the teams from any point only by team name */
public class TeamsHolder extends Holder<ITeam> {
    /** current class instance (singleton implementation) */
    private final static TeamsHolder sInstance = new TeamsHolder();

    private TeamsHolder() {
    }

    public static ITeam getTeam(String teamName) {
        return getInstance().getElement(teamName);
    }

    public static TeamsHolder getInstance() {
        return sInstance;
    }
}
