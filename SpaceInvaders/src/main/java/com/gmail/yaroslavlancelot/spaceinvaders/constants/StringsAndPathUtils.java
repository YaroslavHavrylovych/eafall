package com.gmail.yaroslavlancelot.spaceinvaders.constants;

import java.io.File;

/** game constants for game objects */
public final class StringsAndPathUtils {
    public static final String sSeparator = File.separator;
    /** images storing path */
    public static final String GENERAL_IMAGES_PATH = "images" + sSeparator;
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
    /** description popup cross (for closing) */
    public static final String FILE_BUILDINGS_POPUP_UP_BUTTON = "images/up_button.png";
    /** game button */
    public static final String FILE_GAME_BUTTON = "images/game_button.png";
    /** circle point */
    public static final String FILE_CIRCLE_POINT = "images/circle_point.png";
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
    public static final String SECOND_TEAM_CONTROL_BEHAVIOUR_TYPE = "second_team_CONTROL_BEHAVIOUR_TYPE";
    /** blue team alliance */
    public static final String FIRST_TEAM_ALLIANCE = "first_team_alliance";
    /** red team alliance */
    public static final String SECOND_TEAM_ALLIANCE = "second_team_alliance";
    /** blue team name */
    public static final String FIRST_TEAM_CONTROL_BEHAVIOUR_TYPE = "first_team_CONTROL_BEHAVIOUR_TYPE";
    /** key used to identify money font */
    public static final String KEY_FONT_MONEY = "key_money_font";
    /** splash screen key */
    public static final String KEY_SPLASH_SCREEN = "key_splash_screen";

    /**
     * private constructor
     */
    private StringsAndPathUtils() {
    }

    public static String getPathToGeneralImages() {
        return GENERAL_IMAGES_PATH;
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
