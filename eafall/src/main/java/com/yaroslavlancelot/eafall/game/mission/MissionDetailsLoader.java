package com.yaroslavlancelot.eafall.game.mission;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Used to load mission information
 *
 * @author Yaroslav Havrylovych
 */
@Root(name = "mission_details")
public class MissionDetailsLoader {
    @Element(name = "name")
    public String name;

    @Element(name = "game_handler", required = false)
    public String game_handler;

    @Element(name = "blue_sun")
    public Boolean blue_sun;

    @Element(name = "player_alliance")
    public String player_alliance;

    @Element(name = "player_available_buildings", required = false)
    public Integer player_available_buildings;

    @Element(name = "player_planet")
    public String player_planet;

    @Element(name = "player_start_money", required = false)
    public Integer player_start_money;

    @Element(name = "opponent_alliance")
    public String opponent_alliance;

    @Element(name = "opponent_available_buildings", required = false)
    public Integer opponent_available_buildings;

    @Element(name = "opponent_planet")
    public String opponent_planet;

    @Element(name = "opponent_start_money", required = false)
    public Integer opponent_start_money;

    @Element(name = "max_oxygen", required = false)
    public Integer max_oxygen;

    @Element(name = "planet_health", required = false)
    public Integer planet_health;

    @Element(name = "offensive_units_limit", required = false)
    public Integer offensive_units_limit;

    @Element(name = "enemy_logic_handler", required = false)
    public String enemy_logic_handler;

    @Element(name = "star_code_name", required = false)
    public String star_code_name;

    @Element(name = "star_constellation", required = false)
    public String star_constellation;

    @Element(name = "single_way", required = false)
    public Boolean single_way;

    @Element(name = "suppressor", required = false)
    public Boolean suppressor;

    @Element(name = "definition")
    public DefinitionLoader definition;
}
