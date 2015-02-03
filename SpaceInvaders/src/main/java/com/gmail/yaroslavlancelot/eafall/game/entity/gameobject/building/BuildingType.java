package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building;

/**
 * Define existing building types
 */
public enum BuildingType {
    /** building which produces creeps */
    CREEP_BUILDING,
    /** buildings which produce money amount */
    WEALTH_BUILDING,
    /** unique for each race buildings */
    SPECIAL_BUILDING,
    /** defence (producing orbital stations) building */
    DEFENCE_BUILDING
}
