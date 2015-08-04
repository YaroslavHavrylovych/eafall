package com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.unit;

/** holds data need for unit creation */
public class CreateMovableUnitEvent {
    private final int mKey;
    private final String mPlayerName;
    private final boolean mIsTopPath;

    /**
     * unit creation event
     *
     * @param key       unit key in player
     * @param playerName  player name to create a unit
     * @param isTopPath is unit will move by top path
     */
    public CreateMovableUnitEvent(int key, final String playerName, boolean isTopPath) {
        mKey = key;
        mPlayerName = playerName;
        mIsTopPath = isTopPath;
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
}
