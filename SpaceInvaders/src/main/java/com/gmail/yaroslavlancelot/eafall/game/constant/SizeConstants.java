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
    public static final int PLANET_DIAMETER = 250;
    public static final int FILE_PLANET_DIAMETER = 250;
    public static final int ADDITION_MARGIN_FOR_PLANET = 20;
    public static final int SUN_DIAMETER = 500;
    public static final int FILE_SUN_DIAMETER = 450;
    public static final int BUILDING_SIZE = 100;
    public static final int BUILDING_IMAGE_SIZE = 100;
    public static final int BUILDING_BIG_IMAGE_SIZE = 400;
    public static final int UNIT_SIZE = 16;
    public static final int UNIT_FILE_SIZE = 36;
    public static final int UNIT_BIG_IMAGE_SIZE = 400;
    public static final int BETWEEN_TEXTURES_PADDING = 3;
    public static final int HEALTH_BAR_HEIGHT = 3;
    public static final int TEAM_COLOR_AREA_SIZE = 4;
    public static final int BULLET_SIZE = 2;
    public static final int SELECTOR_IMAGE_SIZE = 150;

    /* money */
    public static final int MONEY_FONT_SIZE = 35;

    /* building popup */
    public static final int BUILDING_POPUP_INVOCATION_BUTTON_SIZE = 100;
    public static final int BUILDING_POPUP_ELEMENT_HEIGHT = 60;
    public static final int BUILDING_POPUP_IMAGE_PADDING = 10;
    public static final int BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT = BUILDING_POPUP_ELEMENT_HEIGHT +
            2 * BUILDING_POPUP_IMAGE_PADDING;
    public static final int BUILDING_POPUP_BACKGROUND_ITEM_WIDTH = 600;


    /** description popup */
    // general
    public static final int DESCRIPTION_POPUP_HEIGHT = 666;
    public static final int DESCRIPTION_POPUP_WIDTH = SizeConstants.GAME_FIELD_WIDTH;
    public static final int DESCRIPTION_POPUP_TEXT_SIZE = 45;
    public static final int DESCRIPTION_POPUP_TEXT_VERTICAL_PADDING = 3; //between rows in description (text only)
    public static final int DESCRIPTION_POPUP_HEADER_SINGLE_ROW_Y = 530;
    public static final int DESCRIPTION_POPUP_HEADER_FONT_SIZE = 45;
    public static final int DESCRIPTION_POPUP_HEADER_TEXT_X = DESCRIPTION_POPUP_WIDTH / 2;
    //image
    public static final int DESCRIPTION_POPUP_IMAGE_HEIGHT = 400;
    public static final int DESCRIPTION_POPUP_IMAGE_WIDTH = 400;
    public static final int DESCRIPTION_POPUP_IMAGE_X = 395;
    public static final int DESCRIPTION_POPUP_IMAGE_Y = 295;
    public static final int DESCRIPTION_POPUP_IMAGE_PADDING = 30;
    // description area
    public static final int DESCRIPTION_POPUP_DES_AREA_HEIGHT = 335;
    public static final int DESCRIPTION_POPUP_DES_AREA_WIDTH = 750;
    public static final int DESCRIPTION_POPUP_DES_AREA_X = 1015;
    public static final int DESCRIPTION_POPUP_DES_AREA_Y = 258;
    public static final int DESCRIPTION_POPUP_DES_BUTTON_HEIGHT = 120;
    // amount text
    public static final int DESCRIPTION_POPUP_AMOUNT_FONT_SIZE = 75;
    public static final int DESCRIPTION_POPUP_AMOUNT_TEXT_PADDING_HORIZONTAL = 15;
    public static final int DESCRIPTION_POPUP_AMOUNT_BACKGROUND_X_PADDING = 4;
    // addition area
    public static final int DESCRIPTION_POPUP_ADDITIONAL_AREA_HEIGHT = 245; //285 background size
    public static final int DESCRIPTION_POPUP_ADDITIONAL_AREA_WIDTH = 270; //310 background size
    public static final int DESCRIPTION_POPUP_ADDITIONAL_AREA_X = 1585;
    public static final int DESCRIPTION_POPUP_ADDITIONAL_AREA_Y = 232;


    private SizeConstants() {
    }
}
