package com.yaroslavlancelot.eafall.game.campaign.missions;

import com.yaroslavlancelot.eafall.game.campaign.missions.utils.FindPathMissionActivity;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;

/** Fourth missin to reach your friends */
public class FourthMissionActivity extends FindPathMissionActivity {
    private final int[] mUnitPos = new int[]{600, 600};
    private final int[] mEndpointPos = new int[]{1200, 540};

    @Override
    protected int[] getMainUnitCoords() {
        return mUnitPos;
    }

    @Override
    protected int[] getEndpointCoords() {
        return mEndpointPos;
    }

    @Override
    protected int getMainUnitId() {
        return 21;
    }

    @Override
    protected void onPopulateEnemies() {
        createEnemy(20, new int[]{10, 100}, new int[]{200, 300});
        createEnemy(12, new int[]{100, 10}, new int[]{300, 200});
    }
}
