package com.gmail.yaroslavlancelot.eafall.game.constant;

import java.io.File;

/**
 * Game constants for game objects.
 * <br/>
 * Note:
 * <br/>
 * <b>Blue team</b> SAME as <i>first team</i> and same as <i>left team</i>
 * <b>Red team</b> SAME as <i>second team</i> and same as <i>right team</i>
 */
public final class StringConstants {
    public static final String SEPARATOR = File.separator;
    /** images storing path */
    public static final String IMAGES_PATH = "graphics/images" + SEPARATOR;
    /** background */
    public static String FILE_BACKGROUND = StringConstants.getImagesPath() + "background.png";
    /** sun file name (top image) */
    public static final String FILE_SUN = "graphics/sprites/sun/sun.png";
    /** sun file name (bottom image) */
    public static final String FILE_SUN_HAZE = "graphics/sprites/sun/sun_haze.png";
    /** splash screen file name */
    public static final String FILE_SPLASH_SCREEN = "graphics/images/splash_screen.png";
    /** bullet image */
    public static final String FILE_BULLET = "graphics/sprites/bullet/bullet.png";
    /** health bar image */
    public static final String FILE_HEALTH_BAR = "graphics/sprites/health_bar.png";
    /** buildings popup item background */
    public static final String FILE_POPUP_BACKGROUND_ITEM = "graphics/images/background_popup_item.png";
    /** description popup background */
    public static final String FILE_DESCRIPTION_POPUP_BACKGROUND = "graphics/images/description_popup_background.png";
    /** description popup cross (for closing) */
    public static final String FILE_BUILDINGS_POPUP_UP_BUTTON = "graphics/icons/up_button.png";
    /** amount value background */
    public static final String FILE_AMOUNT_VALUE_BACKGROUND = "graphics/icons/amount_value_background.png";
    /** game button */
    public static final String FILE_GAME_BUTTON = "graphics/icons/game_button.png";
    /** circle point */
    public static final String FILE_CIRCLE_POINT = "graphics/icons/circle_point.png";
    /** file name for blue planet */
    public static final String FILE_FIRST_PLANET = "graphics/sprites/planet_1.png";
    /** file name for red planet */
    public static final String FILE_SECOND_PLANET = "graphics/sprites/planet_2.png";
    /** used like key for sun static object */
    public static final String KEY_SUN_HAZE = "sun_key_top";
    /** used like key for sun static object */
    public static final String KEY_SUN = "sun_key_bottom";
    /** used like key for red planet static object */
    public static final String KEY_SECOND_PLANET = "red_planet_key";
    /** used like key for blue planet static object */
    public static final String KEY_FIRST_PLANET = "blue_planet_key";
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
    private StringConstants() {
    }

    private static String getPathToAllianceSprites(String allianceName) {
        return "graphics/sprites/alliance/" + allianceName.toLowerCase();
    }

    private static String getPathToAllianceImages(String allianceName) {
        return "graphics/images/alliance/" + allianceName.toLowerCase();
    }

    public static String getImagesPath() {
        return IMAGES_PATH;
    }

    public static String getPathToBuildings(String allianceName) {
        return getPathToAllianceSprites(allianceName) + "/buildings/";
    }

    public static String getPathToUnits(String allianceName) {
        return getPathToAllianceSprites(allianceName) + "/units/";
    }

    public static String getPathToBuildings_Image(String allianceName) {
        return getPathToAllianceImages(allianceName) + "/buildings/";
    }

    public static String getPathToUnits_Image(String allianceName) {
        return getPathToAllianceImages(allianceName) + "/units/";
    }

    public static String getSoundsPath(String allianceName) {
        return "audio/sound/alliance/" + allianceName.toLowerCase() + SEPARATOR;
    }

    public static String getMusicPath() {
        return "audio/music/";
    }
}
