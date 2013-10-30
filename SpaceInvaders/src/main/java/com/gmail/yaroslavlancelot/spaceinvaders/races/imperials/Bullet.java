package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.ISimpleUnitEnemiesUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.util.List;

public class Bullet extends Rectangle {
    public static final int BULLET_SIZE = 3;
    /** */
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
        PhysicsHandler physicsHandler = new PhysicsHandler(this);
        registerUpdateHandler(physicsHandler);
        physicsHandler.setVelocity(goalX - x, goalY - y);
        registerUpdateHandler(new TimerHandler(mUpdateCycleTime, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                if (isOutOfBounds()) {
                    mEntityOperations.detachEntity(Bullet.this);
                    return;
                }

                List<Unit> units = enemiesUpdater.getEnemies();
                if (units != null && !units.isEmpty()) {
                    for (Unit unit : units) {
                        if (unit.collidesWith(Bullet.this)) {
                            unit.hitUnit(damage);
                            mEntityOperations.detachEntity(Bullet.this);
                            return;
                        }
                    }
                    return;
                }
            }
        }));
    }

    private boolean isOutOfBounds() {
        return getX() < 0 || getY() < 0 || getX() > SizeConstants.GAME_FIELD_WIDTH || getY() > SizeConstants.GAME_FIELD_HEIGHT;
    }
}
