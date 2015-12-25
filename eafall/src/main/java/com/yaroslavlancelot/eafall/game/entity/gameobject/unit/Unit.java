package com.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.yaroslavlancelot.eafall.game.audio.LimitedSoundWrapper;
import com.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.yaroslavlancelot.eafall.game.client.IPhysicCreator;
import com.yaroslavlancelot.eafall.game.client.thick.income.ClientIncomeHandler;
import com.yaroslavlancelot.eafall.game.engine.CleanableSpriteGroup;
import com.yaroslavlancelot.eafall.game.engine.ManualFinishRotationModifier;
import com.yaroslavlancelot.eafall.game.entity.bullets.AbstractBullet;
import com.yaroslavlancelot.eafall.game.entity.bullets.BulletsPool;
import com.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.IPlayerObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.yaroslavlancelot.eafall.game.entity.gameobject.explosion.UnitExplosionAnimation;
import com.yaroslavlancelot.eafall.game.entity.gameobject.explosion.UnitExplosionPool;
import com.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IFireListener;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering.IUnitMap;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.PathHelper;
import com.yaroslavlancelot.eafall.game.entity.health.IHealthBar;
import com.yaroslavlancelot.eafall.game.entity.health.UnitHealthBar;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.math.MathUtils;

import java.util.Random;

/**
 * base class for dynamic and static/unmovable objects which can attack other objects
 *
 * @author Yaroslav Havrylovych
 */
