package com.gmail.yaroslavlancelot.eafall.game.eventbus.unit;

/** holds data need for unit creation */
public class CreateStationaryUnitEvent {
    private final int mKey;
    private final String mTeamName;
    private final float mX;
    private final float mY;

    /**
     * unit creation event
     *
     * @param key      unit key in team
     * @param teamName team name to create a unit
     * @param x        abscissa
     * @param y        ordinate
     */
    public CreateStationaryUnitEvent(int key, final String teamName, float x, float y) {
        mKey = key;
        mTeamName = teamName;
        mX = x;
        mY = y;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
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
