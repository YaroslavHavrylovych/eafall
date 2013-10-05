package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ISpriteTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ISpriteTouchable;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** Basic class for all dynamic units in the game */
public abstract class StaticObject extends Sprite implements ISpriteTouchable {
    /** amount of money that brings current object */
    protected int mIncomeIncreasingValue;
    /** how much this object is cost */
    protected int mCost;
    /** current object touch listener */
    private ISpriteTouchListener mSpriteOnTouchListener;

    public StaticObject(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        return mSpriteOnTouchListener != null
                && mSpriteOnTouchListener.onTouch(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY)
                || super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    @Override
    public void setOnTouchListener(ISpriteTouchListener spriteTouchListener) {
        mSpriteOnTouchListener = spriteTouchListener;
    }

    public int getObjectIncomeIncreasingValue() {
        return mIncomeIncreasingValue;
    }

    @SuppressWarnings("unused")
    public int getObjectCost() {
        return mCost;
    }
}
