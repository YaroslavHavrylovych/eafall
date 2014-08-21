package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.loading;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "team_color_area")
public class TeamColorArea {
    @Element(name = "x")
    public Float x;

    @Element(name = "y")
    public Float y;

    @Element(name = "width")
    public Float width;

    @Element(name = "height")
    public Float height;
}
