package com.gmail.yaroslavlancelot.eafall.game.campaign.loader;

import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.mission.MissionDataLoader;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Particular campaign data
 *
 * @author Yaroslav Havrylovych
 */
@Root(name = "campaign")
public class CampaignDataLoader {
    @Attribute(name = "id")
    public Integer id;

    @Element(name = "name")
    public String name;

    @Element(name = "picture")
    public String picture;

    @Element(name = "width")
    public int width;

    @Element(name = "height")
    public int height;

    @Element(name = "rotation", required = false)
    public Integer rotation;

    @Element(name = "position")
    public PositionLoader position;

    @Element(name = "mission", required = false)
    public MissionDataLoader mission;

    public boolean isMission() {
        return mission != null;
    }
}
