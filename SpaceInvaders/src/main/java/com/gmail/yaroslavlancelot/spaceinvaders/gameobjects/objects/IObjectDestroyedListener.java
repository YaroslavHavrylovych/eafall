package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects;

import org.andengine.entity.sprite.Sprite;

/** used for object lifecycle tracking */
public interface IObjectDestroyedListener {
    public void unitDestroyed(Sprite sprite);
}
