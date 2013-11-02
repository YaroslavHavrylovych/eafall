package com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** Present common functions for unit operation */
public interface EntityOperations {
    public void detachEntity(IEntity entity);

    public void attachEntity(IEntity entity);

    public void attachEntityWithTouchArea(IAreaShape entity);

    public void detachEntityWithTouch(IAreaShape entity);

    public void detachPhysicsBody(final GameObject gameObject);

    public VertexBufferObjectManager getObjectManager();

    public Unit createUnitForTeam(int unitKey, final ITeam unitTeam);
}
