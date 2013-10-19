package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects;

import org.andengine.entity.shape.IAreaShape;

/** used for object lifecycle tracking */
public interface IObjectDestroyedListener {
    public void unitDestroyed(IAreaShape sprite);
}
