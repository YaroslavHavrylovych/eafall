package com.yaroslavlancelot.eafall.game.entity.gameobject.building.loader;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/** update of particular building */
@Root(name = "upgrade")
public class UnitBuildingUpgradeLoader {
    @Attribute(name = "screen", required = true)
    public Integer id;

    @Element(name = "image_name")
    public String image_name;

    @Element(name = "cost")
    public Integer cost;

    @Element(name = "unit_id")
    public Integer unit_id;

    @Element(name = "building_time")
    public Integer building_time;
}
