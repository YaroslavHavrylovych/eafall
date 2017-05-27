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
    /** campaign file name */
    private String mFileName;
    @Element(name = "background")
    public String background;

    @Element(name = "parallax_background", required = false)
    public Boolean parallax_background;

    @ElementList(inline = true)
    private List<CampaignDataLoader> mCampaignDataLoaderList = new ArrayList<>(8);

    @ElementList(required = false, inline = true)
    private List<ObjectDataLoader> mCampaignObjectLoaderList = new ArrayList<>(2);

    public List<CampaignDataLoader> getCampaignsList() {
        checkInit();
        return mCampaignDataLoaderList;
    }

    public List<ObjectDataLoader> getObjectsList() {
        checkInit();
        return mCampaignObjectLoaderList;
    }

    /**
     * Check is campaign file loaded init fully
     *
     * @throws IllegalStateException if it doesn't init
     */
    private void checkInit() {
        if (mFileName == null) {
            throw new IllegalStateException("CampaignFileLoaded not totally init. File name missing.");
        }
    }

    public void init(String fileName) {
        mFileName = fileName;
    }
}
