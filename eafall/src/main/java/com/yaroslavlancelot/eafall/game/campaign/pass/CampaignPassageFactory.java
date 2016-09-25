package com.yaroslavlancelot.eafall.game.campaign.pass;

import android.content.Context;

/**
 * Used to get {@link CampaignPassage} instance
 */
public class CampaignPassageFactory {
    private static final CampaignPassageFactory sInstance = new CampaignPassageFactory();

    private CampaignPassageFactory() {
    }

    public static CampaignPassageFactory getInstance() {
        return sInstance;
    }

    /**
     * Creates new campaign passage instance
     *
     * @param campaignFileName name of the campaign file
     * @param context          used to create campaign passage
     * @return new campaign passage instance
     */
    public CampaignPassage getCampaignPassage(String campaignFileName, Context context) {
        return new CampaignPassageSharedPrefImpl(campaignFileName, context);
    }
}
