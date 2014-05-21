package com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.RectangleWithBody;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** Present common functions for unit operation */
public interface EntityOperations {
    void detachEntity(IAreaShape entity);

    void attachEntity(IEntity entity);

    void attachEntityWithTouchArea(IAreaShape entity);

    void attachEntityWithTouchToHud(IAreaShape entity);

    void detachEntityFromHud(IAreaShape entity);

    /** only if you're server. Clients doesn't have physic world
     * @param gameObject*/
    void detachPhysicsBody(final RectangleWithBody gameObject);

    VertexBufferObjectManager getObjectManager();

    Unit createThickUnit(int unitKey, final ITeam unitTeam);

    Body registerCircleBody(final RectangleWithBody gameObject, final BodyDef.BodyType pBodyType, final FixtureDef pFixtureDef, float... coordinates);
}
