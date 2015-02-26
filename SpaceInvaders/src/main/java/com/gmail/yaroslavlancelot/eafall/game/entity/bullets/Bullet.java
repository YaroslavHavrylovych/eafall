package com.gmail.yaroslavlancelot.eafall.game.entity.bullets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.entity.RectangleWithBody;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.AttachEntityEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.CreatePhysicBodyEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.RunOnUpdateThreadEvent;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import java.util.concurrent.atomic.AtomicBoolean;

import de.greenrobot.event.EventBus;

public class Bullet extends RectangleWithBody implements RunOnUpdateThreadEvent.UpdateThreadRunnable {
    /** bullet size in points */
    public static final int BULLET_SIZE = 3;
    private static final BodyDef.BodyType sBulletBodyType = BodyDef.BodyType.DynamicBody;
    private static final float sMaxVelocity = 2.0f;
    /** is bullet still alive (not out of range and doesn't already hit an enemy) */
    private final AtomicBoolean mIsObjectAlive;
    /** update time for current object */
    protected float mUpdateCycleTime = .5f;
    /** bullet lifecycle handler */
    private TimerHandler mLifecycleHandler = new TimerHandler(mUpdateCycleTime, true, new ITimerCallback() {
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
                    destroyBullet();
                }
            }
        }
    });
    private FixtureDef mBulletFixtureDef;
    /** the maximum distance bullet can damage (it's usual 2 times distance than it's enemy) */
    private float mMaxBulletFlyDistance;
    private float mBulletStartX, mBulletStartY;
    /** bullet damage value */
    private Damage mDamage;

    Bullet(VertexBufferObjectManager vertexBufferObjectManager) {
        super(-100, -100, BULLET_SIZE, BULLET_SIZE, vertexBufferObjectManager);
        mIsObjectAlive = new AtomicBoolean(true);
    }

    public void init(Color color, Damage damage, FixtureDef fixtureDef) {
        setColor(color);
        synchronized (mIsObjectAlive) {
            mDamage = damage;
            mBulletFixtureDef = fixtureDef;
        }
    }

    public boolean getAndSetFalseIsObjectAlive() {
        return mIsObjectAlive.getAndSet(false);
    }

    public void fireFromPosition(float x, float y, RectangleWithBody gameObject) {
        Vector2 target = gameObject.getBody().getPosition();
        float goalX = target.x, goalY = target.y;
        if (mPhysicBody == null) {
            EventBus.getDefault().post(new AttachEntityEvent(this));
            EventBus.getDefault().post(new CreatePhysicBodyEvent(this, sBulletBodyType, mBulletFixtureDef, x, y, 0));
            mPhysicBody.setActive(false);
            mPhysicBody.setBullet(true);
        } else {
            mPhysicBody.setTransform(x, y, 0);
        }
        mMaxBulletFlyDistance = StaticHelper.getDistanceBetweenPoints(
                mBulletStartX = x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                mBulletStartY = y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                gameObject.getX(), gameObject.getY());

        float distanceX = goalX - x,
                distanceY = goalY - y;
        float absDistanceX = Math.abs(distanceX),
                absDistanceY = Math.abs(distanceY),
                maxAbsDistance = absDistanceX > absDistanceY ? absDistanceX : absDistanceY;
        float ordinateSpeed = sMaxVelocity * distanceX / maxAbsDistance,
                abscissaSpeed = sMaxVelocity * distanceY / maxAbsDistance;

        registerUpdateHandler(mLifecycleHandler);
        mPhysicBody.setLinearVelocity(ordinateSpeed, abscissaSpeed);
        mIsObjectAlive.set(true);
        mPhysicBody.setActive(true);
    }

    protected boolean isBulletOutOfRange() {
        return getX() < 0 || getY() < 0 || getX() > Sizes.GAME_FIELD_WIDTH || getY() > Sizes.GAME_FIELD_HEIGHT ||
                StaticHelper.getDistanceBetweenPoints(getX(), getY(), mBulletStartX, mBulletStartY) > mMaxBulletFlyDistance;
    }

    public void destroyBullet() {
        EventBus.getDefault().post(this);
    }

    public Damage getDamage() {
        return mDamage;
    }

    @Override
    public void updateThreadCallback() {
        getBody().setTransform(-100, 100, 0);
        getBody().setActive(false);
        onBulletDestroyed();
    }

    protected void onBulletDestroyed() {
    }
}
