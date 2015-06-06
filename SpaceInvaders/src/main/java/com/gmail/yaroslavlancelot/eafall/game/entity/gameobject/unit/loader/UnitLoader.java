package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/** read unit data from unit-store xml file */
@Root(name = "unit")
public class UnitLoader {
    @Attribute(name = "name")
    public String name;

    @Element(name = "id")
    public Integer id;

    @Element(name = "health")
    public Integer health;

    @Element(name = "armor")
    public String armor;

    @Element(name = "armor_value")
    public Integer armor_value;

    @Element(name = "damage")
    public String damage;

    @Element(name = "damage_value")
    public Integer damage_value;

    @Element(name = "attack_radius")
    public Integer attack_radius;

    @Element(name = "view_radius")
    public Integer view_radius;

    @Element(name = "speed")
    public Integer speed;

    @Element(name = "reload_time")
    public Float reload_time;

    @Element(name = "sound")
    public String sound;
}
