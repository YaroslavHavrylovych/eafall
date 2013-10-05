package com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces;

import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IAreaShape;

/** Present common functions for unit operation */
public interface EntityOperations {
    public void detachEntity(IEntity entity);

    public void attachEntity(IEntity entity);

    public void attachEntityWithTouchArea(IAreaShape entity);

    public void detachEntityWithTouch(IAreaShape entity);
}
