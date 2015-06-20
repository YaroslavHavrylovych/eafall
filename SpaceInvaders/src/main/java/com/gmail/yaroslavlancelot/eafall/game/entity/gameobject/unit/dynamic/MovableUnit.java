package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IVelocityListener;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.bonus.Bonus;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path.IUnitPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path.StaticHelper;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.util.math.MathUtils;

import java.util.HashMap;
import java.util.Iterator;
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

    public int getChanceToAvoidAnAttack() {
        return mChanceToAvoidAnAttack;
    }

    public void setVelocityChangedListener(IVelocityListener velocityChangedListener) {
        mVelocityChangedListener = velocityChangedListener;
    }

    @Override
    public BodyDef.BodyType getBodyType() {
        return sBodyType;
    }

    @Override
    protected void onNegativeHealth() {
        removeBonuses();
        super.onNegativeHealth();
    }

    @Override
    public void registerUpdateHandler() {
        //TODO no need to create object each time
        registerUpdateHandler(new TimerHandler(mUpdateCycleTime, true, new SimpleUnitTimerCallback()));
    }

    @Override
    protected void rotationBeforeFire(GameObject attackedObject) {
        rotate(MathUtils.radToDeg(getDirection(attackedObject.getX(), attackedObject.getY())));
    }

    @Override
    public void setUnitLinearVelocity(float x, float y) {
        super.setUnitLinearVelocity(x, y);

        if (mVelocityChangedListener != null) {
            mVelocityChangedListener.velocityChanged(MovableUnit.this);
        }
    }

    /** remove all bonus from the unit */
    public void removeBonuses() {
        synchronized (mBonuses) {
            mBonuses.clear();
        }
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

    /** used for update current object in game loop */
    protected class SimpleUnitTimerCallback implements ITimerCallback {
        private float[] mTwoDimensionFloatArray = new float[2];
        private int mLastBonusUpdateTime = (int) System.currentTimeMillis();

        @Override
        public void onTimePassed(TimerHandler pTimerHandler) {
            //The whole bunch of operations is on the UPDATE THREAD!!!

            // update bonuses
            if (((int) System.currentTimeMillis()) - mLastBonusUpdateTime > sBonusUpdateTime) {
                updateBonuses();
            }

            //TODO maybe it's reasonable to save unit id to check if it the same unit before attack?
            //TODO and the save unit id for stationary unit as well
            if (mObjectToAttack == null) {
                mObjectToAttack = mEnemiesUpdater
                        .getFirstEnemyInRange(MovableUnit.this, mAttackRadius);
                //attack founded enemy
                if (mObjectToAttack != null) {
                    attackTarget(mObjectToAttack);
                    return;
                } else {
                    mObjectToAttack = mEnemiesUpdater
                            .getFirstEnemyInRange(MovableUnit.this, getViewRadius());
                    //chase founded object
                    if (mObjectToAttack != null) {
                        // pursuit attacked unit
                        moveToPoint(mObjectToAttack.getX(), mObjectToAttack.getY());
                        return;
                    }
                }
            }

            if (mObjectToAttack != null) {
                if (!mObjectToAttack.isObjectAlive() || !attackOrMove(mObjectToAttack)) {
                    //object not in the view
                    mObjectToAttack = null;
                }
                return;
            } else {
                GameObject mainTarget = mEnemiesUpdater.getMainTarget();
                if (mainTarget != null && mainTarget.isObjectAlive()) {
                    //if main target in view or in attack range
                    if (attackOrMove(mainTarget)) {
                        return;
                    }
                }
            }
            // move by path (no units in visible range)
            mTwoDimensionFloatArray[0] = getX();
            mTwoDimensionFloatArray[1] = getY();
            mUnitPath.getNextPathPoint(mTwoDimensionFloatArray, mTwoDimensionFloatArray);

            moveToPoint(mTwoDimensionFloatArray[0], mTwoDimensionFloatArray[1]);
        }

        /**
         * Attacks or moves closer to target unit if in range
         *
         * @param enemy enemy game object (e.g. unit) to attack or chase)
         * @return true - if the enemy unit was attacked or chased by this unit
         * <br/>
         * false - if the enemy unit not in the range.
         */
        private boolean attackOrMove(GameObject enemy) {
            // check if we already can attack
            float distanceToTarget = StaticHelper.getDistanceBetweenPoints(getX(), getY(),
                    enemy.getX(), enemy.getY())
                    //minus both objects radius to have distance between objects corners
                    //instead of distance between centers
                    - enemy.getWidth() / 2 - getWidth() / 2;
            if (distanceToTarget < mAttackRadius) {
                attackTarget(enemy);
            } else if (distanceToTarget < getViewRadius()) {
                // pursuit attacked unit
                moveToPoint(enemy.getX(), enemy.getY());
            } else {
                return false;
            }
            return true;
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