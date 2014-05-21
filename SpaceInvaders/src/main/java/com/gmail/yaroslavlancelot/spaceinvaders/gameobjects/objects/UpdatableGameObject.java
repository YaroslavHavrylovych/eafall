package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects;

/** used to mark that current object can be updated */
public interface UpdatableGameObject<T extends GameObject> {
    /** return next version of the current object */
    T getUpdatedGameObject();
}
