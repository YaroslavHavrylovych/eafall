package com.gmail.yaroslavlancelot.spaceinvaders.objects.callbacks;

import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.RectangleWithBody;

/** used for object lifecycle tracking */
public interface IObjectDestroyedListener {
    public void objectDestroyed(RectangleWithBody gameObject);
}
