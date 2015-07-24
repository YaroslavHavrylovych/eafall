package com.gmail.yaroslavlancelot.eafall.game.campaign.loader;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Campaign list loader
 *
 * @author Yaroslav Havrylovych
 */
@Root(strict = false)
public class CampaignListLoader {
    @Element(name = "background")
    public String background;

    @Element(name = "parallax_background", required = false)
    public Boolean parallax_background;

    @Element(name = "music")
    public String music;

    @Element(name = "sound_select")
    public String sound_select;

    @ElementList(required = true, inline = true)
    List<CampaignDataLoader> mCampaignDataLoaderList = new ArrayList<CampaignDataLoader>(8);

    public List<CampaignDataLoader> getList() {
        return mCampaignDataLoaderList;
    }
}
