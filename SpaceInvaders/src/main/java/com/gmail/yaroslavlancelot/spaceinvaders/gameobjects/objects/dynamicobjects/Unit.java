package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects;

import com.badlogic.gdx.math.Vector2;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitPathUtil;
import org.andengine.audio.sound.Sound;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.List;

/** Basic class for all dynamic game units */
public abstract class Unit extends GameObject {
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
    /** last unit attack time */
    private long mLastAttackTime;
    /** unit attack sound */
    protected Sound mFireSound;


    protected Unit(ITextureRegion textureRegion, SoundOperations soundOperations, EntityOperations entityOperations) {
        super(-100, -100, textureRegion, entityOperations.getObjectManager());
        registerUpdateHandler(new TimerHandler(mUpdateCycleTime, true, new SimpleUnitTimerCallback()));
        mSoundOperations = soundOperations;
        mEntityOperations = entityOperations;
    }

    protected void initSound(String path) {
        mFireSound = mSoundOperations.loadSound(path);
    }

    public void calculateUnitPath() {
        mUnitPath = UnitPathUtil.getUnitPathAccordingToStartAbscissa(getX());
    }

    public void setReloadTime(double seconds) {
        mTimeForReload = seconds * 1000;
    }

    public void setEnemiesUpdater(final ISimpleUnitEnemiesUpdater enemiesUpdater) {
        mEnemiesUpdater = enemiesUpdater;
    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        return mObjectSprite.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    public int getViewRadius() {
        return mViewRadius;
    }

    protected void attackGoal() {
        if (!isReloadFinished())
            return;
        mObjectToAttack.damageObject(mObjectDamage);
    }

    protected boolean isReloadFinished() {
        long time = System.currentTimeMillis();
        if (time - mLastAttackTime < mTimeForReload)
            return false;
        mLastAttackTime = time;
        return true;
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
                List<GameObject> units = mEnemiesUpdater.getEnemiesUnitsForUnit(Unit.this);
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

        private void attackOrMove() {
            // check if we already can attack
            if (UnitPathUtil.getDistanceBetweenPoints(
                    getX(), getY(), mObjectToAttack.getX(), mObjectToAttack.getY()) < mAttackRadius) {
                attackGoal();
                // stay on position
                setUnitLinearVelocity(0, 0);
            } else
                // pursuit attacked unit
                moveToPoint(mObjectToAttack.getX(), mObjectToAttack.getY());
        }

        private void moveToPoint(float x, float y) {
            float distanceX = x - getX(),
                    distanceY = y - getY();
            float absDistanceX = Math.abs(distanceX),
                    absDistanceY = Math.abs(distanceY),
                    maxAbsDistance = absDistanceX > absDistanceY ? absDistanceX : absDistanceY;
            float ordinateSpeed = mMaxVelocity * distanceX / maxAbsDistance,
                    abscissaSpeed = mMaxVelocity * distanceY / maxAbsDistance;

            setUnitLinearVelocity(ordinateSpeed, abscissaSpeed);
        }

        private void setUnitLinearVelocity(float x, float y) {
            final Vector2 velocity = Vector2Pool.obtain(x, y);
            mPhysicBody.setLinearVelocity(velocity);
            Vector2Pool.recycle(velocity);
        }
    }
}