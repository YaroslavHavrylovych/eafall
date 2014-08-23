package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/** read unit list from unit-store xml file */
@Root(strict = false)
public class BuildingListLoader {
    @ElementList(required = true, inline = true)
    List<BuildingLoader> mBuildingLoaderList = new ArrayList<BuildingLoader>(8);

    public List<BuildingLoader> getList() {
        return mBuildingLoaderList;
    }
}
