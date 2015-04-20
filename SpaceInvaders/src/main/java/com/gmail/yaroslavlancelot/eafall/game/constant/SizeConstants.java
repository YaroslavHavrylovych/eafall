package com.gmail.yaroslavlancelot.eafall.game.constant;

/**
 * contains constants used in measuring game objects size
 */
public final class SizeConstants {
    /* general */
    public static final int GAME_FIELD_WIDTH = 1920;
    public static final int HALF_FIELD_WIDTH = GAME_FIELD_WIDTH / 2;
    public static final int GAME_FIELD_HEIGHT = 1080;
    public static final int HALF_FIELD_HEIGHT = GAME_FIELD_HEIGHT / 2;
    public static final int PLANET_DIAMETER = 128;
    public static final int ADDITION_MARGIN_FOR_PLANET = 20;
    public static final int SUN_DIAMETER = 450;
    public static final int FILE_SUN_DIAMETER = 224;
    public static final int BUILDING_SIZE = 16;
    public static final int BUILDING_BIG_IMAGE_SIZE = 16;
    public static final int UNIT_SIZE = 16;
    public static final int UNIT_FILE_SIZE = 36;
    public static final int UNIT_BIG_IMAGE_SIZE = 400;
    public static final int BETWEEN_TEXTURES_PADDING = 3;
    public static final int HEALTH_BAR_HEIGHT = 3;
    public static final int TEAM_COLOR_AREA_SIZE = 4;
    public static final int BULLET_SIZE = 2;

    /*
     * each unit should to have some color, which will be same as color of his team.
     * this color will be inside of unit sprite and will have size, as this constant value
     */
    public static final int UNIT_TEAM_COLOR_INNER_SPRITE_SIZE = 11;

    /* money */
    public static final int MONEY_FONT_SIZE = 35;

    /* building popup */
    public static final int BUILDING_POPUP_INVOCATION_BUTTON_SIZE = 100;
    public static final int BUILDING_POPUP_ELEMENT_HEIGHT = 60;
    public static final int BUILDING_POPUP_IMAGE_PADDING = 10;
    public static final int BUILDING_POPUP_AFTER_TEXT_PADDING = 10;
    public static final int BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT = BUILDING_POPUP_ELEMENT_HEIGHT + 2 * BUILDING_POPUP_IMAGE_PADDING;
    public static final int BUILDING_POPUP_BACKGROUND_ITEM_WIDTH = 600;


    /** description popup */
    // general
    public static final int DESCRIPTION_POPUP_HEIGHT = HALF_FIELD_HEIGHT;
    public static final int DESCRIPTION_POPUP_WIDTH = SizeConstants.GAME_FIELD_WIDTH;
    public static final int DESCRIPTION_POPUP_PADDING = 70;
    public static final int DESCRIPTION_POPUP_TEXT_SIZE = 40;
    public static final int DESCRIPTION_POPUP_TITLE_SIZE = 70;
    // description area
    public static final int DESCRIPTION_POPUP_DESCRIPTION_AREA_HEIGHT =
            SizeConstants.DESCRIPTION_POPUP_HEIGHT - 2 * DESCRIPTION_POPUP_PADDING - DESCRIPTION_POPUP_TITLE_SIZE;
    // amount text
    public static final int DESCRIPTION_POPUP_AMOUNT_FONT_SIZE = 90;
    public static final int DESCRIPTION_POPUP_AMOUNT_TEXT_PADDING_VERTICAL = 5;
    public static final int DESCRIPTION_POPUP_AMOUNT_TEXT_PADDING_HORIZONTAL = 15;
    // addition area
    public static final int DESCRIPTION_POPUP_ADDITIONAL_AREA_HEIGHT = 250;
    public static final int DESCRIPTION_POPUP_ADDITIONAL_AREA_WIDTH = DESCRIPTION_POPUP_ADDITIONAL_AREA_HEIGHT;


    private SizeConstants() {
    }
}