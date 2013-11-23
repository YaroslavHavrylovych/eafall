package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects;

import android.content.Context;
import com.badlogic.gdx.physics.box2d.Body;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks.IObjectDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ISpriteTouchable;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ITouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public abstract class GameObject extends Rectangle implements ISpriteTouchable {
    protected static final int sUndestroyableObjectKey = Integer.MIN_VALUE;
    /** object sprite */
    protected Sprite mObjectSprite;
    /** background color */
    protected Rectangle mBackground;
    /** game object health (it can be undestroyable) */
    protected int mObjectHealth = sUndestroyableObjectKey;
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
    /** */
    private int mObjectStringId;

    public int getObjectStringId() {
        return mObjectStringId;
    }

    protected void setObjectStringId(int id) {
        mObjectStringId = id;
    }

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

    public boolean isObjectAlive() {
        return mObjectHealth > 0 || mObjectHealth == sUndestroyableObjectKey;
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
        if (mObjectHealth == sUndestroyableObjectKey) return;
        mObjectHealth -= mObjectArmor.getDamage(damage);
        if (mObjectHealth < 0) {
            if (mObjectDestroyedListener != null)
                mObjectDestroyedListener.objectDestroyed(this);
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

    public int getObjectHealth() {
        return mObjectHealth;
    }
}
