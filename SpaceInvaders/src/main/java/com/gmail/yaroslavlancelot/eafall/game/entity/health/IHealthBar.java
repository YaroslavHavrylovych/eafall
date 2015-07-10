package com.gmail.yaroslavlancelot.eafall.game.entity.health;

import org.andengine.entity.IEntity;

/**
 * @author Yaroslav Havrylovych
 */
public interface IHealthBar {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * attaches health bar to parent
     *
     * @param parent entity to attach health bar to
     */
    void attachHealthBar(IEntity parent);

    /**
     * sets health bar position
     *
     * @param x health bar bottom left abscissa new position
     * @param y health bar bottom left ordinate new position
     */
    void setPosition(float x, float y);

    /**
     * changes health bar accordingly to health changes
     *
     * @param healthMax    maximum health bar value
     * @param actualHealth actual health bar value
     */
    void redrawHealthBar(int healthMax, int actualHealth);

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
