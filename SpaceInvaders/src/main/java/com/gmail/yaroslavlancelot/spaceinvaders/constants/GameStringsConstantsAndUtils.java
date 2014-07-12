package com.gmail.yaroslavlancelot.spaceinvaders.constants;

import java.io.File;

/** game constants for game objects */
public final class GameStringsConstantsAndUtils {
    /** sun file name */
    public static final String FILE_SUN = "images/sun.png";
    /** splash screen file name */
    public static final String FILE_SPLASH_SCREEN = "images/splash_screen.png";
    /** buildings popup item background */
    public static final String FILE_POPUP_BACKGROUND_ITEM = "images/background_popup_item.png";
    /** description popup background */
    public static final String FILE_DESCRIPTION_POPUP_BACKGROUND = "images/description_popup_background.png";
    /** description popup cross (for closing) */
    public static final String FILE_DESCRIPTION_POPUP_CROSS = "images/cross_close.png";
    /** file name for red planet */
    public static final String FILE_RED_PLANET = "images/planet_1.png";
    /** file name for blue planet */
    public static final String FILE_BLUE_PLANET = "images/planet_2.png";
    /** used like key for sun static object */
    public static final String KEY_SUN = "sun_key";
    /** used like key for red planet static object */
    public static final String KEY_RED_PLANET = "red_planet_key";
    /** used like key for blue planet static object */
    public static final String KEY_BLUE_PLANET = "blue_planet_key";
    /** red team name */
    public static final String SECOND_TEAM_NAME = "second_team";
    /** blue team name */
    public static final String FIRST_TEAM_NAME = "first_team";
    /** key used to identify money font */
    public static final String KEY_FONT_MONEY = "key_money_font";
    /** splash screen key */
    public static final String KEY_SPLASH_SCREEN = "key_splash_screen";
    public static final String sSeparator = File.separator;

    /**
     * private constructor
     */
    private GameStringsConstantsAndUtils() {
    }

    public static String getPathToBuildings(String raceName) {
        String path = "images" + sSeparator + "races" + sSeparator + raceName.toLowerCase() + sSeparator + "buildings" + sSeparator;
        return path;
    }

    public static String getPathToUnits(String raceName) {
        String path = "images" + sSeparator + "races" + sSeparator + raceName.toLowerCase() + sSeparator + "units" + sSeparator;
        return path;
    }

    public static String getPathToSounds(String raceName) {
        String path = "sounds" + sSeparator + "races" + sSeparator + raceName.toLowerCase() + sSeparator;
        return path;
    }

    public static String getPathToBackgroundMusic() {
        String path = "sounds" + sSeparator + "game" + sSeparator;
        return path;
    }
}
