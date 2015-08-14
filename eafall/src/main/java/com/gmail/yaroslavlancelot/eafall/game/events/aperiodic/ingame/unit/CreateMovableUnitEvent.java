package com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.unit;

/** holds data need for unit creation */
public class CreateMovableUnitEvent {
    private final int mKey;
    private final String mPlayerName;
    private final boolean mIsTopPath;
    private final int mX;
    private final int mY;

    /**
     * unit creation event
     *
     * @param playerName player name to create a unit
     * @param key        unit key in player
     * @param isTopPath  is unit will move by top path
     * @param x          unit abscissa
     * @param x          unit ordinate
     */
    public CreateMovableUnitEvent(final String playerName, int key, boolean isTopPath, int x, int y) {
        mKey = key;
        mPlayerName = playerName;
        mIsTopPath = isTopPath;
        mX = x;
        mY = y;
    }

    /** is unit will use top path to move */
    public boolean isTopPath() {
        return mIsTopPath;
    }

    /** returns unit key in player */
    public int getKey() {
        return mKey;
    }

    /** player to create unit */
    public String getPlayerName() {
        return mPlayerName;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }
}
