package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame.MainOperationsBaseGameActivity;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.IGameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitPathUtil;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;

import org.andengine.engine.handler.collision.CollisionHandler;
import org.andengine.engine.handler.collision.ICollisionCallback;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.shape.IShape;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.util.concurrent.atomic.AtomicBoolean;

public class Bullet extends IGameObject {
    private static FixtureDef sBulletFixtureDef = PhysicsFactory.createFixtureDef(0f, 0f, 0f);
    public static final int BULLET_SIZE = 3;
    private static final BodyDef.BodyType sBulletBodyType = BodyDef.BodyType.KinematicBody;
    private static final float sMaxVelocity = 2.0f;
    /** for detaching entity from scene */
    private final EntityOperations mEntityOperations;
    private final boolean mIsFakeObject;
    private final AtomicBoolean mIsObjectAlive;
    /** update time for current object */
    protected float mUpdateCycleTime = .5f;
    private volatile TimerHandler mTimerUpdateHandler;
    private volatile CollisionHandler mCollisionUpdateHandler;
    private float mMaxBulletFlyDistance;
    private float mBulletStartX, mBulletStartY;

    public Bullet(VertexBufferObjectManager vertexBufferObjectManager, EntityOperations entityOperations, Color color, boolean isFakeObject) {
        super(0, 0, BULLET_SIZE, BULLET_SIZE, vertexBufferObjectManager);
        setColor(color);
        mEntityOperations = entityOperations;
        mIsFakeObject = isFakeObject;
        mIsObjectAlive = new AtomicBoolean(true);
    }

    public void fire(float x, float y, float goalX, float goalY, final ISimpleUnitEnemiesUpdater enemiesUpdater,
                     final Damage damage) {
        mPhysicBody = mEntityOperations.registerCircleBody(this, sBulletBodyType, sBulletFixtureDef);
        mPhysicBody.setTransform(mBulletStartX = x, mBulletStartY = y, 0);
        mMaxBulletFlyDistance = UnitPathUtil.getDistanceBetweenPoints(x, y, goalX, goalY);

        float distanceX = goalX - x,
                distanceY = goalY - y;
        float absDistanceX = Math.abs(distanceX),
                absDistanceY = Math.abs(distanceY),
                maxAbsDistance = absDistanceX > absDistanceY ? absDistanceX : absDistanceY;
        float ordinateSpeed = sMaxVelocity * distanceX / maxAbsDistance,
                abscissaSpeed = sMaxVelocity * distanceY / maxAbsDistance;

        mCollisionUpdateHandler = new CollisionHandler(new TargetCollisionHandler(damage), this, enemiesUpdater.getMainTarget());
        mTimerUpdateHandler = new TimerHandler(mUpdateCycleTime, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                synchronized (mIsObjectAlive) {
                    if (!mIsObjectAlive.get()) return;
                    if (isBulletOutOfRange()) {
                        unregisterUpdateHandler(pTimerHandler);
                        mIsObjectAlive.set(false);
                    }
                }
            }
        });

        registerUpdateHandler(mCollisionUpdateHandler);
        registerUpdateHandler(mTimerUpdateHandler);

        mPhysicBody.setLinearVelocity(ordinateSpeed, abscissaSpeed);
    }

    protected boolean isBulletOutOfRange() {
        return getX() < 0 || getY() < 0 || getX() > SizeConstants.GAME_FIELD_WIDTH || getY() > SizeConstants.GAME_FIELD_HEIGHT ||
                UnitPathUtil.getDistanceBetweenPoints(getX(), getY(), mBulletStartX, mBulletStartY) > mMaxBulletFlyDistance;
    }

    private void unregisterUpdateHandlers() {
        if (mCollisionUpdateHandler != null) unregisterUpdateHandler(mCollisionUpdateHandler);
        if (mTimerUpdateHandler != null) unregisterUpdateHandler(mTimerUpdateHandler);
    }

    private class TargetCollisionHandler implements ICollisionCallback {
        private final Damage mDamage;

        public TargetCollisionHandler(Damage damage) {
            mDamage = damage;
        }

        @Override
        public boolean onCollision(IShape pCheckShape, IShape pTargetShape) {
            synchronized (mIsObjectAlive) {
                if (!mIsObjectAlive.get() || !(pTargetShape instanceof GameObject))
                    return false;
                mIsObjectAlive.set(false);
            }
            if (!mIsFakeObject) {
                GameObject targetObject = (GameObject) pTargetShape;
                targetObject.damageObject(mDamage);
            }
            return true;
        }
    }
}
