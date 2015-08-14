package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners;

import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite;

/** used for object lifecycle tracking */
public interface IDestroyListener {
    public void objectDestroyed(BodiedSprite gameObject);
}
