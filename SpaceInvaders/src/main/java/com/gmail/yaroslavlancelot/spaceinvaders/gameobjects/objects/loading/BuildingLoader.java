package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/** read building data from unit-store xml file */
@Root(name = "building")
public class BuildingLoader {
    @Attribute(name = "name")
    public String name;

    @Element(name = "cost")
    public Integer cost;

    @Element(name = "position_x")
    public Integer position_x;

    @Element(name = "position_y")
    public Integer position_y;

    @Element(name = "team_color_area")
    public TeamColorArea team_color_area;
}
