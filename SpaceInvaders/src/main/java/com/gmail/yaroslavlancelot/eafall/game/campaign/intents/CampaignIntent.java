package com.gmail.yaroslavlancelot.eafall.game.campaign.intents;

import android.app.Activity;
import android.content.Intent;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.campaign.CampaignActivity;
import com.gmail.yaroslavlancelot.eafall.game.resources.ResourceFactory;

/**
 * Simplified campaign start
 *
 * @author Yaroslav Havrylovych
 */
public class CampaignIntent extends Intent {
    public static final String CAMPAIGN_FILE_NAME = "campaign_file_name";

    public CampaignIntent(String campaignFileName) {
        super(EaFallApplication.getContext(), CampaignActivity.class);
        putExtra(CAMPAIGN_FILE_NAME, campaignFileName);
        putExtra(ResourceFactory.RESOURCE_LOADER, ResourceFactory.TypeResourceLoader.CAMPAIGN);
    }

    public void start(Activity activity) {
        activity.startActivity(this);
    }
}
