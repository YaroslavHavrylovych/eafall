package com.gmail.yaroslavlancelot.eafall.game.campaign.loader;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Campaign screen objects which have nothing in common with particular
 * campaign or mission.
 *
 * @author Yaroslav Havrylovych
 */
@Root(name = "object")
public class ObjectDataLoader {
    @Element(name = "picture")
    public String picture;

    @Element(name = "width")
    public int width;

    @Element(name = "height")
    public int height;

    @Element(name = "rotation", required = false)
    public Integer rotation;

    @Element(name = "radius", required = false)
    public Integer radius;

    @Element(name = "duration", required = false)
    public Integer duration;

    @Element(name = "position")
    public PositionLoader position;
}
