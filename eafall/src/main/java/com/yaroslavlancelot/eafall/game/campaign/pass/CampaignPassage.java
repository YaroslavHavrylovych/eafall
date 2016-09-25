package com.yaroslavlancelot.eafall.game.campaign.pass;

/**
 * Track campaign passage statistic. Saves data about passed missions and
 * closed missions.
 *
 * @author Yaroslav Havrylovych
 */
public interface CampaignPassage {
    /** return amount of passed missions */
    int getPassedCampaignsAmount();

    /** mark that one more mission/campaign has been passed */
    void markNewCampaignPassed();

    /**
     * checks if campaign was passed
     *
     * @param position position of campaign in the campaigns list
     * @return true if campaign has been passed by the user and false in othe case
     */
    boolean checkCampaignPassed(int position);

    /** reset campaign passage */
    void reset();
}
