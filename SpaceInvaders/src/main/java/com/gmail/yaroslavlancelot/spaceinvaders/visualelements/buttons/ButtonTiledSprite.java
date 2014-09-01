package com.gmail.yaroslavlancelot.spaceinvaders.visualelements.buttons;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ISpriteTouchable;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ITouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** contains base logic for creating buttons */
public abstract class ButtonTiledSprite extends TiledSprite implements ISpriteTouchable {
    /** current object touch listener */
    protected ITouchListener mSpriteOnTouchListener;

    public ButtonTiledSprite(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    protected ButtonTiledSprite(float pX, float pY, float pWidth, float pHeight, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public Area getTouchArea() {
        return getTouchArea(0, 0);
    }

    public Area getTouchArea(float offsetX, float offsetY) {
        float x = offsetX, y = offsetY;
        IEntity entity = this;
        while (entity != null) {
            x += entity.getX();
            y += entity.getY();
            entity = entity.getParent();
        }
        return new Area(x, y, getWidth(), getHeight());
    }

    public void press() {
        setCurrentTileIndex(1);
    }

    public void unpress() {
        setCurrentTileIndex(0);
    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        return (mSpriteOnTouchListener != null && mSpriteOnTouchListener.onTouch(pSceneTouchEvent))
                || super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    @Override
    public void setOnTouchListener(ITouchListener spriteTouchListener) {
        mSpriteOnTouchListener = spriteTouchListener;
    }
}
