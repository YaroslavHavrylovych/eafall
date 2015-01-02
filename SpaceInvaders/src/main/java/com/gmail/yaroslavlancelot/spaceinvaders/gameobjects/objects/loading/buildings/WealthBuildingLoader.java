package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.buildings;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/** building which increases your money amount in percents */
@Root(name = "wealth_building")
public class WealthBuildingLoader {
    @Attribute(name = "name")
    public String name;

    @Element(name = "first_build_income")
    public Integer first_build_income;

    @Element(name = "next_buildings_income")
    public Integer next_buildings_income;
}
