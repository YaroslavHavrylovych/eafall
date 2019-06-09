package com.yaroslavlancelot.eafall.game.campaign.missions;

import com.yaroslavlancelot.eafall.game.campaign.missions.utils.FindPathMissionActivity;

/** Fourth mission to reach your friends */
public class FourthMissionActivity extends FindPathMissionActivity {
    private final int[] mUnitPos = new int[]{100, 540};
    private final int[] mEndpointPos = new int[]{1680, 540};

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
        return 50;
    }

    @Override
    protected void onPopulateEnemies() {
        //first line
        createEnemy(50, new int[]{360, 1080 - 50}, new int[]{360, 1080 - 500});
        createEnemy(50, new int[]{360, 1080 - 300}, new int[]{360, 1080 - 750});
        createEnemy(21, new int[]{410, 40}, new int[]{410, 1040});
        //middle
        createEnemy(12, new int[]{720, 20}, new int[]{720, 270});
        createEnemy(12, new int[]{960, 20}, new int[]{960, 270});
        createEnemy(12, new int[]{720, 1080 - 270}, new int[]{720, 1080 - 20});
        createEnemy(12, new int[]{960, 1080 - 270}, new int[]{960, 1080 - 20});
        //end
        createEnemy(70, new int[]{1680, 540}, new int[]{1276, 540});
        createEnemy(70, new int[]{1276, 340}, new int[]{1680, 340});
        createEnemy(70, new int[]{1276, 740}, new int[]{1680, 740});
    }

    @Override
    protected String getScreenName() {
        return "Mission 4 Screen";
    }
}
