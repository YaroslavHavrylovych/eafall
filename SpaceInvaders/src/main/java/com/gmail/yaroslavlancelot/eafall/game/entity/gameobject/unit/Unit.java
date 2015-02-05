package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IFireListener;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.bullet.AttachBulletEvent;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.bullets.Bullet;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;

import org.andengine.audio.sound.Sound;
import org.andengine.input.touch.TouchEvent;

import de.greenrobot.event.EventBus;

/** base class for dynamic and static/unmovable objects which can attack other objects */
public abstract class Unit extends GameObject {
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
        Bullet bullet = new Bullet(getVertexBufferObjectManager(), getBackgroundColor(), mObjectDamage, mBulletFixtureDef);

        setBulletFirePosition(attackedObject, bullet);

        EventBus.getDefault().post(new AttachBulletEvent(bullet));
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

    public void removeDamage() {
        mObjectDamage.removeDamage();
    }

    public void setBulletFixtureDef(FixtureDef bulletFixtureDef) {
        mBulletFixtureDef = bulletFixtureDef;
    }

    public abstract BodyDef.BodyType getBodyType();
}