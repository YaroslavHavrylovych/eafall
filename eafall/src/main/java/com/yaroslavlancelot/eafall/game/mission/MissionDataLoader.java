package com.yaroslavlancelot.eafall.game.mission;

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

    @Element(name = "player_planet")
    public String player_planet;

    @Element(name = "opponent_alliance")
    public String opponent_alliance;

    @Element(name = "opponent_planet")
    public String opponent_planet;

    @Element(name = "start_money", required = false)
    public Integer start_money;

    @Element(name = "max_oxygen", required = false)
    public Integer max_oxygen;

    @Element(name = "planet_health", required = false)
    public Integer planet_health;

    @Element(name = "offensive_units_limit", required = false)
    public Integer offensive_units_limit;

    @Element(name = "star_code_name", required = false)
    public String star_code_name;

    @Element(name = "star_constellation", required = false)
    public String star_constellation;


    @Element(name = "definition")
    public DefinitionLoader definition;
}
