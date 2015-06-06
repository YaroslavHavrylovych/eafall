package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IDestroyListener;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IHealthListener;

import org.andengine.audio.sound.Sound;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.math.MathConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * each visible element on the screen which can be assigned to on or other player and can take
 * participation in object collaboration extends this class (e.g. units, planets, sun etc)
 */
public abstract class GameObject extends BodiedSprite {
    public static final float VELOCITY_EPSILON = 0.00000001f;
    protected static final int sInvincibleObjectKey = Integer.MIN_VALUE;
    /** used for generation new id's */
    private static final AtomicLong sGameObjectsTracker = new AtomicLong(0);
    private static final String TAG = GameObject.class.getCanonicalName();
    /** maximum object health */
    protected int mObjectMaximumHealth = sInvincibleObjectKey;
    /** game object current health (it can be undestroyable) */
    protected int mObjectCurrentHealth = sInvincibleObjectKey;
    /** object health bar */
    protected HealthBar mHealthBar;
    /** object damage */
    protected Damage mObjectDamage;
    /** object armor */
    protected Armor mObjectArmor;
    /** callback to send message about death */
    protected volatile List<IDestroyListener> mObjectDestroyedListener = new ArrayList<IDestroyListener>(2);
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
        mUniqueId = sGameObjectsTracker.getAndIncrement();
        LoggerHelper.printInformationMessage(LoggerHelper.PERFORMANCE_TAG,
                "objects amount: " + sGameObjectsTracker.get());
    }

    public static void clearCounter() {
        sGameObjectsTracker.set(0);
    }

    public long getObjectUniqueId() {
        return mUniqueId;
    }

    public void setObjectUniqueId(long id) {
        mUniqueId = id;
    }

    public void setGameObjectHealthChangedListener(IHealthListener gameObjectHealthChangedListener) {
        mGameObjectHealthChangedListener = gameObjectHealthChangedListener;
    }

    public int getObjectStringId() {
        return mObjectStringId;
    }

    protected void setObjectStringId(int id) {
        mObjectStringId = id;
    }

    public void initHealth(int objectMaximumHealth) {
        initHealth(objectMaximumHealth, objectMaximumHealth);
    }

    protected void initHealth(int objectMaximumHealth, int objectCurrentHealth) {
        mObjectCurrentHealth = objectCurrentHealth;
        mObjectMaximumHealth = objectMaximumHealth;
    }

    protected void initHealthBar() {
        initChildren();
        mHealthBar = new HealthBar(getVertexBufferObjectManager(), getWidth());
        attachChild(mHealthBar.getHealthBarSprite());
    }

    protected void initChildren() {
        if (mChildren == null) {
            mChildren = new SmartList<BatchedSprite>(2);
        }
    }

    protected void playSound(Sound sound) {
        if (sound != null && sound.isLoaded()) {
            SoundFactory.getInstance().playSound(sound, getX(), getY());
        }
    }

    public boolean isObjectAlive() {
        return (mObjectCurrentHealth > 0 || mObjectCurrentHealth == sInvincibleObjectKey) && mPhysicBody != null;
    }

    public void addObjectDestroyedListener(final IDestroyListener objectDestroyedListener) {
        mObjectDestroyedListener.add(objectDestroyedListener);
    }

    public void damageObject(final Damage damage) {
        if (mObjectCurrentHealth == sInvincibleObjectKey) return;
        int objectHealth = mObjectCurrentHealth - mObjectArmor.getDamage(damage);

        setHealth(objectHealth);
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
            mGameObjectHealthChangedListener.gameObjectHealthChanged(mUniqueId, mObjectCurrentHealth);
        }
        if (mObjectCurrentHealth < 0) {
            onNegativeHealth();
            if (mObjectDestroyedListener != null) {
                for (IDestroyListener listener : mObjectDestroyedListener) {
                    listener.objectDestroyed(this);
                }
            }
        } else {
            if (mHealthBar != null) {
                mHealthBar.redrawHealthBar(mObjectMaximumHealth, mObjectCurrentHealth);
            }
        }
    }

    protected void onNegativeHealth() {
        LoggerHelper.methodInvocation(TAG, "onNegativeHealth");
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

    @Override
    public void setPosition(float pX, float pY) {
        super.setPosition(pX, pY);
        updateHealthBarPosition();
    }

    private void updateHealthBarPosition() {
        if (mHealthBar != null) {
            mHealthBar.setPosition(getX() - getWidth() / 2,
                    getY() + getHeight() / 2 + SizeConstants.HEALTH_BAR_HEIGHT);
        }
    }

    /** rotate all objects which hold current game object (and children) exclude health bar */
    public void rotate(float angleInDeg) {
        setRotation(angleInDeg);
    }

    public float getRotationAngle() {
        return getRotation();
    }

    /**
     * physic body will change rotation (in radiance) to point it's head to the target.
     *
     * @param x target abscissa coordinate
     * @param y target ordinate coordinate
     * @return angle value if current angle needs to be changed and null if physic body already in position
     */
    public float getDirection(float x, float y) {
        // next till the end will calculate angle
        float currentX = getX(),
                currentY = getY();

        return getDirection(currentX, currentY, x, y);
    }

    /**
     * return angle in radiance which is angle between abscissa and line from
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

    /** set physic body velocity */
    public void setUnitLinearVelocity(float x, float y) {
        Vector2 velocity = getBody().getLinearVelocity();
        if (Math.abs(velocity.x - x) < VELOCITY_EPSILON && Math.abs(velocity.y - y) < VELOCITY_EPSILON) {
            return;
        }

        mPhysicBody.setLinearVelocity(x, y);
    }
}
