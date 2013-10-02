package com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces;

import org.andengine.entity.IEntity;

import java.util.List;

/** Present common functions for unit operation */
public interface EntityOperations {
    public void detachEntity(IEntity entity);
    public void attachEntity(IEntity entity);

    public void attachEntities(List<IEntity> entities);
    public void detachEntities(List<IEntity> entities);
}
