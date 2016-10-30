package com.yaroslavlancelot.eafall.game.entity.gameobject.building;

/**
 * buildings have custom screen's. Each building have not just int as screen but
 * it's screen and upgrade. So the real building screen is the combination of the building screen + it's upgrade
 */
public class BuildingId {
    /** building line screen */
    private final int mId;
    /** current building line upgrade */
    private final int mUpgrade;

    private BuildingId(int id, int upgrade) {
        mId = id;
        mUpgrade = upgrade;
    }

    public BuildingId getNextUpgrade() {
        return BuildingId.makeId(mId, mUpgrade + 1);
    }

    public static BuildingId makeId(int id, int upgrade) {
        return new BuildingId(id, upgrade);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof BuildingId)) {
            return false;
        }
        BuildingId buildingId = (BuildingId) o;
        return getId() == buildingId.getId() && getUpgrade() == buildingId.getUpgrade();
    }

    @Override
    public int hashCode() {
        return getId() + getUpgrade();
    }

    @Override
    public String toString() {
        return "BuildingId[" + mId + "," + mUpgrade + "]";
    }

    public int getUpgrade() {
        return mUpgrade;
    }

    public int getId() {
        return mId;
    }
}
