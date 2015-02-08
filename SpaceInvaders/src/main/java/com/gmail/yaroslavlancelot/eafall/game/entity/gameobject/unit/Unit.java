package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.gmail.yaroslavlancelot.eafall.game.entity.bullets.Bullet;
import com.gmail.yaroslavlancelot.eafall.game.entity.bullets.BulletPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IFireListener;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.AttachEntityEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.CreatePhysicBodyEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.RunOnUpdateThreadEvent;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;

import org.andengine.audio.sound.Sound;
import org.andengine.input.touch.TouchEvent;

import de.greenrobot.event.EventBus;

/** base class for dynamic and static/unmovable objects which can attack other objects */
public abstract class Unit extends GameObject implements RunOnUpdateThreadEvent.UpdateThreadRunnable {
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
    /** for sound manipulations */
    protected SoundOperations mSoundOperations;
    /** unit attack sound */
    protected Sound mFireSound;
    /** last unit attack time */
    protected long mLastAttackTime;
    /** if fireFromPosition method called it will be triggered */
    protected IFireListener mUnitFireCallback;
    /** fixture def for bullets created by this unit */
    private FixtureDef mBulletFixtureDef;
    /** unit team name */
    private volatile String mTeamName;

    /** create unit from appropriate builder */
    public Unit(UnitBuilder unitBuilder) {
        super(-100, -100, unitBuilder.getTextureRegion(), unitBuilder.getObjectManager());
        mSoundOperations = unitBuilder.getSoundOperations();
        setWidth(unitBuilder.getWidth());
        setHeight(unitBuilder.getHeight());
        initHealth(unitBuilder.getHealth());
        mObjectArmor = new Armor(unitBuilder.getArmor().getArmorType(),
                unitBuilder.getArmor().getArmorValue());
        mObjectDamage = new Damage(unitBuilder.getDamage().getDamageType(),
                unitBuilder.getDamage().getDamageValue());
        mAttackRadius = unitBuilder.getAttackRadius();
        mViewRadius = unitBuilder.getViewRadius();
        setReloadTime(unitBuilder.getReloadTime());
        initSound(unitBuilder.getSoundPath());
    }

    public void setReloadTime(double seconds) {
        mTimeForReload = seconds * 1000;
    }

    protected void initSound(String path) {
        mFireSound = mSoundOperations.loadSound(path);
    }

    @Override
    public void updateThreadCallback() {
        getBody().setTransform(-100, -100, 0);
        getBody().setActive(false);
        onUnitDestroyed();
    }

    protected void onUnitDestroyed() {
    }

    @Override
    protected void onNegativeHealth() {
        super.onNegativeHealth();
        clearUpdateHandlers();
        if (mTeamName != null) {
            TeamsHolder.getTeam(mTeamName).removeObjectFromTeam(this);
        }
        EventBus.getDefault().post(this);
    }

    /**
     * Init unit after creation. You need manually trigger this method after constructor at the time
     * when you want to init and attach this (totally working)  unit
     */
    public void init(String teamName, float x, float y) {
        mTeamName = teamName;
        ITeam team = TeamsHolder.getTeam(teamName);
        boolean existingUnit;

        if (getBody() == null) {
            existingUnit = true;
            EventBus.getDefault().post(new CreatePhysicBodyEvent(this, getBodyType(), team.getFixtureDefUnit()));
        } else {
            getBody().setActive(true);
            existingUnit = false;
        }

        setBulletFixtureDef(CollisionCategories.getBulletFixtureDefByUnitCategory(
                team.getFixtureDefUnit().filter.categoryBits));

        if (team.getTeamControlType() == TeamControlBehaviourType.REMOTE_CONTROL_ON_CLIENT_SIDE) {
            removeDamage();
        }

        setPosition(x, y);
        if (existingUnit) {
            EventBus.getDefault().post(new AttachEntityEvent(this, false, false));
        }
        team.addObjectToTeam(this);
    }

    public abstract BodyDef.BodyType getBodyType();

    public void setBulletFixtureDef(FixtureDef bulletFixtureDef) {
        mBulletFixtureDef = bulletFixtureDef;
    }

    public void removeDamage() {
        mObjectDamage.removeDamage();
    }

    /** define unit behaviour/lifecycle */
    public abstract void registerUpdateHandler();

    public void setUnitFireCallback(IFireListener unitFireCallback) {
        mUnitFireCallback = unitFireCallback;
    }

    public void setEnemiesUpdater(final IEnemiesFilter enemiesUpdater) {
        mEnemiesUpdater = enemiesUpdater;
    }

    public void fire(GameObject objectToAttack) {
        attackGoal(objectToAttack);
    }

    protected void attackGoal(GameObject attackedObject) {
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

        playSound(mFireSound, mSoundOperations);
        Bullet bullet = BulletPool.getInstance().obtainPoolItem();
        bullet.init(getBackgroundColor(), mObjectDamage, mBulletFixtureDef);

        setBulletFirePosition(attackedObject, bullet);
    }

    /**
     * To rotate unit to the target before fire. Abstract because movable units does need it and
     * immovable doesn't.
     */
    protected abstract void rotationBeforeFire(GameObject attackedObject);

    protected boolean isReloadFinished() {
        long time = System.currentTimeMillis();
        if (time - mLastAttackTime < mTimeForReload) {
            return false;
        }
        mLastAttackTime = time;
        return true;
    }

    /**
     * where the bullet will appear during the fire operation
     */
    protected abstract void setBulletFirePosition(GameObject attackedObject, Bullet bullet);

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        return mObjectSprite.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    public int getViewRadius() {
        return mViewRadius;
    }
}