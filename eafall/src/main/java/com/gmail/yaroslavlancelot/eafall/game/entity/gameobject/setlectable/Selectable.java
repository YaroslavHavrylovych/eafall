package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.setlectable;

/**
 * Mark an object as selectable on the scene
 * (e.g. {@link com.gmail.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene}).
 *
 * @author Yaroslav Havrylovych
 */
public interface Selectable {
    float getX();

    float getY();

    float getWidth();

    float getHeight();
}
