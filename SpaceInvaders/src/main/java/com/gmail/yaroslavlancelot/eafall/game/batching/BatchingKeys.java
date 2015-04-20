package com.gmail.yaroslavlancelot.eafall.game.batching;

/**
 * Existing sprite groups keys.
 * <br/>
 * Each key have particular template "nn_key"
 * where 'nn' is order in which groups should be added. The group with biggest value
 * will be in the top of the hierarchy and vice versa (i.e. the group with
 * the smallest value will be the bottom one).
 */
public class BatchingKeys {
    public final static String BULLET_HEALTH_TEAM_COLOR = "00_bullets_and_health_bars_and_team_color";
    public final static String SUN_PLANET = "01_sun_and_planets";
    public final static String PREFIX_BUILDING = "03_building_";
    public final static String PREFIX_UNIT = "04_unit_";

    public static String getUnitSpriteGroup(String teamName) {
        return PREFIX_UNIT + teamName;
    }

    public static String getBuildingSpriteGroup(String teamName) {
        return PREFIX_BUILDING + teamName;
    }
}