package com.gmail.yaroslavlancelot.spaceinvaders.eventbus;

/** holds data need for unit creation */
public class CreateUnitEvent {
    private final int mKey;
    private final String mTeamName;

    /**
     * unit creation event
     *
     * @param key      unit key in team
     * @param teamName team name to create a unit
     */
    public CreateUnitEvent(int key, final String teamName) {
        mKey = key;
        mTeamName = teamName;
    }

    /** returns unit key in team */
    public int getKey() {
        return mKey;
    }

    /** team to create unit */
    public String getTeamName() {
        return mTeamName;
    }
}
