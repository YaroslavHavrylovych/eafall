package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.units;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class Bullet extends Sprite {
    /** max velocity for this unit */
    protected float mMaxVelocity = 2.0f;
    /** update time for current object */
    protected float mUpdateCycleTime = .1f;
    /** unit damage */
    protected int mDamage = 15;
    /** unit to attack */
    protected Unit mUnitToAttack;

    public Bullet(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
        registerUpdateHandler(new TimerHandler(mUpdateCycleTime, true, new BulletTimerCallback()));
    }

    /** used for update current object in game loop */
    protected class BulletTimerCallback implements ITimerCallback {
        private float[] mTwoDimensionFloatArray = new float[2];

        @Override
        public void onTimePassed(TimerHandler pTimerHandler) {
        }
    }
}
