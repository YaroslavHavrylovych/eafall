package com.yaroslavlancelot.eafall.game.campaign.intents;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.android.analytics.AnalyticsFactory;
import com.yaroslavlancelot.eafall.game.campaign.CampaignActivity;
import com.yaroslavlancelot.eafall.android.StartableIntent;
import com.yaroslavlancelot.eafall.game.resources.ResourceFactory;

/**
 * Simplified campaign start
 *
 * @author Yaroslav Havrylovych
 */
public class CampaignIntent extends StartableIntent {
    public static final String CAMPAIGN_FILE_NAME_KEY = "campaign_file_name";
    public static final String DEFAULT_CAMPAIGN = "campaign";
    public static final String GAME_RESULT_SUCCESS_KEY = "single_player_game_result_key";
    public static final String CAMPAIGN_MISSION_ID_KEY = "campaign_mission_key";

    public CampaignIntent(String campaignFileName) {
        super(EaFallApplication.getContext(), CampaignActivity.class);
        putExtra(CAMPAIGN_FILE_NAME_KEY, campaignFileName);
        putExtra(ResourceFactory.RESOURCE_LOADER, ResourceFactory.TypeResourceLoader.CAMPAIGN);
    }

    public CampaignIntent(String campaignFileName, boolean success, Integer missionId) {
        this(campaignFileName);
        if(success) {
            AnalyticsFactory.getInstance().missionCompletedEvent("Mission " + missionId + 1);
        } else {
            AnalyticsFactory.getInstance().missionsFailedEvent("Mission " + missionId + 1);
        }
        putExtra(GAME_RESULT_SUCCESS_KEY, success);
        putExtra(CAMPAIGN_MISSION_ID_KEY, missionId);
    }

    public CampaignIntent() {
        this(getPathToCampaign(DEFAULT_CAMPAIGN));
    }

    public static String getPathToCampaign(String campaignName) {
        return "campaign/" + campaignName + ".xml";
    }
}
