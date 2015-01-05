package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities.AttachEntityEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.bonuses.Bonus;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.IUnitFireCallback;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Bullet;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.ISimpleUnitEnemiesUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitPathUtil;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.audio.sound.Sound;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.math.MathUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.greenrobot.event.EventBus;

/** Basic class for all dynamic game units */
public class Unit extends GameObject {
    public static final String TAG = Unit.class.getCanonicalName();
    /** how often (in millis) unit bonuses should be updated */
    private static final int sBonusUpdateTime = 1000;
    /** current unit bonuses */
    protected final Map<Bonus, Integer> mBonuses = new HashMap<Bonus, Integer>();
    /** max velocity for this unit */
    protected float mMaxVelocity;
    /** update time for current object */
    protected float mUpdateCycleTime = .5f;
    /** delay time between attacks */
    protected double mTimeForReload;
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
    /** unit attack sound */
    protected Sound mFireSound;
    /** last unit attack time */
    private long mLastAttackTime;
    /** if fireFromPosition method called it will be triggered */
    private IUnitFireCallback mUnitFireCallback;
    /** fixture def for bullets created by this unit */
    private FixtureDef mBulletFixtureDef;
    /** object bonus to the health */
    private int mObjectHealthBonus;
    /** chance to avoid an attack */
    private int mChanceToAvoidAnAttack;

    /** create unit from appropriate builder */
    public Unit(UnitBuilder unitBuilder) {
        super(-100, -100, unitBuilder.getTextureRegion(), unitBuilder.getObjectManager());
        mSoundOperations = unitBuilder.getSoundOperations();
        setWidth(unitBuilder.getWidth());
        setHeight(unitBuilder.getHeight());
        initHealth(unitBuilder.getHealth());
        mObjectArmor = unitBuilder.getArmor();
        mObjectDamage = unitBuilder.getDamage();
        mAttackRadius = unitBuilder.getAttackRadius();
        mViewRadius = unitBuilder.getViewRadius();
        mMaxVelocity = unitBuilder.getSpeed();
        setReloadTime(unitBuilder.getReloadTime());
        initSound(unitBuilder.getSoundPath());
    }

    public void setReloadTime(double seconds) {
        mTimeForReload = seconds * 1000;
    }

    protected void initSound(String path) {
        mFireSound = mSoundOperations.loadSound(path);
    }

    /**
     * add bonus to the unit
     *
     * @param bonus bonus to add
     * @param ttl   bonus leave time
     */
    public void addBonus(Bonus bonus, int ttl) {
        synchronized (mBonuses) {
            mBonuses.put(bonus, (int) System.currentTimeMillis() + ttl);
        }
    }

    /** remove bonus from the unit */
    public void removeBonus(Bonus bonus) {
        synchronized (mBonuses) {
            mBonuses.remove(bonus);
        }
    }

    public void registerUpdateHandler() {
        registerUpdateHandler(new TimerHandler(mUpdateCycleTime, true, new SimpleUnitTimerCallback()));
    }

    public void initMovingPath(boolean ltr, boolean top) {
        mUnitPath = UnitPathUtil.createUnitPath(ltr, top);
    }

    public void setUnitFireCallback(IUnitFireCallback unitFireCallback) {
        mUnitFireCallback = unitFireCallback;
    }

    public void setEnemiesUpdater(final ISimpleUnitEnemiesUpdater enemiesUpdater) {
        mEnemiesUpdater = enemiesUpdater;
    }

    public void fire(GameObject objectToAttack) {
        attackGoal(objectToAttack);
    }

    protected void attackGoal(GameObject attackedObject) {
        if (attackedObject == null) {
            return;
        }

        rotate(MathUtils.radToDeg(getDirection(attackedObject.getCenterX(), attackedObject.getCenterY())));

        if (!isReloadFinished()) {
            return;
        }

        if (attackedObject instanceof Unit) {
            int chanceToAvoidAnAttack = ((Unit) attackedObject).getChanceToAvoidAnAttack();
            if (chanceToAvoidAnAttack > 0) {
                if (new Random().nextInt(100) < chanceToAvoidAnAttack) {
                    return;
                }
            }
        }

        if (mUnitFireCallback != null) {
            mUnitFireCallback.fire(getObjectUniqueId(), attackedObject.getObjectUniqueId());
        }

        playSound(mFireSound, mSoundOperations);
        Bullet bullet = new Bullet(getVertexBufferObjectManager(), getBackgroundColor(), mObjectDamage, mBulletFixtureDef);
        Vector2 objectPosition = getBody().getPosition();

        bullet.fireFromPosition(objectPosition.x + SizeConstants.UNIT_SIZE / 2 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                objectPosition.y - Bullet.BULLET_SIZE / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                attackedObject);

        EventBus.getDefault().post(new AttachEntityEvent(bullet));
    }

