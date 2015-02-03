package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.loader;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/** read unit list from unit-store xml file */
@Root(strict = false)
public class BuildingListLoader {
    @ElementList(inline = true)
    List<CreepBuildingLoader> creepBuildingLoaderList = new ArrayList<CreepBuildingLoader>(8);

    @Element(name = "defence_building")
    public DefenceBuildingLoader defenceBuildingLoader;

    @Element(name = "wealth_building")
    public WealthBuildingLoader wealthBuildingLoader;

    @Element(name = "special_building")
    public SpecialBuildingLoader specialBuildingLoader;

    public List<CreepBuildingLoader> getList() {
        return creepBuildingLoaderList;
    }
}
