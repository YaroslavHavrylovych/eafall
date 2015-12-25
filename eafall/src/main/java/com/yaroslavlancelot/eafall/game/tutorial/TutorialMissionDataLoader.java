package com.yaroslavlancelot.eafall.game.tutorial;

import com.yaroslavlancelot.eafall.game.alliance.mutants.Mutants;
import com.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.yaroslavlancelot.eafall.game.mission.DefinitionLoader;
import com.yaroslavlancelot.eafall.game.mission.MissionDataLoader;

/**
 * @author Yaroslav Havrylovych
 */
public class TutorialMissionDataLoader extends MissionDataLoader {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public TutorialMissionDataLoader() {
        reset();
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    public void reset() {
        id = "0";
        name = "tutorial_mission";
        player_alliance = Mutants.ALLIANCE_NAME;
        player_planet = "earth";
        player_start_money = 1865;
        opponent_alliance = Mutants.ALLIANCE_NAME;
        opponent_planet = "faked_earth";
        opponent_start_money = 4500;
        max_oxygen = 5000;
        planet_health = 5000;
        offensive_units_limit = 50;
        star_code_name = "sun";
        star_constellation = "no_constellation";
        definition = new DefinitionLoader();
        definition.time_limit = 130;
        definition.type = MissionConfig.MissionType.SURVIVE.toString();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
