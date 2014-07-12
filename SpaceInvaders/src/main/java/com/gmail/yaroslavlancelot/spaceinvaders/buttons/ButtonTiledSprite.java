package com.gmail.yaroslavlancelot.spaceinvaders.buttons;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ISpriteTouchable;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ITouchListener;

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
