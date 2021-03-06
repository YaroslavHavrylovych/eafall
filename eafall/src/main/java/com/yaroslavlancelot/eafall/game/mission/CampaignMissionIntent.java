package com.yaroslavlancelot.eafall.game.mission;

import com.yaroslavlancelot.eafall.game.campaign.intents.CampaignIntent;

/** Intent used to launch campaign missions */
public class CampaignMissionIntent extends MissionIntent {

    public CampaignMissionIntent(Class activityClass, MissionDetailsLoader missionData,
                                 String campaignFileName, Integer missionId) {
        super(activityClass, missionData);
        putExtra(CampaignIntent.CAMPAIGN_FILE_NAME_KEY, campaignFileName);
        putExtra(CampaignIntent.CAMPAIGN_MISSION_ID_KEY, missionId);
    }
}
