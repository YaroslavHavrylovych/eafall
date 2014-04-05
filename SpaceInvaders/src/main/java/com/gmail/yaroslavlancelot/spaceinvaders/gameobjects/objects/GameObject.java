package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.Body;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
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

public abstract class GameObject extends Rectangle implements ISpriteTouchable {
    protected static final int sUndestroyableObjectKey = Integer.MIN_VALUE;
    /** maximum object health */
    protected int mObjectMaximumHealth = sUndestroyableObjectKey;
    /** game object current health (it can be undestroyable) */
    protected int mObjectCurrentHealth = sUndestroyableObjectKey;
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
    /** physics body associated with current object {@link Sprite} */
    protected Body mPhysicBody;
    /** current object touch listener */
    private ITouchListener mSpriteOnTouchListener;
    /** id of the string in the string files to represent object */
    private int mObjectStringId;
    /** will trigger if object velocity changed */
    private IVelocityChangedListener mVelocityChangedListener;

    protected GameObject(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion.getWidth(), textureRegion.getWidth(), vertexBufferObjectManager);
        setColor(Color.TRANSPARENT);
        mObjectSprite = new Sprite(0, 0, textureRegion, vertexBufferObjectManager);
        mBackground = new Rectangle(0, 0, 0, 0, vertexBufferObjectManager);
        attachChild(mBackground);
        attachChild(mObjectSprite);
    }

    protected static void loadResource(String pathToUnit, Context context, BitmapTextureAtlas textureAtlas, int x, int y) {
        TextureRegionHolderUtils.addElementFromAssets(pathToUnit, TextureRegionHolderUtils.getInstance(),
                textureAtlas, context, x, y);
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
        return mObjectCurrentHealth > 0 || mObjectCurrentHealth == sUndestroyableObjectKey;
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
        mObjectCurrentHealth -= mObjectArmor.getDamage(damage);
        if (mObjectCurrentHealth < 0) {
            if (mObjectDestroyedListener != null)
                mObjectDestroyedListener.objectDestroyed(this);
            return;
        }
        if (mHealthBar != null)
            mHealthBar.redrawHealthBar(mObjectMaximumHealth, mObjectCurrentHealth);
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

    public Body getBody() {
        return mPhysicBody;
    }

    /**
     * set physics body associated with current {@link Sprite}
     *
     * @param body the physics body
     */
    public void setBody(Body body) {
        mPhysicBody = body;
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

    public void setBodyTransform(float x, float y, float... angle) {
        float defaultAngle;
        if (angle.length > 0) {
            defaultAngle = angle[0];
        } else
            defaultAngle = mPhysicBody.getAngle();
        mPhysicBody.setTransform(x, y, defaultAngle);
    }

    public void setUnitLinearVelocity(float x, float y) {
        mPhysicBody.setLinearVelocity(x, y);
        if (mVelocityChangedListener != null)
            mVelocityChangedListener.velocityChanged(GameObject.this);
    }
}