public abstract class Unit extends GameObject implements
        IPlayerObject {
    /** tag for logger */
    public static final String TAG = Unit.class.getCanonicalName();
    /** tag for logger */
    public static final float ROTATION_SPEED = .0044f;
    /** unit Random instance */
    private static volatile Random sRandom = new Random();
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
    /** last unit attack time */
    protected long mLastAttackTime;
    /** if fire method called it will be triggered */
    protected IFireListener mUnitFireCallback;
    /** unit shout sound */
    protected LimitedSoundWrapper mFireSound;
    /** take care about unit rotation */
    protected ManualFinishRotationModifier mUnitRotationModifier = new ManualFinishRotationModifier(0, 0, 0);
    /** unit player name */
    protected String mPlayerName;

    /** create unit from appropriate builder */
    public Unit(UnitBuilder unitBuilder) {
        super(-100, -100, unitBuilder.getWidth(), unitBuilder.getHeight(),
                unitBuilder.getTextureRegion(), unitBuilder.getObjectManager());
        mUnitRotationModifier.setFinished(true);
        initHealth(unitBuilder.mHealth);
        mObjectArmor = new Armor(unitBuilder.getArmor().getArmorType(),
                unitBuilder.getArmor().getArmorValue());
        mObjectDamage = new Damage(unitBuilder.getDamage().getDamageType(),
                unitBuilder.getDamage().getDamageValue());
        mAttackRadius = unitBuilder.getAttackRadius();
        mViewRadius = unitBuilder.getViewRadius();
        setReloadTime(unitBuilder.getReloadTime());
        mFireSound = unitBuilder.getFireSound();
    }

    public abstract BodyDef.BodyType getBodyType();

    protected boolean isReloadFinished() {
        long time = System.currentTimeMillis();
        if (time - mLastAttackTime < mTimeForReload) {
            return false;
        }
        mLastAttackTime = time;
        return true;
    }

    public int getViewRadius() {
        return mViewRadius;
    }

    public void setReloadTime(double seconds) {
        mTimeForReload = seconds * 1000;
    }

    public void setUnitFireCallback(IFireListener unitFireCallback) {
        mUnitFireCallback = unitFireCallback;
    }

    @Override
    public String getPlayerName() {
        return mPlayerName;
    }

    @Override
    public void setPlayer(String playerName) {
        mPlayerName = playerName;
        setSpriteGroupName(BatchingKeys.getUnitSpriteGroup(playerName));
    }

    @Override
    protected void onNegativeHealth() {
        mPhysicBody.setActive(false);
        final UnitExplosionAnimation unitExplosionAnimation = UnitExplosionPool.getExplosion();
        unitExplosionAnimation.setPosition(mX, mY);
        unitExplosionAnimation.init(this);
        IPlayer enemyPlayer = PlayersHolder.getPlayer(mPlayerName).getEnemyPlayer();
        int incomeChance = enemyPlayer.getUnitDeathIncomeChance();
        if (incomeChance > 0) {
            int doneIfOne = sRandom.nextInt(incomeChance);
            if (doneIfOne == 0) {
                int money = mObjectMaximumHealth >> 2;
                if (enemyPlayer.getControlType().user()) {
                    ButtonSprite incomeButton = ClientIncomeHandler.getIncomeHandler().makeIncome(
                            ClientIncomeHandler.IncomeType.UNIT, money);
                    incomeButton.setPosition(mX, mY);
                } else {
                    enemyPlayer.changeMoney(money);
                }
            }
        }
    }

    @Override
    protected void initHealthBar() {
        super.initHealthBar();
        syncHealthBarBehaviour();
        updateHealthBar();
    }

    @Override
    protected IHealthBar createHealthBar() {
        return new UnitHealthBar(getPlayerName(), Math.max(mWidth, mHeight), getVertexBufferObjectManager());
    }

    @Override
    public void destroy() {
        PlayersHolder.getPlayer(mPlayerName)
                .getEnemyPlayer()
                .changeMoney(mObjectMaximumHealth / 100);
        clearUpdateHandlers();
        clearEntityModifiers();
        PlayersHolder.getPlayer(mPlayerName).removeObjectFromPlayer(this);
        mPhysicBody.setTransform(BODY_OUT_OF_CAMERA, BODY_OUT_OF_CAMERA, 0);
        mPhysicBody.setActive(false);
        setPosition(-100, -100);
        setTag(CleanableSpriteGroup.RECYCLE);
        onDestroyed();
    }

    protected void onDestroyed() {
    }

    /**
     * Init unit after creation. You need manually trigger this method after constructor at the time
     * when you want to init and attach this (totally working)  unit
     * <br/>
     * WARNING: unit player name have to be assigned before init() triggers
     */
    public void init(float x, float y, IPhysicCreator physicCreator) {
        //TODO logger was here
        setTag(0);
        IPlayer player = PlayersHolder.getPlayer(mPlayerName);
        mObjectCurrentHealth = mObjectMaximumHealth;
        initHealthBar();
        boolean existingUnit;
        float posX = x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                posY = y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        if (getBody() == null) {
            existingUnit = false;
            physicCreator.createPhysicBody(this, getBodyType(), player.getFixtureDefUnit());
        } else {
            existingUnit = getParent() != null;
        }
        setPosition(x, y);
        setRotation(PathHelper.isLeftSide(x) ? 90 : -90);
        mPhysicBody.setActive(true);
        mPhysicBody.setTransform(posX, posY, 0);
        if (!existingUnit) {
            attachSelf();
        }
        player.addObjectToPlayer(this);
    }

    /** trigger new unit lifecycle step */
    public abstract void lifecycleTick(IUnitMap enemiesMap);

    public void fire(GameObject objectToAttack) {
        attackTarget(objectToAttack);
    }

    protected void attackTarget(GameObject attackedObject) {
        int angle = getAngle(attackedObject.getX(), attackedObject.getY());
        if (needRotation(angle)) {
            rotateWithAngle(angle);
        }
        if (!isReloadFinished() || !mUnitRotationModifier.isFinished()) {
            return;
        }
        if (mUnitFireCallback != null) {
            mUnitFireCallback.fire(getObjectUniqueId(), attackedObject.getObjectUniqueId());
        }
        playSound(mFireSound);
        AbstractBullet bullet = BulletsPool.getBullet(mObjectDamage.getDamageType(),
                PlayersHolder.getPlayer(mPlayerName)
                        .getEnemyPlayer()
                        .getName());
        bullet.setRotation(mRotation);

        bulletFire(attackedObject, bullet);
    }

    /**
     * Only angles with absolute value > 4 need rotation
     *
     * @param angle given angle for rotation
     * @return true if absolute angle value > 4 and false in other case
     */
    protected boolean needRotation(int angle) {
        return (angle < -4) || (angle > 4);
    }

    /** used to sync settings health bar with one which used */
    public void syncHealthBarBehaviour() {
    }

    /**
     * Calculates the angle unit has to rotate.
     * <p/>
     * return the smallest angle unit has to rotate to get on direction from his current
     * position to given coordinates.
     * <br/>
     * E.g. unit are on position 10, 10 and his angle is 90. His next point is 10,20.
     * So unit will rotate to -90 degrees to point to the 10,20.
     *
     * @param x target abscissa
     * @param y target ordinate
     * @return angle unit has to rotate from current angle (-180 <= angle <= 180)
     */
    protected int getAngle(float x, float y) {
        int a = ((int) mRotation) % 360;
        int b = (int) MathUtils.radToDeg(getDirection(mX, mY, x, y));
        boolean plus = b > a;
        int alpha = plus ? b - a : a - b;
        int res;
        if (alpha <= 180) {
            res = plus ? alpha : -alpha;
        } else {
            res = plus ? alpha - 360 : 360 - alpha;
        }
        return res;
    }

    /**
     * rotate the unit from current_rotation to (current_rotation + angle)
     *
     * @param angle the angle unit has to pass
     */
    protected void rotateWithAngle(int angle) {
        unregisterEntityModifier(mUnitRotationModifier);
        int fromAngle = (int) getRotation();
        int toAngle = fromAngle + angle;
        mUnitRotationModifier.reset(ROTATION_SPEED * angle, fromAngle, toAngle);
        registerEntityModifier(mUnitRotationModifier);
    }

    /**
     * where the bullet will appear during the fire operation
     */
    protected void bulletFire(GameObject attackedObject, AbstractBullet bullet) {
        bullet.fire(mObjectDamage, getX(), getY(), attackedObject);
    }
}