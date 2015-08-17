package com.gmail.yaroslavlancelot.eafall.game.entity.health;

import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;

/**
 * @author Yaroslav Havrylovych
 */
public interface IHealthBar {
    /**
     * @return health bar visibility
     */
    boolean isVisible();

    /**
     * sets health bar visibility
     */
    void setVisible(boolean visible);

    /**
     * add health bar as a child
     *
     * @param parent entity health bar will be added
     */
    void addToParent(BatchedSprite parent);

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

}
