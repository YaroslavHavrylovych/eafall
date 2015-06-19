package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.entity.bullets.Bullet;
import com.gmail.yaroslavlancelot.eafall.game.entity.bullets.BulletPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.IPlayerObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IFireListener;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.AttachSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.CreatePhysicBodyEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.audio.sound.Sound;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import de.greenrobot.event.EventBus;

/** base class for dynamic and static/unmovable objects which can attack other objects */
public abstract class Unit extends GameObject implements
        IPlayerObject {
    /** tag for logger */
    public static final String TAG = Unit.class.getCanonicalName();
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
    protected IEnemiesFilter mEnemiesUpdater;
    /** last unit attack time */
    protected long mLastAttackTime;
    /** if fire method called it will be triggered */
    protected IFireListener mUnitFireCallback;
    /** unit shout sound */
    protected Sound mFireSound;
    /** unit player name */
    private volatile String mPlayerName;

    /** create unit from appropriate builder */
    public Unit(UnitBuilder unitBuilder) {
        super(-100, -100, unitBuilder.getWidth(), unitBuilder.getHeight(),
                unitBuilder.getTextureRegion(), unitBuilder.getObjectManager());
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

    public void setEnemiesUpdater(final IEnemiesFilter enemiesUpdater) {
        mEnemiesUpdater = enemiesUpdater;
    }

    @Override
    protected void initHealthBar() {
        if (Config.getConfig().isUnitsHealthBarEnabled()) {
            super.initHealthBar();
        }
    }

    @Override
    protected void onNegativeHealth() {
        super.onNegativeHealth();
        clearUpdateHandlers();
        if (mPlayerName != null) {
            PlayersHolder.getPlayer(mPlayerName).removeObjectFromPlayer(this);
        }
        getBody().setTransform(-100, -100, 0);
        getBody().setActive(false);
        onUnitDestroyed();
    }

    @Override
    public String getPlayer() {
        return mPlayerName;
    }

    @Override
    public void setPlayer(String playerName) {
        mPlayerName = playerName;
        setSpriteGroupName(BatchingKeys.getUnitSpriteGroup(playerName));
    }

    protected void onUnitDestroyed() {
    }

    /**
     * Init unit after creation. You need manually trigger this method after constructor at the time
     * when you want to init and attach this (totally working)  unit
     * <br/>
     * WARNING: unit player name have to be assigned before init() triggers
     */
    public void init(float x, float y) {
        LoggerHelper.methodInvocation(TAG, "init(float, float, String)");
        IPlayer player = PlayersHolder.getPlayer(mPlayerName);
        setHealth(mObjectMaximumHealth);
        initHealthBar();

        boolean existingUnit;
        float posX = x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                posY = y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                angle = 0f;
        if (getBody() == null) {
            existingUnit = false;
            EventBus.getDefault().post(new CreatePhysicBodyEvent(this, getBodyType(),
                    player.getFixtureDefUnit(), posX, posY, angle));
        } else {
            getBody().setActive(true);
            getBody().setTransform(posX, posY, angle);
            existingUnit = true;
        }

        if (player.getControlType() == IPlayer.ControlType.REMOTE_CONTROL_ON_CLIENT_SIDE) {
            removeDamage();
        }

        if (!existingUnit) {
            EventBus.getDefault().post(new AttachSpriteEvent(this));
        }
        player.addObjectToPlayer(this);
    }

    public void removeDamage() {
        mObjectDamage.removeDamage();
    }

    /** define unit behaviour/lifecycle */
    public abstract void registerUpdateHandler();

    public void fire(GameObject objectToAttack) {
        attackTarget(objectToAttack);
    }

    protected void attackTarget(GameObject attackedObject) {
        if (attackedObject == null) {
            return;
        }

        rotationBeforeFire(attackedObject);

        if (!isReloadFinished()) {
            return;
        }

        if (mUnitFireCallback != null) {
            mUnitFireCallback.fire(getObjectUniqueId(), attackedObject.getObjectUniqueId());
        }

        playSound(mFireSound);
        Bullet bullet = BulletPool.getInstance().obtainPoolItem();

        bulletFire(attackedObject, bullet);
    }

    /**
     * To rotate unit to the target before fire. Abstract because movable units does need it and
     * immovable doesn't.
     */
    protected abstract void rotationBeforeFire(GameObject attackedObject);

    /**
     * where the bullet will appear during the fire operation
     */
    protected void bulletFire(GameObject attackedObject, Bullet bullet) {
        bullet.fire(mObjectDamage, getX(), getY(), attackedObject);
    }
}