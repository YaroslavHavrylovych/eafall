package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.entity.RectangleWithBody;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IDestroyListener;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.IHealthListener;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.Area;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.math.MathConstants;
import org.andengine.util.math.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * each visible element on the screen which can be assigned to on or other team and can take
 * participation in object collaboration extends this class (e.g. units, planets, sun etc)
 */
public abstract class GameObject extends RectangleWithBody {
    public static final float VELOCITY_EPSILON = 0.00000001f;
    protected static final int sUndestroyableObjectKey = Integer.MIN_VALUE;
    /** maximum object health */
    protected int mObjectMaximumHealth = sUndestroyableObjectKey;
    /** game object current health (it can be undestroyable) */
    protected int mObjectCurrentHealth = sUndestroyableObjectKey;
    /** used for generation new id's */
    private static final AtomicLong sGameObjectsTracker = new AtomicLong(0);
    /** object sprite */
    protected Sprite mObjectSprite;
    /** background color */
    protected Rectangle mBackground;
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
        super(x, y, width, height, vertexBufferObjectManager);
        float halfX = getWidth() / 2,
                halfY = getHeight() / 2;
        setColor(Color.TRANSPARENT);
        mObjectSprite = new Sprite(halfX, halfY, textureRegion, vertexBufferObjectManager);
        mBackground = new Rectangle(halfX, halfY, 0, 0, vertexBufferObjectManager);
        attachChild(mBackground);
        attachChild(mObjectSprite);
        mUniqueId = sGameObjectsTracker.getAndIncrement();
    }

    public static void clearCounter() {
        sGameObjectsTracker.set(0);
    }

    public static void loadResource(String pathToImage, Context context, BitmapTextureAtlas textureAtlas, int x, int y) {
        TextureRegionHolder.addElementFromAssets(pathToImage, textureAtlas, context, x, y);
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

    protected void initHealth(int objectMaximumHealth) {
        initHealth(objectMaximumHealth, objectMaximumHealth);
    }

    protected void initHealth(int objectMaximumHealth, int objectCurrentHealth) {
        mObjectCurrentHealth = objectCurrentHealth;
        mObjectMaximumHealth = objectMaximumHealth;
        initHealthBar();
    }

    protected void initHealthBar() {
        mHealthBar = new HealthBar(getVertexBufferObjectManager(), getWidth());
        mHealthBar.setPosition(getWidth() / 2, getHeight() + HealthBar.HEALTH_BAR_HEIGHT);
        attachChild(mHealthBar.getHealthBar());
    }

    protected void playSound(Sound sound, SoundOperations soundOperations) {
        if (sound == null || !sound.isLoaded())
            return;

        soundOperations.playSoundDependingFromPosition(sound, getX(), getY());
    }

    public boolean isObjectAlive() {
        return (mObjectCurrentHealth > 0 || mObjectCurrentHealth == sUndestroyableObjectKey) && mPhysicBody != null;
    }

    public void addObjectDestroyedListener(final IDestroyListener objectDestroyedListener) {
        mObjectDestroyedListener.add(objectDestroyedListener);
    }

    @Override
    public void setWidth(final float pWidth) {
        super.setWidth(pWidth);
        float multiplier = pWidth / mObjectSprite.getWidth();
        mBackground.setX(multiplier * mBackground.getX());
        mBackground.setWidth(multiplier * mBackground.getWidth());
        mObjectSprite.setWidth(pWidth);
    }

    @Override
    public void setHeight(final float pHeight) {
        super.setHeight(pHeight);
        float multiplier = pHeight / mObjectSprite.getHeight();
        mBackground.setY(multiplier * mBackground.getY());
        mBackground.setHeight(multiplier * mBackground.getHeight());
        mObjectSprite.setHeight(pHeight);
    }

    public void damageObject(final Damage damage) {
        if (mObjectCurrentHealth == sUndestroyableObjectKey) return;
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
            if (mObjectDestroyedListener != null) {
                onNegativeHealth();
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
    }

    public Color getBackgroundColor() {
        return mBackground.getColor();
    }

    public void setBackgroundColor(Color color) {
        mBackground.setColor(color);
    }

    public void setBackgroundArea() {
        setBackgroundArea(Area.getArea(2.5f, 2.5f, Sizes.UNIT_TEAM_COLOR_INNER_SPRITE_SIZE,
                Sizes.UNIT_TEAM_COLOR_INNER_SPRITE_SIZE));
    }

    public void setBackgroundArea(Area area) {
        mBackground.setX(area.left);
        mBackground.setY(area.top);
        mBackground.setWidth(area.width);
        mBackground.setHeight(area.height);
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

    /** used to manually set physic body position */
    public void setUnitPosition(float x, float y) {
        mPhysicBody.setTransform(x, y, mPhysicBody.getAngle());
    }

    /** rotate all objects which hold current game object (and children) exclude health bar */
    public void rotate(float angleInDeg) {
        mObjectSprite.setRotation(angleInDeg);
    }

    public float getRotationAngle() {
        return mObjectSprite.getRotation();
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
