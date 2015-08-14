package com.gmail.yaroslavlancelot.eafall.game.constant;

import java.io.File;

/**
 * Game constants for game objects.
 * <br/>
 * Note:
 * <br/>
 * <b>Blue player</b> SAME as <i>first player</i> and same as <i>left player</i>
 * <b>Red player</b> SAME as <i>second player</i> and same as <i>right player</i>
 *
 * @author Yaroslav Havrylovych
 */
public final class StringConstants {
    public static final String SEPARATOR = File.separator;
    /** images storing path */
    public static final String IMAGES_PATH = "graphics/images" + SEPARATOR;
    /** sun file name (top image) */
    public static final String FILE_SUN = "graphics/sprites/sun/sun.png";
    /** sun file name (bottom image) */
    public static final String FILE_SUN_HAZE = "graphics/sprites/sun/sun_haze.png";
    /** splash screen file name */
    public static final String FILE_SPLASH_SCREEN = "graphics/images/splash_screen.png";
    /** bullet image */
    public static final String FILE_BULLET = "graphics/sprites/bullet/bullet.png";
    /** buildings popup item background */
    public static final String FILE_CONSTRUCTIONS_POPUP_ITEM = "graphics/images/constructions_popup_item.png";
    /** construction popup */
    public static final String FILE_CONSTRUCTIONS_POPUP = "graphics/images/constructions_popup.png";
    /** description popup background */
    public static final String FILE_DESCRIPTION_POPUP_BACKGROUND = "graphics/images/description_popup_background.png";
    /** health bar for display player health */
    public static final String FILE_HEALTH_BAR_PLAYER = "graphics/sprites/health/health";
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
    /** red player name */
    public static final String SECOND_PLAYER_CONTROL_BEHAVIOUR_TYPE = "second_player_CONTROL_BEHAVIOUR_TYPE";
    /** blue player alliance */
    public static final String FIRST_PLAYER_ALLIANCE = "first_player_alliance";
    /** red player alliance */
    public static final String SECOND_PLAYER_ALLIANCE = "second_player_alliance";
    /** blue player name */
    public static final String FIRST_PLAYER_CONTROL_BEHAVIOUR_TYPE = "first_player_CONTROL_BEHAVIOUR_TYPE";
    /** splash screen key */
    public static final String KEY_SPLASH_SCREEN = "key_splash_screen";
    /** description popup cross (for closing) */
    public static final String FILE_BUILDINGS_POPUP_UP_BUTTON = "graphics/icons/up_button.png";
    /** game menu button */
    public static final String FILE_MENU_BUTTON = "graphics/icons/menu_button.png";
    /** amount value background */
    public static final String FILE_AMOUNT_VALUE_BACKGROUND = "graphics/icons/amount_value_background.png";
    /** game button */
    public static final String FILE_GAME_BUTTON = "graphics/icons/game_button.png";
    /** circle point */
    public static final String FILE_CIRCLE_POINT = "graphics/icons/circle_point.png";
    /** hud energy icon */
    public static final String FILE_SHUTTLE_HUD = "graphics/icons/shuttle_hud.png";
    /** oxygen icon for popup */
    public static final String FILE_OXYGEN = "graphics/icons/oxygen.png";
    /** hud oxygen icon */
    public static final String FILE_OXYGEN_HUD = "graphics/icons/oxygen_hud.png";
    /** hud clock icon */
    public static final String FILE_CLOCK_HUD = "graphics/icons/clock_hud.png";
    /** health bar carcass */
    public static final String FILE_HEALTH_BAR_CARCASS = "graphics/icons/health_bar_carcass.png";
    /** background */
    public static String FILE_BACKGROUND = StringConstants.getImagesPath() + "background.png";

    /**
     * private constructor
     */
    private StringConstants() {
    }

    public static String getImagesPath() {
        return IMAGES_PATH;
    }

    public static String getMusicPath() {
        return "audio/music/";
    }

    private static String getPathToAllianceSprites(String allianceName) {
        return "graphics/sprites/alliance/" + allianceName.toLowerCase();
    }

    private static String getPathToAllianceImages(String allianceName) {
        return "graphics/images/alliance/" + allianceName.toLowerCase();
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
}
