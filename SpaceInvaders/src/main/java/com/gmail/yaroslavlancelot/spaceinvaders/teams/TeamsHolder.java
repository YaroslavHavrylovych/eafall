package com.gmail.yaroslavlancelot.spaceinvaders.teams;

import com.gmail.yaroslavlancelot.spaceinvaders.utils.HolderUtils;

/** ot have access to the teams from any point only by team name */
public class TeamsHolder extends HolderUtils<ITeam> {
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
