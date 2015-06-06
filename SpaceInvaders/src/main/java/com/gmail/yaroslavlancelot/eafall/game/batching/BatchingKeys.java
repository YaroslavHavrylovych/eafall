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
    public final static String BULLET_AND_HEALTH = "00_bullets_and_health_bars";
    public final static String SUN_PLANET = "01_sun_and_planets";
    public final static String PREFIX_BUILDING = "03_building_";
    public final static String PREFIX_UNIT = "04_unit_";

    public static String getUnitSpriteGroup(String playerName) {
        return PREFIX_UNIT + playerName;
    }

    public static String getBuildingSpriteGroup(String playerName) {
        return PREFIX_BUILDING + playerName;
    }
}
