package com.gmail.yaroslavlancelot.eafall.game.campaign.loader.mission;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Used to load mission information
 *
 * @author Yaroslav Havrylovych
 */
@Root(name = "mission")
public class MissionDataLoader {
    @Attribute(name = "id")
    public String id;

    @Element(name = "name")
    public String name;

    @Element(name = "player_alliance")
    public String player_alliance;

    @Element(name = "opponent_alliance")
    public String opponent_alliance;

    @Element(name = "max_oxygen", required = false)
    public Integer max_oxygen;

    @Element(name = "planet_health", required = false)
    public Integer planet_health;

    @Element(name = "movable_units_limit", required = false)
    public Integer movable_units_limit;


    @Element(name = "definition")
    public DefinitionLoader definition;
}
