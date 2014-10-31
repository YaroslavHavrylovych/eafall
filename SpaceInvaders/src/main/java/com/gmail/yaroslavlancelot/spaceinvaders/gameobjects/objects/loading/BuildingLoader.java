package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/** read building data from unit-store xml file */
@Root(name = "building")
public class BuildingLoader {
    @Attribute(name = "name", required = true)
    public String name;

    @Element(name = "id")
    public Integer id;

    @Element(name = "position_x")
    public Integer position_x;

    @Element(name = "position_y")
    public Integer position_y;

    @ElementList(name = "upgrades")
    List<BuildingUpgradeLoader> mBuildingLoaderList = new ArrayList<BuildingUpgradeLoader>(3);

    public List<BuildingUpgradeLoader> getUpdates() {
        return mBuildingLoaderList;
    }
}
