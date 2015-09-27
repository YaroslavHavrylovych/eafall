package com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.unit;

/** holds data need for unit creation */
public class CreateDefenceUnitEvent {
    private final int mKey;
    private final String mPlayerName;
    private final float mX;
    private final float mY;

    /**
     * unit creation event
     *
     * @param key      unit key in player
     * @param playerName player name to create a unit
     * @param x        abscissa
     * @param y        ordinate
     */
    public CreateDefenceUnitEvent(int key, final String playerName, float x, float y) {
        mKey = key;
        mPlayerName = playerName;
        mX = x;
        mY = y;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    /** returns unit key in player */
    public int getKey() {
        return mKey;
    }

    /** player to create unit */
    public String getPlayerName() {
        return mPlayerName;
    }
}
