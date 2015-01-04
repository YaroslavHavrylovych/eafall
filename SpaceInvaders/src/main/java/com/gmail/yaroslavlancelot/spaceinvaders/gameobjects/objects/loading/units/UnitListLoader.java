package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.units;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/** read unit list from unit-store xml file */
@Root(strict = false)
public class UnitListLoader {
    @ElementList(required = true, inline = true)
    List<UnitLoader> mUnitLoaderList = new ArrayList<UnitLoader>(8);

    public List<UnitLoader> getList() {
        return mUnitLoaderList;
    }
}
