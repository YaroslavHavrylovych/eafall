package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;

/** used for object lifecycle tracking */
public interface IObjectDestroyedListener {
    public void objectDestroyed(GameObject gameObject);
}
