package com.gmail.yaroslavlancelot.eafall.game.campaign.campaigns;

import android.app.Activity;

import com.gmail.yaroslavlancelot.eafall.game.campaign.ICampaign;
import com.gmail.yaroslavlancelot.eafall.game.campaign.intents.CampaignIntent;

/**
 * List of possible campaigns.
 *
 * @author Yaroslav Havrylovych
 */
public class GuideCampaign implements ICampaign {
    @Override
    public void startCampaignActivity(Activity activity) {
        CampaignIntent intent = new CampaignIntent("campaign/guide_campaign.xml");
        intent.start(activity);
    }

    @Override
    public void startMission(int missionId) {
        throw new UnsupportedOperationException("No missions in guide campaign (it's only presentative page)");
    }
}
