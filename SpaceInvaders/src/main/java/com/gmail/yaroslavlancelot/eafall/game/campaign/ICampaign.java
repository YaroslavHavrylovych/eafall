package com.gmail.yaroslavlancelot.eafall.game.campaign;

/**
 * Abstract campaign interface.
 *
 * @author Yaroslav Havrylovych
 */
public interface ICampaign {
    /** start campaign base screen */
    void startCampaign();

    /**
     * start particular campaign mission
     *
     * @param missionId determines particular mission
     */
    void startMission(int missionId);
}
