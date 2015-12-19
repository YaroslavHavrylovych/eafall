package com.yaroslavlancelot.eafall.game.entity.gameobject.building.loader;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/** read building data from unit-store xml file */
@Root(name = "units_building")
public class UnitBuildingLoader {
    @Attribute(name = "name", required = true)
    public String name;

    @Element(name = "id")
    public Integer id;

    @Element(name = "position_x")
    public Integer position_x;

    @Element(name = "position_y")
    public Integer position_y;

    @ElementList(name = "upgrades")
    List<UnitBuildingUpgradeLoader> mBuildingLoaderList = new ArrayList<UnitBuildingUpgradeLoader>(3);

    public List<UnitBuildingUpgradeLoader> getUpdates() {
        return mBuildingLoaderList;
    }
}
