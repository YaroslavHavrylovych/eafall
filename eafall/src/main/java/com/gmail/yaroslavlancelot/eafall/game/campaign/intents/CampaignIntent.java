package com.gmail.yaroslavlancelot.eafall.game.campaign.intents;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.campaign.CampaignActivity;
import com.gmail.yaroslavlancelot.eafall.game.resources.ResourceFactory;

/**
 * Simplified campaign start
 *
 * @author Yaroslav Havrylovych
 */
public class CampaignIntent extends StartableIntent {
    public static final String CAMPAIGN_FILE_NAME = "campaign_file_name";
    private static final String DEFAULT_CAMPAIGN = "guide_campaign";


    public CampaignIntent(String campaignFileName) {
        super(EaFallApplication.getContext(), CampaignActivity.class);
        putExtra(CAMPAIGN_FILE_NAME, getPathToCampaign(campaignFileName));
        putExtra(ResourceFactory.RESOURCE_LOADER, ResourceFactory.TypeResourceLoader.CAMPAIGN);
    }

    public CampaignIntent() {
        this(DEFAULT_CAMPAIGN);
    }

    public static String getPathToCampaign(String campaignName) {
        return "campaign/" + campaignName + ".xml";
    }
}
