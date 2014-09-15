package com.gmail.yaroslavlancelot.spaceinvaders.visualelements.sprite;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ISpriteTouchable;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ITouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Extend {@link org.andengine.entity.sprite.Sprite}
 * to implement {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ISpriteTouchable}.
 * Custom touch listener can be used with this Sprite.
 */
public class TouchableSprite extends Sprite implements ISpriteTouchable {
    /** current object touch listener */
    protected ITouchListener mSpriteOnTouchListener;

    public TouchableSprite(float pX, float pY, float width, float height, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, width, height, pTextureRegion, pVertexBufferObjectManager);
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

    /** invoke {@code detachSelf} method with unregister touch even from scene */
    public boolean detachSelf(Scene scene) {
        scene.unregisterTouchArea(this);
        return super.detachSelf();
    }
}
