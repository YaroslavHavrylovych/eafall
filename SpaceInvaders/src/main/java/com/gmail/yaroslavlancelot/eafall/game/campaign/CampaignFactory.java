package com.gmail.yaroslavlancelot.eafall.game.campaign;

import com.gmail.yaroslavlancelot.eafall.game.campaign.campaigns.GuideCampaign;

/**
 * Used to get particular ICampaign instance (short just Campaign) with
 * possibility to start it.
 *
 * @author Yaroslav Havrylovych
 */
public class CampaignFactory {
    private static final CampaignFactory sInstance = new CampaignFactory();

    private CampaignFactory() {
    }

    public static CampaignFactory getInstance() {
        return sInstance;
    }

    public ICampaign getCampaign(TypeCampaign campaign) {
        switch (campaign) {
            case GUIDE_CAMPAIGN: {
                return new GuideCampaign();
            }
        }
        return new GuideCampaign();
    }

    public enum TypeCampaign {
        GUIDE_CAMPAIGN
    }
}
