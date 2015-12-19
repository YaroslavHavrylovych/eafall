package com.yaroslavlancelot.eafall.game.entity.gameobject.setlectable;

/**
 * Mark an object as selectable on the scene
 * (e.g. {@link com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene}).
 *
 * @author Yaroslav Havrylovych
 */
public interface Selectable {
    float getX();

    float getY();

    float getWidth();

    float getHeight();
}
