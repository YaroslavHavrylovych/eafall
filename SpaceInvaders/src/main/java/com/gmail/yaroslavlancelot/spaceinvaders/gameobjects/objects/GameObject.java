package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ISpriteTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ISpriteTouchable;
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
    /** unit sprite */
    protected Sprite mUnitSprite;
    /** background unit color */
    protected Rectangle mBackground;
    /** current object touch listener */
    private ISpriteTouchListener mSpriteOnTouchListener;

    protected GameObject(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion.getWidth(), textureRegion.getWidth(), vertexBufferObjectManager);
        setColor(Color.TRANSPARENT);
        mUnitSprite = new Sprite(0, 0, textureRegion, vertexBufferObjectManager);
        mBackground = new Rectangle(0, 0, 0, 0, vertexBufferObjectManager);
        attachChild(mBackground);
        attachChild(mUnitSprite);
    }

    protected static void loadResource(String pathToUnit, Context context, BitmapTextureAtlas textureAtlas, int x, int y) {
        TextureRegionHolderUtils.addElementFromAssets(pathToUnit, TextureRegionHolderUtils.getInstance(),
                textureAtlas, context, x, y);
    }

    @Override
    public void setOnTouchListener(ISpriteTouchListener spriteTouchListener) {
        mSpriteOnTouchListener = spriteTouchListener;
    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        return mSpriteOnTouchListener != null
                && mSpriteOnTouchListener.onTouch(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY)
                || super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    @Override
    public void setWidth(final float pWidth) {
        super.setWidth(pWidth);
        mUnitSprite.setWidth(pWidth);
    }

    @Override
    public void setHeight(final float pHeight) {
        super.setHeight(pHeight);
        mUnitSprite.setHeight(pHeight);
    }

    public Color getBackgroundColor() {
        return mBackground.getColor();
    }

    public void setBackgroundColor(Color color) {
        mBackground.setColor(color);
    }

    public void setBackgroundArea(Area area) {
        mBackground.setX(area.left);
        mBackground.setY(area.top);
        mBackground.setWidth(area.width);
        mBackground.setHeight(area.height);
    }
}
