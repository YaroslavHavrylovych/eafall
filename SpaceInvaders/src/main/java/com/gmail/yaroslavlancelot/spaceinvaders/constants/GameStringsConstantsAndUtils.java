package com.gmail.yaroslavlancelot.spaceinvaders.constants;

import java.io.File;

/** game constants for game objects */
public final class GameStringsConstantsAndUtils {
    /** sun file name */
    public static final String FILE_SUN = "sun.png";
    /** file name for red planet */
    public static final String FILE_RED_PLANET = "red_planet.png";
    /** file name for blue planet */
    public static final String FILE_BLUE_PLANET = "blue_planet.png";
    /** used like key for sun static object */
    public static final String KEY_SUN = "sun_key";
    /** used like key for red planet static object */
    public static final String KEY_RED_PLANET = "red_planet_key";
    /** used like key for blue planet static object */
    public static final String KEY_BLUE_PLANET = "blue_planet_key";
    /** red team name */
    public static final String RED_TEAM_NAME = "red";
    /** blue team name */
    public static final String BLUE_TEAM_NAME = "blue";
    /** key used to identify money font */
    public static final String KEY_FONT_MONEY = "key_money_font";

    /**
     * private constructor
     */
    private GameStringsConstantsAndUtils() {
    }

    public static String getPathToBuildings(String raceName) {
        String separator = File.separator;
        String path = "races" + separator + raceName.toLowerCase() + separator + "buildings" + separator;
        return path;
    }

    public static String getPathToUnits(String raceName) {
        String separator = File.separator;
        String path = "races" + separator + raceName.toLowerCase() + separator + "units" + separator;
        return path;
    }
}
