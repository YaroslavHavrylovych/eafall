package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.buildings;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/** read unit list from unit-store xml file */
@Root(strict = false)
public class BuildingListLoader {
    @ElementList(inline = true)
    List<CreepBuildingLoader> mCreepBuildingLoaderList = new ArrayList<CreepBuildingLoader>(8);

    @Element(name = "wealth_building")
    public WealthBuildingLoader mWealthBuildingLoader;

    public List<CreepBuildingLoader> getList() {
        return mCreepBuildingLoaderList;
    }
}
