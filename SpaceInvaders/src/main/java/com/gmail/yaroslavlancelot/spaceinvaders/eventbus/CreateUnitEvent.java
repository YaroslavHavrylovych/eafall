package com.gmail.yaroslavlancelot.spaceinvaders.eventbus;

/** holds data need for unit creation */
public class CreateUnitEvent {
    private final int mKey;
    private final String mTeamName;
    private final boolean mIsTopPath;

    /**
     * unit creation event
     *
     * @param key       unit key in team
     * @param teamName  team name to create a unit
     * @param isTopPath is unit will move by top path
     */
    public CreateUnitEvent(int key, final String teamName, boolean isTopPath) {
        mKey = key;
        mTeamName = teamName;
        mIsTopPath = isTopPath;
    }

    /** is unit will use top path to move */
    public boolean isTopPath() {
        return mIsTopPath;
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
