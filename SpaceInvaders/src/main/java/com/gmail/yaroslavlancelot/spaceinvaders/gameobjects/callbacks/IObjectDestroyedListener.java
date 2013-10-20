package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks;

import org.andengine.entity.shape.IAreaShape;

/** used for object lifecycle tracking */
public interface IObjectDestroyedListener {
    public void unitDestroyed(IAreaShape sprite);
}
