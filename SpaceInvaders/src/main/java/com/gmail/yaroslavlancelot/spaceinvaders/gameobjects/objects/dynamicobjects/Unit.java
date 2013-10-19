package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.IObjectDestroyedListener;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitPathUtil;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.util.List;

/** Basic class for all dynamic game units */
public abstract class Unit extends Rectangle {
    /** physics body associated with current object {@link Sprite} */
    protected Body mSimpleWarriorBody;
    /** max velocity for this unit */
    protected float mMaxVelocity = 2.0f;
    /** update time for current object */
    protected float mUpdateCycleTime = .5f;
    /** unit health */
    protected int mUnitHealth = 100;
    /** unit damage */
    protected int mDamage = 15;
    /** attack radius of current unit */
    protected int mAttackRadius = 100;
    /** area in which unit can search for enemies */
    protected int mViewRadius = 150;
    /** currently attacked unit */
    protected Unit mUnitToAttack;
    /** callback for using to update unit visible enemies */
    protected ISimpleUnitEnemiesUpdater mEnemiesUpdater;
    /** callback to send message about unit death */
    protected IObjectDestroyedListener mObjectDestroyedListener;
    /** unit path */
    protected UnitPathUtil.UnitPath mUnitPath;
    /** unit sprite */
    protected Sprite mUnitSprite;
    /** background unit color */
    protected Rectangle mBackground;

    protected Unit(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion.getWidth(), textureRegion.getWidth(), vertexBufferObjectManager);
        setColor(Color.TRANSPARENT);
        mUnitSprite = new Sprite(x, y, textureRegion, vertexBufferObjectManager);
        mBackground = new Rectangle(0, 0, 0, 0, vertexBufferObjectManager);
        attachChild(mBackground);
        registerUpdateHandler(new TimerHandler(mUpdateCycleTime, true, new SimpleUnitTimerCallback()));
        attachChild(mUnitSprite);

    }

    public void calculateUnitPath() {
        mUnitPath = UnitPathUtil.getUnitPathAccordingToStartAbscissa(getX());
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

    public abstract void hitUnit(int hitPower);

    /**
     * set physics body associated with current {@link Sprite}
     *
     * @param body the physics body
     */
    public void setBody(Body body) {
        mSimpleWarriorBody = body;
    }

    public void setEnemiesUpdater(final ISimpleUnitEnemiesUpdater enemiesUpdater) {
        mEnemiesUpdater = enemiesUpdater;
    }

    public boolean isUnitAlive() {
        return mUnitHealth > 0;
    }

    public void setObjectDestroyedListener(final IObjectDestroyedListener objectDestroyedListener) {
        mObjectDestroyedListener = objectDestroyedListener;
    }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        return mUnitSprite.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    public int getViewRadius() {
        return mViewRadius;
    }

    protected abstract void attackGoal();

    public void setBackgroundArea(Area area) {
        mBackground.setX(area.left);
        mBackground.setY(area.top);
        mBackground.setWidth(area.width);
        mBackground.setHeight(area.height);
    }

    public void setBackgroundColor(Color color) {
        mBackground.setColor(color);
    }

    /** used for update current object in game loop */
    protected class SimpleUnitTimerCallback implements ITimerCallback {
        private float[] mTwoDimensionFloatArray = new float[2];

        @Override
        public void onTimePassed(TimerHandler pTimerHandler) {
            // check for units to attack
            if (mUnitToAttack != null && mUnitToAttack.isUnitAlive() &&
                    UnitPathUtil.getDistanceBetweenPoints(getX(), getY(),
                            mUnitToAttack.getX(), mUnitToAttack.getY()) < mViewRadius) {
                attackOrMove();
                return;
            } else {
                // we don't see previously attacked unit
                mUnitToAttack = null;
            }

            // search for new unit to attack
            if (mEnemiesUpdater != null) {
                List<Unit> units = mEnemiesUpdater.getEnemies(Unit.this);
                if (units != null && !units.isEmpty()) {
                    mUnitToAttack = units.get(0);
                    attackOrMove();
                    return;
                }
            }

            // move by path, without attack other units
            mTwoDimensionFloatArray[0] = getX();
            mTwoDimensionFloatArray[1] = getY();
            mUnitPath.getNextPathPoint(mTwoDimensionFloatArray, mTwoDimensionFloatArray);

            moveToPoint(mTwoDimensionFloatArray[0], mTwoDimensionFloatArray[1]);
        }

        private void attackOrMove() {
            // check if we already can attack
            if (UnitPathUtil.getDistanceBetweenPoints(
                    getX(), getY(), mUnitToAttack.getX(), mUnitToAttack.getY()) < mAttackRadius) {
                attackGoal();
                // stay on position
                setUnitLinearVelocity(0, 0);
            } else
                // pursuit attacked unit
                moveToPoint(mUnitToAttack.getX(), mUnitToAttack.getY());
        }

        private void moveToPoint(float x, float y) {
            float distanceX = x - getX(),
                    distanceY = y - getY();
            float absDistanceX = Math.abs(distanceX),
                    absDistanceY = Math.abs(distanceY),
                    maxAbsDistance = absDistanceX > absDistanceY ? absDistanceX : absDistanceY;
            float ordinateSpeed = mMaxVelocity * distanceX / maxAbsDistance,
                    abscissaSpeed = mMaxVelocity * distanceY / maxAbsDistance;

            setUnitLinearVelocity(ordinateSpeed, abscissaSpeed);
        }

        private void setUnitLinearVelocity(float x, float y) {
            final Vector2 velocity = Vector2Pool.obtain(x, y);
            mSimpleWarriorBody.setLinearVelocity(velocity);
            Vector2Pool.recycle(velocity);
        }
    }
}