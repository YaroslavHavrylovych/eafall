package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.IUnitFireCallback;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitPathUtil;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.audio.sound.Sound;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.math.MathUtils;

import java.util.List;

/** Basic class for all dynamic game units */
public abstract class Unit extends GameObject {
    public static final String TAG = Unit.class.getCanonicalName();
    /** max velocity for this unit */
    protected float mMaxVelocity = 1.5f;
    /** update time for current object */
    protected float mUpdateCycleTime = .5f;
    /** delay time between attacks */
    protected double mTimeForReload = 1.5 * 1000;
    /** attack radius of current unit */
    protected int mAttackRadius;
    /** area in which unit can search for enemies */
    protected int mViewRadius;
    /** currently attacked object */
    protected GameObject mObjectToAttack;
    /** callback for using to update unit visible enemies */
    protected ISimpleUnitEnemiesUpdater mEnemiesUpdater;
    /** unit path */
    protected UnitPathUtil.UnitPath mUnitPath;
    /** for sound manipulations */
    protected SoundOperations mSoundOperations;
    /** for creating new entities */
    protected EntityOperations mEntityOperations;
    /** unit attack sound */
    protected Sound mFireSound;
    /** last unit attack time */
    private long mLastAttackTime;
    /** if fireFromPosition method called it will be triggered */
    private IUnitFireCallback mUnitFireCallback;
    /** fixture def for bullets created by this unit */
    private FixtureDef mBulletFixtureDef;

    protected Unit(ITextureRegion textureRegion, SoundOperations soundOperations, EntityOperations entityOperations) {
        super(-100, -100, textureRegion, entityOperations.getObjectManager());

        mSoundOperations = soundOperations;
        mEntityOperations = entityOperations;
    }

    public void registerUpdateHandler() {
        registerUpdateHandler(new TimerHandler(mUpdateCycleTime, true, new SimpleUnitTimerCallback()));
    }

    protected void initSound(String path) {
        mFireSound = mSoundOperations.loadSound(path);
    }

    public void initMovingPath() {
        mUnitPath = UnitPathUtil.getUnitPathAccordingToStartAbscissa(getX());
    }

    public void setUnitFireCallback(IUnitFireCallback unitFireCallback) {
        mUnitFireCallback = unitFireCallback;
    }

    public void setReloadTime(double seconds) {
        mTimeForReload = seconds * 1000;
    }

    public void setEnemiesUpdater(final ISimpleUnitEnemiesUpdater enemiesUpdater) {
        mEnemiesUpdater = enemiesUpdater;
    }

    public void fire(GameObject objectToAttack) {
        attackGoal(objectToAttack);
    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        return mObjectSprite.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    public int getViewRadius() {
        return mViewRadius;
    }

    @Override
    public void setBody(Body body) {
        super.setBody(body);
        if (getBody().getType().equals(BodyDef.BodyType.KinematicBody)) {
            mObjectDamage.removeDamage();
        }
    }

    protected void attackGoal(GameObject attackedObject) {
        if (attackedObject == null) return;

        rotate(MathUtils.radToDeg(getDirection(attackedObject.getX(), attackedObject.getY())));

        if (!isReloadFinished()) return;

        if (mUnitFireCallback != null)
            mUnitFireCallback.fire(getObjectUniqueId(), attackedObject.getObjectUniqueId());

        LoggerHelper.printDebugMessage(TAG, "unit=" + getObjectUniqueId() + "(" + getX() + "," + getY() + ")" +
                ", attack object=" + attackedObject.getObjectUniqueId() + "(" + attackedObject.getX() + "," + attackedObject.getY() + ")");
        playSound(mFireSound, mSoundOperations);
        Bullet bullet = new Bullet(getVertexBufferObjectManager(), mEntityOperations,
                getBackgroundColor(), mObjectDamage, mBulletFixtureDef);
        Vector2 objectPosition = getBody().getPosition();

        bullet.fireFromPosition(objectPosition.x + SizeConstants.UNIT_SIZE / 2 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                objectPosition.y - Bullet.BULLET_SIZE / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                attackedObject);

        mEntityOperations.attachEntity(bullet);
    }

    protected boolean isReloadFinished() {
        long time = System.currentTimeMillis();
        if (time - mLastAttackTime < mTimeForReload)
            return false;
        mLastAttackTime = time;
        return true;
    }

    public void setBulletFixtureDef(FixtureDef bulletFixtureDef) {
        mBulletFixtureDef = bulletFixtureDef;
    }

    /** used for update current object in game loop */
    protected class SimpleUnitTimerCallback implements ITimerCallback {
        private float[] mTwoDimensionFloatArray = new float[2];

        @Override
        public void onTimePassed(TimerHandler pTimerHandler) {
            // check for units to attack
            if (mObjectToAttack != null && mObjectToAttack != mEnemiesUpdater.getMainTarget() &&
                    mObjectToAttack.isObjectAlive() && UnitPathUtil.getDistanceBetweenPoints(getX(), getY(),
                    mObjectToAttack.getX(), mObjectToAttack.getY()) < mViewRadius) {
                attackOrMove();
                return;
            } else {
                // we don't see previously attacked unit
                mObjectToAttack = null;
            }

            // search for new unit to attack
            if (mEnemiesUpdater != null) {
                List<GameObject> units = mEnemiesUpdater.getVisibleEnemiesForUnit(Unit.this);
                if (units != null && !units.isEmpty()) {
                    mObjectToAttack = units.get(0);
                    attackOrMove();
                    return;
                } else if (mEnemiesUpdater.getMainTarget() != null &&
                        UnitPathUtil.getDistanceBetweenPoints(getX(), getY(), mEnemiesUpdater.getMainTarget().getX(),
                                mEnemiesUpdater.getMainTarget().getY()) < mViewRadius) {
                    mObjectToAttack = mEnemiesUpdater.getMainTarget();
                    attackOrMove();
                    return;
                }
            }

            // move by path, without attack other units
            mTwoDimensionFloatArray[0] = getX();
            mTwoDimensionFloatArray[1] = getY();
            mUnitPath.getNextPathPoint(mTwoDimensionFloatArray, mTwoDimensionFloatArray);

            moveToPoint(mTwoDimensionFloatArray[0], mTwoDimensionFloatArray[1]);
        }

        /**
         * Call it when target in view radius.
         * This method will move unit closer to target or shoot if it in attack radius.
         */
        private void attackOrMove() {
            // check if we already can attack
            float distanceToTarget = UnitPathUtil.getDistanceBetweenPoints(getX(), getY(), mObjectToAttack.getX(), mObjectToAttack.getY());
            if (distanceToTarget < mAttackRadius) {
                attackGoal(mObjectToAttack);
                // stay on position
                setUnitLinearVelocity(0, 0);
            } else
                // pursuit attacked unit
                moveToPoint(mObjectToAttack.getX(), mObjectToAttack.getY());
        }

        private void moveToPoint(float x, float y) {
            rotate(MathUtils.radToDeg(getDirection(x, y)));

            float distanceX = x - getX(),
                    distanceY = y - getY();
            float absDistanceX = Math.abs(distanceX),
                    absDistanceY = Math.abs(distanceY),
                    maxAbsDistance = absDistanceX > absDistanceY ? absDistanceX : absDistanceY;
            float ordinateSpeed = mMaxVelocity * distanceX / maxAbsDistance,
                    abscissaSpeed = mMaxVelocity * distanceY / maxAbsDistance;

            setUnitLinearVelocity(ordinateSpeed, abscissaSpeed);
        }
    }
}