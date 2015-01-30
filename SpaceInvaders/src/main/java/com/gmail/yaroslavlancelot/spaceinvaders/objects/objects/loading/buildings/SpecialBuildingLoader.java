package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.loading.buildings;

import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.loading.TeamColorArea;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/** gives some each race specific thing (e.g. increase units health, attack or etc) */
@Root(name = "wealth_building")
public class SpecialBuildingLoader {
    @Attribute(name = "name")
    public String name;

    @Element(name = "characteristic")
    public String characteristic;

    @Element(name = "value")
    public Integer value;

    @Element(name = "percentage")
    public Boolean percentage;

    @Element(name = "cost")
    public Integer cost;

    @Element(name = "position_x")
    public Integer position_x;

    @Element(name = "position_y")
    public Integer position_y;

    @Element(name = "image_name")
    public String image_name;

    @Element(name = "team_color_area")
    public TeamColorArea team_color_area;
}
