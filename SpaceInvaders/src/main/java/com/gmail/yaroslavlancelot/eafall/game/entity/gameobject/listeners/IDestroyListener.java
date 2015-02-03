package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners;

import com.gmail.yaroslavlancelot.eafall.game.entity.RectangleWithBody;

/** used for object lifecycle tracking */
public interface IDestroyListener {
    public void objectDestroyed(RectangleWithBody gameObject);
}
