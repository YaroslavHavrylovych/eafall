package com.gmail.yaroslavlancelot.spaceinvaders.eventbus;

/** after building was created */
public class BuildingsAmountChangedEvent {
    private final int mKey;
    private final int mNewBuildingsAmount;
    private final String mTeamName;

    /**
     * building created event
     *
     * @param teamName           team name to create a building
     * @param key                building key in team
     * @param newBuildingsAmount new buildings amount
     */
    public BuildingsAmountChangedEvent(final String teamName, int key, int newBuildingsAmount) {
        mKey = key;
        mTeamName = teamName;
        mNewBuildingsAmount = newBuildingsAmount;
    }

    /** returns building key/id in team */
    public int getKey() {
        return mKey;
    }

    /** team to create building */
    public String getTeamName() {
        return mTeamName;
    }

    public int getNewBuildingsAmount() {
        return mNewBuildingsAmount;
    }
}
