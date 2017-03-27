package com.yaroslavlancelot.eafall.game.campaign.missions;

import com.yaroslavlancelot.eafall.game.campaign.missions.utils.FindPathMissionActivity;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;

/** Fourth missin to reach your friends */
public class FourthMissionActivity extends FindPathMissionActivity {
    private final int[] mUnitPos = new int[]{600, 600};

    @Override
    protected int[] getMainUnitCoords() {
        return mUnitPos;
    }

    @Override
    protected int getMainUnitId() {
        return 21;
    }
}
