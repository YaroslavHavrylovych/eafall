package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.loading.buildings;

import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.loading.TeamColorArea;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/** update of particular building */
@Root(name = "upgrade")
public class CreepBuildingUpgradeLoader {
    @Attribute(name = "id", required = true)
    public Integer id;

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
