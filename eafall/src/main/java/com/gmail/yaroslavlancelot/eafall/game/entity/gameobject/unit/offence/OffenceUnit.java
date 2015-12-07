package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.configuration.game.ApplicationSettings;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IVelocityListener;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.bonus.Bonus;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering.IUnitMap;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.IUnitPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.PathHelper;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/** Basic class for all dynamic game units */
public class OffenceUnit extends Unit {
    public static final String TAG = OffenceUnit.class.getCanonicalName();
    /** how often (in millis) unit bonuses should be updated */
    private static final long sBonusUpdateTime = TimeUnit.SECONDS.toMillis(1);
    /** how long health bar should be visible after unit was hit */
    private static final long sHealthBarVisibleTime = TimeUnit.SECONDS.toMillis(3);
    /** body type */
    private static final BodyDef.BodyType sBodyType = BodyDef.BodyType.DynamicBody;
    /** current unit bonuses */
    protected final Map<Bonus, Long> mBonuses = new HashMap<>();
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
    /** to track health bar visibility, last time unit took damage */
    private volatile long mLastHitTime;
    private float[] mTwoDimensionFloatArray = new float[2];
    private long mLastBonusUpdateTime = System.currentTimeMillis();

    /** create unit from appropriate builder */
    public OffenceUnit(OffenceUnitBuilder unitBuilder) {
        super(unitBuilder);
        mMaxVelocity = unitBuilder.getSpeed();
    }

    public int getChanceToAvoidAnAttack() {
        return mChanceToAvoidAnAttack;
    }

    private boolean isHealthBarDefaultBehaviour() {
        return EaFallApplication.getConfig().getSettings().getHealthBarBehavior() ==
                ApplicationSettings.UnitHealthBarBehavior.DEFAULT;
    }

    public void setVelocityChangedListener(IVelocityListener velocityChangedListener) {
        mVelocityChangedListener = velocityChangedListener;
    }

    @Override
    public BodyDef.BodyType getBodyType() {
        return sBodyType;
    }

    @Override
    public void destroy() {
        removeBonuses();
        if (mHealthBar != null) {
            mHealthBar.setVisible(false);
            mLastHitTime = 0;
        }
        super.destroy();
    }

    @Override
    public void lifecycleTick(IUnitMap enemiesMap) {
        //The whole bunch of operations is on the UPDATE THREAD!!!

        // update bonuses
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastBonusUpdateTime > sBonusUpdateTime) {
            updateBonuses();
        }

        if (isHealthBarDefaultBehaviour()) {
            // update health bar visibility
            if (currentTime - mLastHitTime > sHealthBarVisibleTime) {
                mLastHitTime = 0;
                updateHealthBar();
            }
        }

        if (mObjectToAttack == null) {
            mObjectToAttack = enemiesMap.getClosestUnit(mX, mY, mAttackRadius);
            //attack founded enemy
            if (mObjectToAttack != null) {
                setUnitLinearVelocity(0, 0);
                attackTarget(mObjectToAttack);
                return;
            } else {
                mObjectToAttack = enemiesMap.getClosestUnit(mX, mY, mViewRadius);
                //chase founded object
                if (mObjectToAttack != null) {
                    // pursuit attacked unit
                    moveToPoint(mObjectToAttack.getX(), mObjectToAttack.getY());
                    return;
                }
            }
        }

        if (mObjectToAttack != null) {
            if (!mObjectToAttack.isObjectAlive() || !attackOrMoveIfInRange(mObjectToAttack)) {
                //object not in the view
                mObjectToAttack = null;
            }
            return;
        } else {
            GameObject mainTarget = PlayersHolder.getPlayer(mPlayerName)
                    .getEnemyPlayer()
                    .getPlanet();
            if (mainTarget != null && mainTarget.isObjectAlive()) {
                //if main target in view or in attack range
                if (attackOrMoveIfInRange(mainTarget)) {
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

    @Override
    public void syncHealthBarBehaviour() {
        if (EaFallApplication.getConfig().getSettings().getHealthBarBehavior()
                == ApplicationSettings.UnitHealthBarBehavior.ALWAYS_VISIBLE) {
            mHealthBar.setVisible(true);
        } else {
            mHealthBar.setVisible(false);
        }
    }

    @Override
    protected void updateHealthBar() {
        boolean updatePosition = false;
        if (isHealthBarDefaultBehaviour()) {
            boolean newVisible = mObjectCurrentHealth < mObjectMaximumHealth / 2 || mLastHitTime > 0;
            updatePosition = newVisible != mHealthBar.isVisible();
            mHealthBar.setVisible(newVisible);
        }
        if (mHealthBar.isVisible()) {
            if (updatePosition) {
                updateHealthBarPosition();
            }
            mHealthBar.redrawHealthBar(mObjectMaximumHealth, mObjectCurrentHealth);
        }
    }

    @Override
    public void damageObject(Damage damage) {
        if (isHealthBarDefaultBehaviour()) {
            mLastHitTime = System.currentTimeMillis();
        }
        super.damageObject(damage);
    }

    @Override
    public void setUnitLinearVelocity(float x, float y) {
        super.setUnitLinearVelocity(x, y);
        if (mVelocityChangedListener != null) {
            mVelocityChangedListener.velocityChanged(OffenceUnit.this);
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
     */
    public void addBonus(Bonus bonus, int timeToLive) {
        synchronized (mBonuses) {
            mBonuses.put(bonus, System.currentTimeMillis() + timeToLive);
        }
    }

    /** remove bonus from the unit */
    public void removeBonus(Bonus bonus) {
        synchronized (mBonuses) {
            mBonuses.remove(bonus);
        }
    }

    public void initMovingPath(boolean ltr, boolean top) {
        mUnitPath = PathHelper.createUnitPath(ltr, top);
    }

    /** remove bonuses which are supposed to die because of passes time */
    private void updateBonuses() {
        synchronized (mBonuses) {
            Map.Entry<Bonus, Long> entry;
            long currentTime = System.currentTimeMillis();
            for (Iterator<Map.Entry<Bonus, Long>> it = mBonuses.entrySet().iterator(); it.hasNext(); ) {
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

    /**
     * Attacks or moves closer to target unit if in range
     *
     * @param enemy enemy game object (e.g. unit) to attack or chase)
     * @return true - if the enemy unit was attacked or chased by this unit
     * <br/>
     * false - if the enemy unit not in the range.
     */
    private boolean attackOrMoveIfInRange(GameObject enemy) {
        // check if we already can attack
        float distanceToTarget = PathHelper.getDistanceBetweenPoints(getX(), getY(),
                enemy.getX(), enemy.getY())
                //minus both objects radius to have distance between objects corners
                //instead of distance between centers
                - enemy.getWidth() / 2 - getWidth() / 2;
        if (distanceToTarget < mAttackRadius) {
            setUnitLinearVelocity(0, 0);
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
        int angle = getAngle(x, y);
        if (needRotation(angle)) {
            rotateWithAngle(angle);
        }

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