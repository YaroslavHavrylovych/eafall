package com.gmail.yaroslavlancelot.eafall.game.constant;

/**
 * contains constants used in measuring game objects size
 *
 * @author Yaroslav Havrylovych
 */
public final class SizeConstants {
    /* general */
    public static final int GAME_FIELD_WIDTH = 1920;
    public static final int HALF_FIELD_WIDTH = GAME_FIELD_WIDTH / 2;
    public static final int GAME_FIELD_HEIGHT = 1080;
    public static final int HALF_FIELD_HEIGHT = GAME_FIELD_HEIGHT / 2;
    public static final int PLANET_DIAMETER = 250;
    public static final int SHIPYARD_SPAWN_LENGTH = 185;
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
    public static final int SELECTOR_IMAGE_SIZE = 150;
    public static final int HEALTH_BAR_CARCASS_WIDTH = 524;
    public static final int HEALTH_BAR_CARCASS_HEIGHT = 49;
    public static final int UNIT_HEALTH_BAR_HEIGHT = 3;
    public static final int UNIT_HEALTH_BAR_FILE_SIZE = 3;
    public static final int FIELD_PARTITION_SIZE = 120;
    public static final int BULLET_MASS_DAMAGE_RADIUS = FIELD_PARTITION_SIZE / 2;

    /* HUD */
    //positions
    public static final int HUD_VALUE_OFFSET = 10;
    public static final int HUD_VALUE_TEXT_OFFSET = 30 + HUD_VALUE_OFFSET;
    public static final int HUD_VALUE_TEXT_FONT_SIZE = 35;
    public static final int HUD_VALUES_Y_PADDING = 60;
    public static final int HUD_VALUES_X_LEFT = 37;
    //7 - max hud text elements amount
    public static final int HUD_VALUES_X_RIGHT =
            GAME_FIELD_WIDTH - HUD_VALUES_X_LEFT - HUD_VALUE_OFFSET - 150;
    //sizes
    public static final int MENU_BUTTON_WIDTH = 137;
    public static final int MENU_BUTTON_HEIGHT = 49;
    public static final int MONEY_FONT_SIZE = 35;
    public static final int HUD_OXYGEN = 36;
    public static final int ICON_OXYGEN = 70;
    public static final int HUD_ENERGY = 42;
    public static final int HUD_CLOCK = 42;

    /* constructions popup */
    public static final int CONSTRUCTIONS_POPUP_INVOCATION_BUTTON_WIDTH = MENU_BUTTON_WIDTH;
    public static final int CONSTRUCTIONS_POPUP_INVOCATION_BUTTON_HEIGHT = MENU_BUTTON_HEIGHT;
    public static final int CONSTRUCTIONS_POPUP_ELEMENT_HEIGHT = 123;
    public static final int CONSTRUCTIONS_POPUP_ELEMENT_WIDTH = 623;
    public static final int CONSTRUCTIONS_POPUP_ELEMENT_NAME_X = 157;
    public static final int CONSTRUCTIONS_POPUP_ELEMENT_IMAGE_X = 81;
    public static final int CONSTRUCTIONS_POPUP_ELEMENT_OXYGEN_IMAGE_X = 487;
    public static final int CONSTRUCTIONS_POPUP_ELEMENT_OXYGEN_IMAGE_Y =
            CONSTRUCTIONS_POPUP_ELEMENT_HEIGHT / 2 + 5;
    public static final int CONSTRUCTIONS_POPUP_ELEMENT_OXYGEN_VALUE_X = 527;
    public static final int CONSTRUCTIONS_POPUP_ELEMENT_OXYGEN_OFFSET = 10;
    public static final int CONSTRUCTIONS_POPUP_ELEMENT_IMAGE_Y = 59;
    public static final int CONSTRUCTIONS_POPUP_ELEMENT_IMAGE_SIZE = 103;
    public static final int CONSTRUCTIONS_POPUP_ROWS = 6;
    public static final int CONSTRUCTIONS_POPUP_FIRST_ROW_Y = 165;
    public static final int CONSTRUCTIONS_POPUP_FIRST_COLUMN_X = 123;
    public static final int CONSTRUCTIONS_POPUP_SECOND_COLUMN_X = 766;
    public static final int CONSTRUCTIONS_POPUP_WIDTH = 1512;
    public static final int CONSTRUCTIONS_POPUP_HEIGHT = 950;

    /* menu popup */
    public static final int MENU_POPUP_WIDTH = 840;
    public static final int MENU_POPUP_HEIGHT = 649;
    public static final int MENU_POPUP_BUTTON_WIDTH = 585;
    public static final int MENU_POPUP_BUTTON_HEIGHT = 250;
    public static final int MENU_POPUP_FIRST_BUTTON_Y = 30;

    /* description popup */
    // general
    public static final int DESCRIPTION_POPUP_HEIGHT = 620;
    public static final int DESCRIPTION_POPUP_WIDTH = SizeConstants.GAME_FIELD_WIDTH;
    public static final int DESCRIPTION_POPUP_TEXT_SIZE = 45;
    public static final int DESCRIPTION_POPUP_TEXT_VERTICAL_PADDING = 3; //between rows in description (text only)
    public static final int DESCRIPTION_POPUP_HEADER_SINGLE_ROW_Y = 530;
    public static final int DESCRIPTION_POPUP_HEADER_FONT_SIZE = 45;
    public static final int DESCRIPTION_POPUP_HEADER_TEXT_X = DESCRIPTION_POPUP_WIDTH / 2;
    public static final int DESCRIPTION_POPUP_ARROW_WIDTH = 55;
    public static final int DESCRIPTION_POPUP_ARROW_HEIGHT = 55;
    public static final int DESCRIPTION_POPUP_LEFT_ARROW_X = 747;
    public static final int DESCRIPTION_POPUP_RIGHT_ARROW_X = 1190;
    public static final int DESCRIPTION_POPUP_ARROW_Y = 546;
    //image
    public static final int DESCRIPTION_POPUP_IMAGE_HEIGHT = 400;
    public static final int DESCRIPTION_POPUP_IMAGE_WIDTH = 400;
    public static final int DESCRIPTION_POPUP_IMAGE_X = 388;
    public static final int DESCRIPTION_POPUP_IMAGE_Y = 301;
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
    public static final int DESCRIPTION_POPUP_ADDITIONAL_AREA_HEIGHT = 245;
    public static final int DESCRIPTION_POPUP_ADDITIONAL_AREA_WIDTH = 270;
    public static final int DESCRIPTION_POPUP_ADDITIONAL_AREA_FILE_WIDTH = 379;
    public static final int DESCRIPTION_POPUP_ADDITIONAL_AREA_FILE_HEIGHT = 357;
    public static final int DESCRIPTION_POPUP_ADDITIONAL_BACKGROUND_WIDTH = 386;
    public static final int DESCRIPTION_POPUP_ADDITIONAL_BACKGROUND_HEIGHT = 365;
    public static final int DESCRIPTION_POPUP_ADDITIONAL_AREA_X = 1585;
    public static final int DESCRIPTION_POPUP_ADDITIONAL_AREA_Y = 240;

    /* campaign */
    public static final int CAMPAIGN_START_BUTTON_WIDTH = 200;
    public static final int CAMPAIGN_START_BUTTON_HEIGHT = 75;
    public static final int CAMPAIGN_START_BUTTON_X = GAME_FIELD_WIDTH -
            CAMPAIGN_START_BUTTON_WIDTH / 2;
    public static final int CAMPAIGN_START_BUTTON_Y = CAMPAIGN_START_BUTTON_HEIGHT / 2;


    private SizeConstants() {
    }
}
