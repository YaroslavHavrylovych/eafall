package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.Area;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.bullets.Bullet;
import com.gmail.yaroslavlancelot.eafall.game.entity.bullets.BulletPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.ITeamObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IFireListener;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.AttachSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.CreatePhysicBodyEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.RunOnUpdateThreadEvent;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;

import org.andengine.audio.sound.Sound;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import de.greenrobot.event.EventBus;

/** base class for dynamic and static/unmovable objects which can attack other objects */
public abstract class Unit extends GameObject implements
        ITeamObject,
        RunOnUpdateThreadEvent.UpdateThreadRunnable {
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
    /** if fireFromPosition method called it will be triggered */
    protected IFireListener mUnitFireCallback;
    /** unit shout sound */
    protected Sound mFireSound;
    /** fixture def for bullets created by this unit */
    private FixtureDef mBulletFixtureDef;
    /** unit team name */
    private volatile String mTeamName;
    /** hold position of current unit team color region (relative the unit) */
    private Area mTeamColorArea;
    /** unit team color */
    private BatchedSprite mTeamColorAreaSprite;

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
        if (Config.getConfig().isTeamColorAreaEnabled()) {
            mTeamColorArea = unitBuilder.getTeamColorArea();
        }
    }

    public void setReloadTime(double seconds) {
        mTimeForReload = seconds * 1000;
    }

    @Override
    public void updateThreadCallback() {
        getBody().setTransform(-100, -100, 0);
        getBody().setActive(false);
        onUnitDestroyed();
    }

    protected void onUnitDestroyed() {
    }

    /**
     * Init unit after creation. You need manually trigger this method after constructor at the time
     * when you want to init and attach this (totally working)  unit
     * WARNING: unit team name have to be assigned before init() trigger
     */
    public void init(float x, float y, String teamName) {
        LoggerHelper.methodInvocation(TAG, "init(float, float, String)");
        setTeam(teamName);
        setSpriteGroupName(BatchingKeys.getUnitSpriteGroup(teamName));
        ITeam team = TeamsHolder.getTeam(mTeamName);
        initTeamColorArea(team);
        setHealth(mObjectMaximumHealth);
        initHealthBar();

        boolean existingUnit;
        float posX = x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                posY = y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                angle = 0f;
        if (getBody() == null) {
            existingUnit = false;
            EventBus.getDefault().post(new CreatePhysicBodyEvent(this, getBodyType(),
                    team.getFixtureDefUnit(), posX, posY, angle));
        } else {
            getBody().setActive(true);
            getBody().setTransform(posX, posY, angle);
            existingUnit = true;
        }

        setBulletFixtureDef(CollisionCategories.getBulletFixtureDefByUnitCategory(
                team.getFixtureDefUnit().filter.categoryBits));

        if (team.getControlType() == TeamControlBehaviourType.REMOTE_CONTROL_ON_CLIENT_SIDE) {
            removeDamage();
        }

        if (!existingUnit) {
            EventBus.getDefault().post(new AttachSpriteEvent(this));
        }
        team.addObjectToTeam(this);
    }

    private void initTeamColorArea(ITeam team) {
        if (Config.getConfig().isTeamColorAreaEnabled()) {
            if (mTeamColorAreaSprite == null) {
                initChildren();
                mTeamColorAreaSprite = new BatchedSprite(
                        getX() + mTeamColorArea.left, getY() + mTeamColorArea.top,
                        mTeamColorArea.width, mTeamColorArea.height,
                        TextureRegionHolder.getRegion(StringConstants.FILE_HEALTH_BAR),
                        getVertexBufferObjectManager());
                mTeamColorAreaSprite.setSpriteGroupName(BatchingKeys.BULLET_HEALTH_TEAM_COLOR);
                attachChild(mTeamColorAreaSprite);
            }
            mTeamColorAreaSprite.setColor(team.getColor());
        }
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
        if (mTeamName != null) {
            TeamsHolder.getTeam(mTeamName).removeObjectFromTeam(this);
        }
        EventBus.getDefault().post(this);
    }

    @Override
    public void setPosition(float pX, float pY) {
        super.setPosition(pX, pY);
        updateTeamColorPosition();
    }

    @Override
    public void rotate(float angleInDeg) {
        super.rotate(angleInDeg);
        updateTeamColorRotation(angleInDeg);
    }

    private void updateTeamColorRotation(float angleInDeg) {
        if (mTeamColorArea == null || mTeamColorAreaSprite == null) {
            return;
        }
        mTeamColorAreaSprite.setRotation(angleInDeg);
    }

    private void updateTeamColorPosition() {
        if (mTeamColorAreaSprite == null || mTeamColorArea == null) {
            return;
        }
        mTeamColorAreaSprite.setPosition(getX() + mTeamColorArea.left, getY() + mTeamColorArea.top);
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

        playSound(mFireSound);
        Bullet bullet = BulletPool.getInstance().obtainPoolItem();
        bullet.init(mObjectDamage, mBulletFixtureDef);

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

    public int getViewRadius() {
        return mViewRadius;
    }

    @Override
    public String getTeam() {
        return mTeamName;
    }

    @Override
    public void setTeam(String teamName) {
        mTeamName = teamName;
    }
}