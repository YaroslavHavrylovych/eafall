package com.yaroslavlancelot.eafall.game.mission;

import com.yaroslavlancelot.eafall.game.campaign.intents.CampaignIntent;
import com.yaroslavlancelot.eafall.game.resources.ResourceFactory;

/** Intent used to launch campaign missions */
public class CampaignFindPathMissionIntent extends MissionIntent {

    public CampaignFindPathMissionIntent(Class activityClass, MissionDetailsLoader missionData,
                                         String campaignFileName, Integer missionId) {
        super(activityClass, missionData);
        putExtra(CampaignIntent.CAMPAIGN_FILE_NAME_KEY, campaignFileName);
        putExtra(CampaignIntent.CAMPAIGN_MISSION_ID_KEY, missionId);
        putExtra(ResourceFactory.RESOURCE_LOADER, ResourceFactory.TypeResourceLoader.FIND_PATH);
    }
}