    protected boolean isReloadFinished() {
        long time = System.currentTimeMillis();
        if (time - mLastAttackTime < mTimeForReload)
            return false;
        mLastAttackTime = time;
        return true;
    }

    public int getChanceToAvoidAnAttack() {
        return mChanceToAvoidAnAttack;
    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        return mObjectSprite.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    public int getViewRadius() {
        return mViewRadius;
    }

    public void removeDamage() {
        mObjectDamage.removeDamage();
    }

    public void setBulletFixtureDef(FixtureDef bulletFixtureDef) {
        mBulletFixtureDef = bulletFixtureDef;
    }

    /** remove bonuses which are supposed to die because of passes time */
    private void updateBonuses() {
        synchronized (mBonuses) {
            Map.Entry<Bonus, Integer> entry;
            int currentTime = (int) System.currentTimeMillis();
            for (Iterator<Map.Entry<Bonus, Integer>> it = mBonuses.entrySet().iterator(); it.hasNext(); ) {
                entry = it.next();
                if (entry.getValue() < currentTime) {
                    it.remove();
                }
            }
            Set<Bonus> bonusSet = mBonuses.keySet();
            //attack
            mObjectDamage.setAdditionalDamage(Bonus.getBonusByType(bonusSet,
                    Bonus.BonusType.ATTACK, mObjectDamage.getDamageValue()));
            //defence
            mObjectArmor.setAdditionalArmor(Bonus.getBonusByType(bonusSet,
                    Bonus.BonusType.DEFENCE, mObjectArmor.getArmorValue()));
            //health
            int healthBonus = Bonus.getBonusByType(bonusSet,
                    Bonus.BonusType.HEALTH, mObjectMaximumHealth);
            if (healthBonus > mObjectHealthBonus) {
                mObjectCurrentHealth += (healthBonus - mObjectHealthBonus);
                mObjectHealthBonus = healthBonus;
            }
            //avoid an attack
            mChanceToAvoidAnAttack = Bonus.getBonusByType(bonusSet,
                    Bonus.BonusType.AVOID_ATTACK_CHANCE, 0);
        }
    }

    /** used for update current object in game loop */
    protected class SimpleUnitTimerCallback implements ITimerCallback {
        private float[] mTwoDimensionFloatArray = new float[2];
        private int mLastBonusUpdateTime = (int) System.currentTimeMillis();

        @Override
        public void onTimePassed(TimerHandler pTimerHandler) {
            // update bonuses
            if (((int) System.currentTimeMillis()) - mLastBonusUpdateTime > sBonusUpdateTime) {
                updateBonuses();
            }
            // check for units to attack
            if (mObjectToAttack != null && mObjectToAttack != mEnemiesUpdater.getMainTarget() &&
                    mObjectToAttack.isObjectAlive() && UnitPathUtil.getDistanceBetweenPoints(getX(), getY(),
                    mObjectToAttack.getCenterX(), mObjectToAttack.getCenterY()) < mViewRadius) {
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
            float distanceToTarget = UnitPathUtil.getDistanceBetweenPoints(getCenterX(), getCenterY(),
                    mObjectToAttack.getCenterX(), mObjectToAttack.getCenterY())
                    //minus both objects radius to have distance between objects corners
                    //instead of distance between centers
                    - mObjectToAttack.getWidth() / 2
                    - getWidth() / 2;
            if (distanceToTarget < mAttackRadius) {
                attackGoal(mObjectToAttack);
                // stay on position
                setUnitLinearVelocity(0, 0);
            } else {
                // pursuit attacked unit
                moveToPoint(mObjectToAttack.getCenterX(), mObjectToAttack.getCenterY());
            }
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