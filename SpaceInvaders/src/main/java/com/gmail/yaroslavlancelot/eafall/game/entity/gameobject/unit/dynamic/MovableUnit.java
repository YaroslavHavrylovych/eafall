package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.entity.bullets.Bullet;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IVelocityListener;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.bonus.Bonus;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path.IUnitPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path.StaticHelper;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.math.MathUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Basic class for all dynamic game units */
public class MovableUnit extends Unit {
    public static final String TAG = MovableUnit.class.getCanonicalName();
    /** how often (in millis) unit bonuses should be updated */
    private static final int sBonusUpdateTime = 1000;
    /** body type */
    private static final BodyDef.BodyType sBodyType = BodyDef.BodyType.DynamicBody;
    /** current unit bonuses */
    protected final Map<Bonus, Integer> mBonuses = new HashMap<Bonus, Integer>();
    /** max velocity for this unit */
    protected float mMaxVelocity;
    /** unit moving path */
    protected IUnitPath mUnitPath;
    /** object bonus to the health */
    private int mObjectHealthBonus;
    /** chance to avoid an attack */
    private int mChanceToAvoidAnAttack;
    /** will trigger if object velocity changed */
    private IVelocityListener mVelocityChangedListener;

    /** create unit from appropriate builder */
    public MovableUnit(MovableUnitBuilder unitBuilder) {
        super(unitBuilder);
        mMaxVelocity = unitBuilder.getSpeed();
    }

    public void registerUpdateHandler() {
        registerUpdateHandler(new TimerHandler(mUpdateCycleTime, true, new SimpleUnitTimerCallback()));
    }

    @Override
    protected void rotationBeforeFire(GameObject attackedObject) {
        rotate(MathUtils.radToDeg(getDirection(attackedObject.getCenterX(), attackedObject.getCenterY())));
    }

    @Override
    protected void setBulletFirePosition(GameObject attackedObject, Bullet bullet) {
        Vector2 objectPosition = getBody().getPosition();
        bullet.fireFromPosition(objectPosition.x + Sizes.UNIT_SIZE / 2 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                objectPosition.y - Bullet.BULLET_SIZE / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                attackedObject);
    }

    @Override
    public BodyDef.BodyType getBodyType() {
        return sBodyType;
    }

    public int getChanceToAvoidAnAttack() {
        return mChanceToAvoidAnAttack;
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

    public void initMovingPath(boolean ltr, boolean top) {
        mUnitPath = StaticHelper.createUnitPath(ltr, top);
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

    public void setVelocityChangedListener(IVelocityListener velocityChangedListener) {
        mVelocityChangedListener = velocityChangedListener;
    }

    @Override
    public void setUnitLinearVelocity(float x, float y) {
        super.setUnitLinearVelocity(x, y);

        if (mVelocityChangedListener != null) {
            mVelocityChangedListener.velocityChanged(MovableUnit.this);
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
                    mObjectToAttack.isObjectAlive() && StaticHelper.getDistanceBetweenPoints(getX(), getY(),
                    mObjectToAttack.getCenterX(), mObjectToAttack.getCenterY()) < mViewRadius) {
                attackOrMove();
                return;
            } else {
                // we don't see previously attacked unit
                mObjectToAttack = null;
            }

            // search for new unit to attack
            if (mEnemiesUpdater != null) {
                List<GameObject> units = mEnemiesUpdater.getVisibleEnemiesForUnit(MovableUnit.this);
                if (units != null && !units.isEmpty()) {
                    mObjectToAttack = units.get(0);
                    attackOrMove();
                    return;
                } else if (mEnemiesUpdater.getMainTarget() != null &&
                        StaticHelper.getDistanceBetweenPoints(getX(), getY(), mEnemiesUpdater.getMainTarget().getX(),
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
            float distanceToTarget = StaticHelper.getDistanceBetweenPoints(getCenterX(), getCenterY(),
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