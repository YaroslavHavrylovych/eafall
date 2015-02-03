package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.loader;

import com.gmail.yaroslavlancelot.eafall.game.entity.TeamColorArea;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/** give ability to create defence buildings (orbital stations) */
@Root(name = "defence_building")
public class DefenceBuildingLoader {
    @Attribute(name = "name")
    public String name;

    @Element(name = "position_x")
    public Integer position_x;

    @Element(name = "position_y")
    public Integer position_y;

    @Element(name = "image_name")
    public String image_name;

    @Element(name = "cost")
    public Integer cost;

    @Element(name = "unit_id")
    public Integer unit_id;

    @Element(name = "building_time")
    public Integer building_time;

    @Element(name = "team_color_area")
    public TeamColorArea team_color_area;
}
