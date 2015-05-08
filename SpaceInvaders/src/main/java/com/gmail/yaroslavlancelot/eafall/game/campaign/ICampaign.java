package com.gmail.yaroslavlancelot.eafall.game.campaign;

import android.app.Activity;

/**
 * Abstract campaign interface.
 *
 * @author Yaroslav Havrylovych
 */
public interface ICampaign {
    /**
     * start campaign base screen
     *
     * @param activity used as context to start new activity
     */
    void startCampaignActivity(Activity activity);

    /**
     * start particular campaign mission
     *
     * @param missionId determines particular mission
     */
    void startMission(int missionId);
}
