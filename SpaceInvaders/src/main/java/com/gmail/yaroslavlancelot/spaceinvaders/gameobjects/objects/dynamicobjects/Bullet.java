package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.util.List;

public class Bullet extends Rectangle {
    public static final int BULLET_SIZE = 3;
    /** for detaching entity from scene */
    private final EntityOperations mEntityOperations;
    /** update time for current object */
    protected float mUpdateCycleTime = .05f;

    public Bullet(VertexBufferObjectManager vertexBufferObjectManager, EntityOperations entityOperations, Color color) {
        super(0, 0, BULLET_SIZE, BULLET_SIZE, vertexBufferObjectManager);
        setColor(color);
        mEntityOperations = entityOperations;
    }

    public void fire(float x, float y, float goalX, float goalY, final ISimpleUnitEnemiesUpdater enemiesUpdater,
                     final Damage damage) {
        setX(x);
        setY(y);
        final PhysicsHandler physicsHandler = new PhysicsHandler(this);
        registerUpdateHandler(physicsHandler);
        physicsHandler.setVelocity((goalX - x) * 2, (goalY - y) * 2);
        registerUpdateHandler(new TimerHandler(mUpdateCycleTime, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                if (isOutOfBounds()) {
                    mEntityOperations.detachEntity(Bullet.this);
                    unregisterUpdateHandler(physicsHandler);
                    return;
                }

                if (isIfBulletColliedThenHit(enemiesUpdater.getMainTarget(), damage))
                    return;

                List<GameObject> objects = enemiesUpdater.getEnemiesObjects();
                if (objects != null && !objects.isEmpty()) {
                    for (GameObject object : objects) {
                        if (isIfBulletColliedThenHit(object, damage))
                            return;
                    }
                    return;
                }
            }
        }));
    }

    protected boolean isOutOfBounds() {
        return getX() < 0 || getY() < 0 || getX() > SizeConstants.GAME_FIELD_WIDTH || getY() > SizeConstants.GAME_FIELD_HEIGHT;
    }

    private boolean isIfBulletColliedThenHit(GameObject object, Damage damage) {
        if (object == null) return false;
        if (object.collidesWith(this)) {
            object.damageObject(damage);
            mEntityOperations.detachEntity(this);
            return true;
        }
        return false;
    }
}
