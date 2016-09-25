package com.yaroslavlancelot.eafall.game.campaign.loader;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Particular campaign file loader
 *
 * @author Yaroslav Havrylovych
 */
@Root(strict = false)
public class CampaignFileLoader {
    @Element(name = "background")
    public String background;

    @Element(name = "parallax_background", required = false)
    public Boolean parallax_background;

    @ElementList(inline = true)
    private List<CampaignDataLoader> mCampaignDataLoaderList = new ArrayList<>(8);

    @ElementList(required = false, inline = true)
    private List<ObjectDataLoader> mCampaignObjectLoaderList = new ArrayList<>(2);

    public List<CampaignDataLoader> getCampaignsList() {
        return mCampaignDataLoaderList;
    }

    public List<ObjectDataLoader> getObjectsList() {
        return mCampaignObjectLoaderList;
    }
}
