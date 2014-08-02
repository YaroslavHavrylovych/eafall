package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.CreateCircleBodyEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities.DetachEntityEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.RectangleWithBody;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitPathUtil;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.util.concurrent.atomic.AtomicBoolean;

import de.greenrobot.event.EventBus;

public class Bullet extends RectangleWithBody {
    public static final int BULLET_SIZE = 3;
    private static final BodyDef.BodyType sBulletBodyType = BodyDef.BodyType.DynamicBody;
    private static final float sMaxVelocity = 2.0f;
    private final FixtureDef mBulletFixtureDef;
    private final AtomicBoolean mIsObjectAlive;
    /** update time for current object */
    protected float mUpdateCycleTime = .5f;
    private float mMaxBulletFlyDistance;
    private float mBulletStartX, mBulletStartY;
    private Damage mDamage;

    public Bullet(VertexBufferObjectManager vertexBufferObjectManager, Color color, Damage damage, FixtureDef fixtureDef) {
        super(0, 0, BULLET_SIZE, BULLET_SIZE, vertexBufferObjectManager);
        setColor(color);
        mIsObjectAlive = new AtomicBoolean(true);
        mDamage = damage;
        mBulletFixtureDef = fixtureDef;
    }

    public boolean getAndSetFalseIsObjectAlive() {
        return mIsObjectAlive.getAndSet(false);
    }

    public void fireFromPosition(float x, float y, RectangleWithBody gameObject) {
        if (gameObject.getBody() == null) return;
        Vector2 target = gameObject.getBody().getPosition();
        float goalX = target.x, goalY = target.y;
        EventBus.getDefault().post(new CreateCircleBodyEvent(this, sBulletBodyType, mBulletFixtureDef, x, y, 0));

        mPhysicBody.setBullet(true);
        mMaxBulletFlyDistance = UnitPathUtil.getDistanceBetweenPoints(
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
                        EventBus.getDefault().post(new DetachEntityEvent(Bullet.this));
                    }
                }
            }
        });

        registerUpdateHandler(timerUpdateHandler);

        mPhysicBody.setLinearVelocity(ordinateSpeed, abscissaSpeed);
    }

    protected boolean isBulletOutOfRange() {
        return getX() < 0 || getY() < 0 || getX() > SizeConstants.GAME_FIELD_WIDTH || getY() > SizeConstants.GAME_FIELD_HEIGHT ||
                UnitPathUtil.getDistanceBetweenPoints(getX(), getY(), mBulletStartX, mBulletStartY) > mMaxBulletFlyDistance;
    }

    public Damage getDamage() {
        return mDamage;
    }
}
