package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.touch.ISpriteTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.touch.ISpriteTouchable;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** Basic class for all dynamic game units */
public class StaticObject extends Sprite implements ISpriteTouchable {
    /** tag, which is used for debugging purpose */
    public static final String TAG = StaticObject.class.getCanonicalName();
    /** current object touch listener */
    private ISpriteTouchListener mSpriteOnTouchListener;

    public StaticObject(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        LoggerHelper.methodInvocation(TAG, "onAreaTouched");
        return mSpriteOnTouchListener != null && mSpriteOnTouchListener.onTouch(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY) || super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    @Override
    public void setOnTouchListener(ISpriteTouchListener spriteTouchListener) {
        LoggerHelper.methodInvocation(TAG, "setOnTouchListener");
        mSpriteOnTouchListener = spriteTouchListener;
    }
}
