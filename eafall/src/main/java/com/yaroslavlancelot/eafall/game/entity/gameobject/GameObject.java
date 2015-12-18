package com.yaroslavlancelot.eafall.game.entity.gameobject;

import com.yaroslavlancelot.eafall.android.LoggerHelper;
import com.yaroslavlancelot.eafall.game.audio.LimitedSoundWrapper;
import com.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.BodiedSprite;
import com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IDestroyListener;
import com.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IHealthListener;
import com.yaroslavlancelot.eafall.game.entity.health.IHealthBar;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.math.MathConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Each visible element on the screen which can be assigned to on or other player and can take
 * participation in object collaboration extends this class (e.g. units, planets, sun etc)
 *
 * @author Yaroslav Havrylovych
 */
public abstract class GameObject extends BodiedSprite {
    protected static final int sInvincibleObjectKey = Integer.MIN_VALUE;
    /** used for generation new id's */
    private static final AtomicLong sGameObjectsTracker = new AtomicLong(0);
    /** maximum object health */
    protected int mObjectMaximumHealth = sInvincibleObjectKey;
    /** game object current health (it can't be destroyed) */
    protected int mObjectCurrentHealth = sInvincibleObjectKey;
    /** object health bar */
    protected IHealthBar mHealthBar;
    /** object damage */
    protected Damage mObjectDamage;
    /** object armor */
    protected Armor mObjectArmor;
    /** callback to send message about death */
    protected volatile List<IDestroyListener> mObjectDestroyedListener = new ArrayList<>(2);
    /** id of the string in the string files to represent object */
    private int mObjectStringId;
    /** will trigger if object health changed */
    private IHealthListener mGameObjectHealthChangedListener;
    /** unique unit id */
    private long mUniqueId;

    protected GameObject(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        this(x, y, textureRegion.getWidth(), textureRegion.getWidth(), textureRegion, vertexBufferObjectManager);
    }

    protected GameObject(float x, float y, float width, float height, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, width, height, textureRegion, vertexBufferObjectManager);
        LoggerHelper.printInformationMessage(LoggerHelper.PERFORMANCE_TAG,
                "objects amount: " + sGameObjectsTracker.get());
    }

    public long getObjectUniqueId() {
        return mUniqueId;
    }

    public void setObjectUniqueId(long id) {
        mUniqueId = id;
    }

    public int getObjectStringId() {
        return mObjectStringId;
    }

    protected void setObjectStringId(int id) {
        mObjectStringId = id;
    }

    public boolean isObjectAlive() {
        return checkHealth(mObjectCurrentHealth) && mPhysicBody != null;
    }

    public Armor getObjectArmor() {
        return mObjectArmor;
    }

    public Damage getObjectDamage() {
        return mObjectDamage;
    }

    public int getObjectCurrentHealth() {
        return mObjectCurrentHealth;
    }

    public int getMaximumObjectHealth() {
        return mObjectMaximumHealth;
    }

    public void setGameObjectHealthChangedListener(IHealthListener gameObjectHealthChangedListener) {
        mGameObjectHealthChangedListener = gameObjectHealthChangedListener;
    }

    /**
     * set new {@link GameObject} health.
     * Handles object destruction if health are less then 0.
     *
     * @param objectHealth new health value
     */
    public void setHealth(int objectHealth) {
        mObjectCurrentHealth = objectHealth;
        if (mGameObjectHealthChangedListener != null) {
            mGameObjectHealthChangedListener.gameObjectHealthChanged(mUniqueId, objectHealth);
        }
        if (!checkHealth(objectHealth)) {
            onNegativeHealth();
            if (mObjectDestroyedListener != null) {
                for (IDestroyListener listener : mObjectDestroyedListener) {
                    listener.objectDestroyed(this);
                }
            }
        } else {
            updateHealthBar();
        }
    }

    @Override
    public void setPosition(float pX, float pY) {
        super.setPosition(pX, pY);
        updateHealthBarPosition();
    }

    protected abstract void onNegativeHealth();

    /** if object isn't invincible it will be killed in a moment by setting it's health to 0 */
    public void kill() {
        if (mObjectCurrentHealth != sInvincibleObjectKey) {
            setHealth(0);
        }
    }

    protected void updateHealthBar() {
        if (mHealthBar != null) {
            mHealthBar.redrawHealthBar(mObjectMaximumHealth, mObjectCurrentHealth);
        }
    }

    public void initHealth(int objectMaximumHealth) {
        mUniqueId = sGameObjectsTracker.getAndIncrement();
        initHealth(objectMaximumHealth, objectMaximumHealth);
    }

    protected void initHealth(int objectMaximumHealth, int objectCurrentHealth) {
        mObjectCurrentHealth = objectCurrentHealth;
        mObjectMaximumHealth = objectMaximumHealth;
    }

    /**
     * creates the new health bar if the game object doesn't have one and attach it
     * <p/>
     * P.S. only if {@link #createHealthBar} will return not null
     */
    protected void initHealthBar() {
        if (mHealthBar == null) {
            mHealthBar = createHealthBar();
            if (mHealthBar != null) {
                initChildren();
                mHealthBar.addToParent(this);
            }
        }
    }

    protected abstract IHealthBar createHealthBar();

    protected void initChildren() {
        if (mChildren == null) {
            mChildren = new SmartList<>(2);
        }
    }

    protected void playSound(LimitedSoundWrapper sound) {
        if (sound != null && sound.isLoaded()) {
            SoundFactory.getInstance().playSound(sound, mX, mY);
        }
    }

    public void addObjectDestroyedListener(final IDestroyListener objectDestroyedListener) {
        mObjectDestroyedListener.add(objectDestroyedListener);
    }

    public void damageObject(final Damage damage) {
        if (checkHealth(mObjectCurrentHealth)) {
            setHealth(mObjectCurrentHealth - mObjectArmor.getDamage(damage));
        }
    }

    public abstract void destroy();

    protected void updateHealthBarPosition() {
        if (mHealthBar != null && mHealthBar.isVisible()) {
            mHealthBar.setPosition(
                    mX - mWidth / 2,
                    mY + mHeight / 2 + SizeConstants.UNIT_HEALTH_BAR_HEIGHT
            );
        }
    }

    /** set physic body velocity */
    public void setUnitLinearVelocity(float x, float y) {
        mPhysicBody.setLinearVelocity(x, y);
    }

    /**
     * Check vitality based on health
     *
     * @param health given health value
     * @return true if health value more or equal zero or if health value is the key of
     * undestroyable object.
     * <br/>
     * returns false if health value negative and health value is not the value of undestroyable object
     */
    protected static boolean checkHealth(int health) {
        return health > 0 || health == sInvincibleObjectKey;
    }

    public static void clearCounter() {
        sGameObjectsTracker.set(0);
    }

    /**
     * return angle in radiance which is angle between ordinate and line from
     * (startX, startY, x, y)
     */
    public static float getDirection(float startX, float startY, float x, float y) {
        float a = Math.abs(startX - x),
                b = Math.abs(startY - y);

        float newAngle = (float) Math.atan(b / a);

        if (startY > y) {
            if (startX > x) return 3 * MathConstants.PI / 2 - newAngle;
            else return newAngle + MathConstants.PI / 2;
        }

        if (startX > x) return newAngle + 3 * MathConstants.PI / 2;
        return MathConstants.PI / 2 - newAngle;
    }
}
