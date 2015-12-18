package com.yaroslavlancelot.eafall.game.campaign.loader;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Loads coordinates from xml
 *
 * @author Yaroslav Havrylovych
 */

@Root(name = "position")
public class PositionLoader {
    @Element(name = "x")
    public Float x;

    @Element(name = "y")
    public Float y;
}
