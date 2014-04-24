package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.IGameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitPathUtil;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.util.concurrent.atomic.AtomicBoolean;

public class Bullet extends IGameObject {
    public static final int BULLET_SIZE = 3;
    private static final BodyDef.BodyType sBulletBodyType = BodyDef.BodyType.DynamicBody;
    private static final float sMaxVelocity = 2.0f;
    private final FixtureDef mBulletFixtureDef;
    /** for detaching entity from scene */
    private final EntityOperations mEntityOperations;
    private final AtomicBoolean mIsObjectAlive;
    /** update time for current object */
    protected float mUpdateCycleTime = .5f;
    private float mMaxBulletFlyDistance;
    private float mBulletStartX, mBulletStartY;
    private Damage mDamage;

    public Bullet(VertexBufferObjectManager vertexBufferObjectManager, EntityOperations entityOperations,
                  Color color, Damage damage, FixtureDef fixtureDef) {
        super(0, 0, BULLET_SIZE, BULLET_SIZE, vertexBufferObjectManager);
        setColor(color);
        mEntityOperations = entityOperations;
        mIsObjectAlive = new AtomicBoolean(true);
        mDamage = damage;
        mBulletFixtureDef = fixtureDef;
    }

    public boolean getAndSetFalseIsObjectAlive() {
        return mIsObjectAlive.getAndSet(false);
    }

    public void fireFromPosition(float x, float y, IGameObject gameObject) {
        if (gameObject.getBody() == null) return;
        Vector2 target = gameObject.getBody().getPosition();
        float goalX = target.x, goalY = target.y;
        mBulletStartX = x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        mBulletStartY = y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        mPhysicBody = mEntityOperations.registerCircleBody(this, sBulletBodyType, mBulletFixtureDef, x, y, 0);
        mPhysicBody.setBullet(true);
        mMaxBulletFlyDistance = UnitPathUtil.getDistanceBetweenPoints(x, y, goalX, goalY) * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

        float distanceX = goalX - x,
                distanceY = goalY - y;
        float absDistanceX = Math.abs(distanceX),
                absDistanceY = Math.abs(distanceY),
                maxAbsDistance = absDistanceX > absDistanceY ? absDistanceX : absDistanceY;
        float ordinateSpeed = sMaxVelocity * distanceX / maxAbsDistance,
                abscissaSpeed = sMaxVelocity * distanceY / maxAbsDistance;

        TimerHandler timerUpdateHandler = new TimerHandler(mUpdateCycleTime, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                synchronized (mIsObjectAlive) {
                    if (!mIsObjectAlive.get()) {
                        unregisterUpdateHandler(pTimerHandler);
                        return;
                    }
                    if (isBulletOutOfRange()) {
                        mIsObjectAlive.set(false);
                        unregisterUpdateHandler(pTimerHandler);
                        mEntityOperations.detachEntity(Bullet.this);
                    }
                }
            }
        });

        registerUpdateHandler(timerUpdateHandler);

        mPhysicBody.setLinearVelocity(ordinateSpeed, abscissaSpeed);
    }

    public Damage getDamage() {
        return mDamage;
    }

    protected boolean isBulletOutOfRange() {
        return getX() < 0 || getY() < 0 || getX() > SizeConstants.GAME_FIELD_WIDTH || getY() > SizeConstants.GAME_FIELD_HEIGHT ||
                UnitPathUtil.getDistanceBetweenPoints(getX(), getY(), mBulletStartX, mBulletStartY) > mMaxBulletFlyDistance;
    }
}
