package com.gmail.yaroslavlancelot.spaceinvaders.constants;

/**
 * contains constants used in measuring game objects size
 */
public final class SizeConstants {
    public static final int GAME_FIELD_WIDTH = 1920;
    public static final int GAME_FIELD_HEIGHT = 1080;
    public static final int PLANET_DIAMETER = 128;
    public static final int ADDITION_MARGIN_FOR_PLANET = 20;
    public static final int SUN_DIAMETER = 450;
    public static final int FILE_SUN_DIAMETER = 224;
    public static final int BUILDING_DIAMETER = 16;
    public static final int UNIT_SIZE = 16;
    /**
     * each unit should to have some color, which will be same as color of his team.
     * this color will be inside of unit sprite and will have size, as this constant value
     */
    public static final int UNIT_TEAM_COLOR_INNER_SPRITE_SIZE = 11;
    public static final int MONEY_FONT_SIZE = 35;
    public static final int BUILDING_POPUP_ELEMENT_HEIGHT = 60;


    private SizeConstants() {
    }
}
