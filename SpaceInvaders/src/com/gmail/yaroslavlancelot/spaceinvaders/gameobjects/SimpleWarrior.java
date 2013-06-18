package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects;

import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class SimpleWarrior extends Sprite {
    public static final String TAG = SimpleWarrior.class.getCanonicalName();
    private Body mSimpleWarriorBody;
    private float centerY, centerX;
    private float mMainTargetX, mMainTargetY;
    private float mMaxVelocity = 2.0f;

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

    public void setMainTarget(float mainTargetX, float mainTargetY) {
        mMainTargetX = mainTargetX;
        mMainTargetY = mainTargetY;

        float sum = mainTargetX + mainTargetY;
        float x = mMaxVelocity * (mainTargetX - getX()) / sum,
                y = mMaxVelocity * (mainTargetY - getY()) / sum;

        Log.d(TAG, "x = " + x + ", y = " + y);
        final Vector2 velocity = Vector2Pool.obtain(x, y);
        mSimpleWarriorBody.setLinearVelocity(velocity);
        Vector2Pool.recycle(velocity);
    }

//    public void setMaxVelocity(float maxVelocityX, float maxVelocityY) {
//        mMaxVelocityX = maxVelocityX;
//        mMaxVelocityY = maxVelocityY;
//    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }
}
