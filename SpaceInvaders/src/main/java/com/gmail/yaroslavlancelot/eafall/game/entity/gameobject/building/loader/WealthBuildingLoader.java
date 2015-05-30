package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.loader;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/** building which increases your money amount in percents */
@Root(name = "wealth_building")
public class WealthBuildingLoader {
    @Attribute(name = "name")
    public String name;

    @Element(name = "first_build_income")
    public Integer first_build_income;

    @Element(name = "next_buildings_income")
    public Integer next_buildings_income;

    @Element(name = "cost")
    public Integer cost;

    @Element(name = "position_x")
    public Integer position_x;

    @Element(name = "position_y")
    public Integer position_y;

    @Element(name = "image_name")
    public String image_name;
}
