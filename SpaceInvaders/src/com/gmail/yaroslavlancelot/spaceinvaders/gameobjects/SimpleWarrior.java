package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects;

import com.badlogic.gdx.physics.box2d.Body;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class SimpleWarrior extends Sprite {
    private Body mSimpleWarriorBody;
    private float centerY, centerX;
    private float mMainTargetX, mMainTargetY;
    private float mMaxVelocityX, mMaxVelocityY;

    public SimpleWarrior(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
        centerX = x + getWidth() / 2;
        centerY = y + getHeight() / 2;
    }

    public void setBody(Body body) {
        mSimpleWarriorBody = body;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setmMainTargetX(float mainTargetX, float mainTargetY) {
        mMainTargetX = mainTargetX;
        mMainTargetY = mainTargetY;
    }

    public void setmMaxVelocity(float maxVelocityX, float maxVelocityY) {
        mMaxVelocityX = maxVelocityX;
        mMaxVelocityY = maxVelocityY;
    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }
}
