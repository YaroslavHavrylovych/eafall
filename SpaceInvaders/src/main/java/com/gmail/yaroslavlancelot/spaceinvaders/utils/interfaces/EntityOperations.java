package com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;

import java.util.List;

/** Present common functions for unit operation */
public interface EntityOperations {
    public void detachEntity(IEntity entity);
    public void attachEntity(IEntity entity);

    public void attachEntities(List<IEntity> entities);
    public void detachEntities(List<IEntity> entities);
}
