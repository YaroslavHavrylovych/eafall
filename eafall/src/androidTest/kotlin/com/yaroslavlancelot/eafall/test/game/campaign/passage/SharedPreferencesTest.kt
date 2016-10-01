package com.yaroslavlancelot.eafall.test.game.campaign.passage

import com.yaroslavlancelot.eafall.game.campaign.pass.CampaignPassage
import com.yaroslavlancelot.eafall.game.campaign.pass.CampaignPassageFactory
import com.yaroslavlancelot.eafall.test.game.EaFallTestCase

/**
 * Check basic popup scene functionality
 *
 * @author Yaroslav Havrylovych
 */
class SharedPreferencesTest : EaFallTestCase() {
    val mFileName = "campaign1.test"

    fun testPassage() {
        val campaignPassage = CampaignPassageFactory.getInstance().getCampaignPassage(mFileName, context)
        campaignPassage.reset()
        assertNewlyCreated(campaignPassage)
        campaignPassage.markNewCampaignPassed()
        assertTrue(campaignPassage.passedCampaignsAmount == 1, "campaign passage should increase passed campaigns index")
        assertTrue(campaignPassage.checkCampaignPassed(0), "campaign doesn't passed, but it should")
        assertFalse(campaignPassage.checkCampaignPassed(1), "campaign shouldn't be passed")
        campaignPassage.markNewCampaignPassed()
        assertTrue(campaignPassage.passedCampaignsAmount == 2, "campaign passage should increase passed campaigns index")
        assertTrue(campaignPassage.checkCampaignPassed(0), "campaign doesn't passed, but it should")
        assertTrue(campaignPassage.checkCampaignPassed(1), "campaign doesn't passed, but it should")
        assertFalse(campaignPassage.checkCampaignPassed(2), "campaign shouldn't be passed")
    }

    fun assertNewlyCreated(campaignPassage: CampaignPassage) {
        assertTrue(campaignPassage.passedCampaignsAmount == 0, "newly created campaign has to contain zero passed missions")
        assertFalse(campaignPassage.checkCampaignPassed(0), "newly created campaign shouldn't contain passed missions")
    }
}
