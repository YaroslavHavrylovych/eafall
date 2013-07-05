package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects;

import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** Basic class for all dynamic game units */
public class SimpleUnit extends Sprite {
    /** tag, which is used for debugging purpose */
    public static final String TAG = SimpleUnit.class.getCanonicalName();
    /** physics body associated with current object {@link Sprite} */
    private Body mSimpleWarriorBody;
    /* main target abscissa and ordinate */
    private float mMainTargetX, mMainTargetY;
    /** max velocity for this unit */
    private float mMaxVelocity = 2.0f;
    /** update time for current object */
    private float mUpdateCycleTime = .5f;

    public SimpleUnit(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
        registerUpdateHandler(new TimerHandler(mUpdateCycleTime, true, new SimpleUnitTimerCallback()));
    }

    /**
     * set physics body associated with current {@link Sprite}
     *
     * @param body the physics body
     */
    public void setBody(Body body) {
        mSimpleWarriorBody = body;
    }

    /**
     * main target coordinates for current unit
     *
     * @param mainTargetX main target abscissa
     * @param mainTargetY main target ordinate
     */
    public void setMainTarget(float mainTargetX, float mainTargetY) {
        LoggerHelper.methodInvocation(TAG, "setMainTarget");
        mMainTargetX = mainTargetX;
        mMainTargetY = mainTargetY;
        Log.v(TAG, "mMainTargetX = " + mMainTargetX + ", mMainTargetY = " + mMainTargetY);
    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    /** used for update current object in game loop */
    protected class SimpleUnitTimerCallback implements ITimerCallback {
        @Override
        public void onTimePassed(TimerHandler pTimerHandler) {
            LoggerHelper.methodInvocation(TAG, "onTimePassed");
            float distanceX = mMainTargetX - getX(),
                    distanceY = mMainTargetY - getY();
            float absDistanceX = Math.abs(distanceX),
                    absDistanceY = Math.abs(distanceY),
                    maxAbsDistance = absDistanceX > absDistanceY ? absDistanceX : absDistanceY;
            float ordinateSpeed = mMaxVelocity * distanceX / maxAbsDistance,
                    abscissaSpeed = mMaxVelocity * distanceY / maxAbsDistance;

            Log.v(TAG, "xSpeed = " + ordinateSpeed + ", ySpeed = " + abscissaSpeed);
            final Vector2 velocity = Vector2Pool.obtain(ordinateSpeed, abscissaSpeed);
            mSimpleWarriorBody.setLinearVelocity(velocity);
            Vector2Pool.recycle(velocity);

        }
    }
}
