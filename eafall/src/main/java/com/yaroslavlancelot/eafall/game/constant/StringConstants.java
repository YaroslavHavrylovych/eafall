package com.yaroslavlancelot.eafall.game.constant;

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
    /** annihilator bullet image */
    public static final String FILE_ANNIHILATOR_BULLET = "graphics/sprites/bullet/annihilator.png";
    /** higgson bullet image */
    public static final String FILE_HIGGSON_BULLET = "graphics/sprites/bullet/higgson.png";
    /** laser bullet image */
    public static final String FILE_LASER_BULLET = "graphics/sprites/bullet/laser.png";
    /** neutrino bullet image */
    public static final String FILE_NEUTRINO_BULLET = "graphics/sprites/bullet/neutrino.png";
    /** quaker bullet image */
    public static final String FILE_QUAKER_BULLET = "graphics/sprites/bullet/quaker.png";
    /** railgun bullet image */
    public static final String FILE_RAILGUN_BULLET = "graphics/sprites/bullet/railgun.png";
    /** buildings popup item background */
    public static final String FILE_CONSTRUCTIONS_POPUP_ITEM = "graphics/images/constructions_popup_item.png";
    /** construction popup */
    public static final String FILE_CONSTRUCTIONS_POPUP = "graphics/images/constructions_popup.png";
    /** description popup background */
    public static final String FILE_DESCRIPTION_POPUP_BACKGROUND = "graphics/images/description_popup_background.png";
    /** description popup background */
    public static final String FILE_DESCRIPTION_LEFT_BLOCK = "graphics/images/description_popup_left_block.png";
    /** description popup arrows */
    public static final String FILE_DESCRIPTION_ARROWS = "graphics/icons/popup_description_arrow.png";
    /** menu popup background */
    public static final String FILE_MENU_POPUP_BACKGROUND = "graphics/images/menu_popup_background.png";
    /** menu popup button */
    public static final String FILE_MENU_POPUP_BUTTON = "graphics/images/menu_popup_button.png";
    /** health bar for display player health */
    public static final String FILE_HEALTH_BAR_PLAYER = "graphics/sprites/health/health";
    /** unit health bar */
    public static final String FILE_HEALTH_BAR_UNIT = "graphics/sprites/health/unit_health_bar.png";
    /** file name for blue planet */
    public static final String PLANET = "graphics/sprites/earth.png";
    /** campaign foreground */
    public static final String FILE_CAMPAIGN_HUD_FOREGROUND = "graphics/images/campaign/foreground.png";
    /** selector image */
    public static final String FILE_SELECTOR = "graphics/sprites/select/selector.png";
    /** planet explosion animation */
    public static final String KEY_PLANET_EXPLOSION = "graphics/sprites/explosion/explosion_planet.png";
    /** unit explosion animation */
    public static final String KEY_UNIT_EXPLOSION = "graphics/sprites/explosion/explosion_unit.png";
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
    /** income image */
    public static final String FILE_INCOME = "graphics/icons/income.png";
    /** tutorial pointer image */
    public static final String FILE_TUTORIAL_POINTER = "graphics/icons/pointer.png";
    /** game menu button */
    public static final String FILE_MENU_BUTTON = "graphics/icons/menu_button.png";
    /** amount value background */
    public static final String FILE_AMOUNT_VALUE_BACKGROUND = "graphics/icons/amount_value_background.png";
    /** game button */
    public static final String FILE_GAME_BUTTON = "graphics/icons/game_button.png";
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
    /** path to the shot sounds */
    public static String SOUND_SHOT_PATH = "audio/sound/shot/";
    /** clock tick sound path */
    public static String SOUND_CLOCK_TICK_PATH = "audio/sound/clock_tick.ogg";

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
}
