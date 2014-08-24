package com.gmail.yaroslavlancelot.spaceinvaders.eventbus;

/** holds data need for building creation */
public class CreateBuildingEvent {
    private final int mKey;
    private final String mTeamName;

    /**
     * building creation event
     *
     * @param teamName team name to create a building
     * @param key      building key in team
     */
    public CreateBuildingEvent(final String teamName, int key) {
        mKey = key;
        mTeamName = teamName;
    }

    /** returns building key in team */
    public int getKey() {
        return mKey;
    }

    /** team to create building */
    public String getTeamName() {
        return mTeamName;
    }
}
