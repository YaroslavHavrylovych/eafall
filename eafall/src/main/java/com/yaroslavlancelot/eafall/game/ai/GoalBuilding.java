package com.yaroslavlancelot.eafall.game.ai;

import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;

/** Used in bot logic to mark next building goal to build or to upgrade */
class GoalBuilding {
    private final boolean mUpdate;
    private final BuildingId mBuildingId;

    GoalBuilding(BuildingId buildingId, boolean update) {
        mBuildingId = buildingId;
        mUpdate = update;
    }

    public BuildingId getBuildingId() {
        return mBuildingId;
    }

    public boolean isUpdate() {
        return mUpdate;
    }
}
