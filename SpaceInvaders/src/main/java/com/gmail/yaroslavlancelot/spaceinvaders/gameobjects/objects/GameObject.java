package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.IGameObjectHealthChanged;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.IObjectDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.IVelocityChangedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ISpriteTouchable;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ITouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.math.MathConstants;
import org.andengine.util.math.MathUtils;

import java.util.concurrent.atomic.AtomicLong;

/** all static and dynamic objects in the game have it in parent */
public abstract class GameObject extends IGameObject implements ISpriteTouchable {
    public static final float VELOCITY_EPSILON = 0.00000001f;
    protected static final int sUndestroyableObjectKey = Integer.MIN_VALUE;
    /** maximum object health */
    protected int mObjectMaximumHealth = sUndestroyableObjectKey;
    /** game object current health (it can be undestroyable) */
    protected int mObjectCurrentHealth = sUndestroyableObjectKey;
    /** used for generation new id's */
    private static volatile AtomicLong sGameObjectsTracker = new AtomicLong(0);
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
    protected IObjectDestroyedListener mObjectDestroyedListener;
    /** current object touch listener */
    private ITouchListener mSpriteOnTouchListener;
    /** id of the string in the string files to represent object */
    private int mObjectStringId;
    /** will trigger if object velocity changed */
    private IVelocityChangedListener mVelocityChangedListener;
    /** will trigger if object health changed */
    private IGameObjectHealthChanged mGameObjectHealthChangedListener;
    /** unique unit id */
    private long mUniqueId;
    /** */
    private Rectangle mBodyRectangle;
    /*
     * used for storing previous unit velocity. So we can compare and use in update loop. If velocity
     * not changed we will not set new velocity and will not trigger velocity changed listener
     * */
    private float prevVelocityX, prevVelocityY;

    protected GameObject(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion.getWidth(), textureRegion.getWidth(), vertexBufferObjectManager);
        mBodyRectangle = new Rectangle(0, 0, textureRegion.getWidth(), textureRegion.getWidth(), vertexBufferObjectManager);
        mBodyRectangle.setColor(Color.TRANSPARENT);
        setColor(Color.TRANSPARENT);
        attachChild(mBodyRectangle);
        mObjectSprite = new Sprite(0, 0, textureRegion, vertexBufferObjectManager);
        mBackground = new Rectangle(0, 0, 0, 0, vertexBufferObjectManager);
        mBodyRectangle.attachChild(mBackground);
        mBodyRectangle.attachChild(mObjectSprite);
        mUniqueId = sGameObjectsTracker.getAndIncrement();
    }

    protected static void loadResource(String pathToUnit, Context context, BitmapTextureAtlas textureAtlas, int x, int y) {
        TextureRegionHolderUtils.addElementFromAssets(pathToUnit, TextureRegionHolderUtils.getInstance(),
                textureAtlas, context, x, y);
    }

    /**
     * return angle in radiance which is angle between abscissa and line from
     * (startX, startY, x, y)
     */
    public static float getDirection(float startX, float startY, float x, float y) {
        float a = Math.abs(startX - x),
                b = Math.abs(startY - y);

        float newAngle = (float) Math.atan(b / a);

        if (startY < y) {
            if (startX > x) return 3 * MathConstants.PI / 2 - newAngle;
            else return newAngle + MathConstants.PI / 2;
        }

        if (startX > x) return newAngle + 3 * MathConstants.PI / 2;
        return MathConstants.PI / 2 - newAngle;
    }

    public long getObjectUniqueId() {
        return mUniqueId;
    }

    public void setObjectUniqueId(long id) {
        mUniqueId = id;
    }

    public void setGameObjectHealthChangedListener(IGameObjectHealthChanged gameObjectHealthChangedListener) {
        mGameObjectHealthChangedListener = gameObjectHealthChangedListener;
    }

    public void setVelocityChangedListener(IVelocityChangedListener velocityChangedListener) {
        mVelocityChangedListener = velocityChangedListener;
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

    public void setObjectDestroyedListener(final IObjectDestroyedListener objectDestroyedListener) {
        mObjectDestroyedListener = objectDestroyedListener;
    }

    @Override
    public void setOnTouchListener(ITouchListener spriteTouchListener) {
        mSpriteOnTouchListener = spriteTouchListener;
    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        return mSpriteOnTouchListener != null
                && mSpriteOnTouchListener.onTouch(pSceneTouchEvent)
                || super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
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
     * set new {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject} health.
     * Handles object destruction if health are less then 0.
     *
     * @param objectHealth new health value
     */
    public void setHealth(int objectHealth) {
        mObjectCurrentHealth = objectHealth;
        if (mGameObjectHealthChangedListener != null)
            mGameObjectHealthChangedListener.gameObjectHealthChanged(mUniqueId, mObjectCurrentHealth);
        if (mObjectCurrentHealth < 0) {
            if (mObjectDestroyedListener != null)
                mObjectDestroyedListener.objectDestroyed(this);
        } else {
            if (mHealthBar != null)
                mHealthBar.redrawHealthBar(mObjectMaximumHealth, mObjectCurrentHealth);
        }
    }

    public Color getBackgroundColor() {
        return mBackground.getColor();
    }

    public void setBackgroundColor(Color color) {
        mBackground.setColor(color);
    }

    public void setBackgroundArea() {
        setBackgroundArea(new Area(2.5f, 2.5f, SizeConstants.UNIT_TEAM_COLOR_INNER_SPRITE_SIZE,
                SizeConstants.UNIT_TEAM_COLOR_INNER_SPRITE_SIZE));
    }

    public void setBackgroundArea(Area area) {
        mBackground.setX(area.left);
        mBackground.setY(area.top);
        mBackground.setWidth(area.width);
        mBackground.setHeight(area.height);
    }

    public float getCenterX() {
        return getX() + getWidth() / 2;
    }

    public float getCenterY() {
        return getY() + getHeight() / 2;
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
        mBodyRectangle.setRotation(angleInDeg);
    }

    public float getRotationAngle() {
        return mBodyRectangle.getRotation();
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

    /** set physic body velocity */
    public void setUnitLinearVelocity(float x, float y) {
        if (Math.abs(prevVelocityX - x) < VELOCITY_EPSILON && Math.abs(prevVelocityY - y) < VELOCITY_EPSILON)
            return;
        else {
            prevVelocityX = x;
            prevVelocityY = y;
        }

        mPhysicBody.setLinearVelocity(x, y);
        if (mVelocityChangedListener != null)
            mVelocityChangedListener.velocityChanged(GameObject.this);
    }
}
