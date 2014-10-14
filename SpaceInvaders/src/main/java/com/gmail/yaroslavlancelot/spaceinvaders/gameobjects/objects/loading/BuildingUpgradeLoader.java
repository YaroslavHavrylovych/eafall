package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/** update of particular building */
@Root(name = "upgrade")
public class BuildingUpgradeLoader {
    @Attribute(name = "id", required = true)
    public Integer id;

    @Element(name = "image_name")
    public String image_name;

    @Element(name = "cost")
    public Integer cost;

    @Element(name = "unit_id")
    public Integer unit_id;

    @Element(name = "team_color_area")
    public TeamColorArea team_color_area;
}
