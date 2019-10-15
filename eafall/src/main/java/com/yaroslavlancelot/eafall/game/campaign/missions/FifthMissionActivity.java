package com.yaroslavlancelot.eafall.game.campaign.missions;

import com.yaroslavlancelot.eafall.game.campaign.missions.utils.FindPathMissionActivity;

/** Sixth mission to reach your friends */
public class FifthMissionActivity extends FindPathMissionActivity {
    private final int[] mUnitPos = new int[]{1785, 540};
    private final int[] mEndpointPos = new int[]{200, 540};

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
        return 71;
    }

    @Override
    protected void onPopulateEnemies() {
        //top down
        createEnemy(30, new int[]{480,  220}, new int[]{480,  810}, new int[] {480,  270});
        createEnemy(30, new int[]{720,  220}, new int[]{720,  810}, new int[] {720,  405});
        createEnemy(30, new int[]{960,  220}, new int[]{960,  810}, new int[] {960,  540});
        createEnemy(30, new int[]{1200, 220}, new int[]{1200, 810}, new int[] {1200, 675});
        createEnemy(30, new int[]{1440, 220}, new int[]{1440, 810}, new int[] {1440, 810});
        //bottom up
        createEnemy(30, new int[]{360,  860}, new int[]{360,  270}, new int[] {360,  810});
        createEnemy(30, new int[]{600,  860}, new int[]{600,  270}, new int[] {600,  675});
        createEnemy(30, new int[]{840,  860}, new int[]{840,  270}, new int[] {840,  540});
        createEnemy(30, new int[]{1080, 860}, new int[]{1080, 270}, new int[] {1080, 405});
        createEnemy(30, new int[]{1320, 860}, new int[]{1320, 270}, new int[] {1320, 270});
        createEnemy(30, new int[]{1560, 860}, new int[]{1560, 270}, new int[] {1560, 405});
        //top
        createEnemy(30, new int[]{360, 90}, new int[]{1560, 90});
        createEnemy(30, new int[]{1560, 135}, new int[]{360, 135});
        //bottom
        createEnemy(30, new int[]{360, 990}, new int[]{1560, 990});
        createEnemy(30, new int[]{1560, 945}, new int[]{360, 945});
    }

    @Override
    protected String getScreenName() {
        return "Mission 6 Screen";
    }
}
