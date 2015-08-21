package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path.PathHelper;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

/** Basic class for all stationary/unmovable game units ( */
public class StationaryUnit extends Unit {
    public static final String TAG = StationaryUnit.class.getCanonicalName();
    /** body type */
    private static final BodyDef.BodyType sBodyType = BodyDef.BodyType.StaticBody;

    /** create unit from appropriate builder */
    public StationaryUnit(StationaryUnitBuilder unitBuilder) {
        super(unitBuilder);
        // todo: maybe it's better to use more functional style here, like
        // updateCycleTime() method which will return specific value, class field depends on lifecycle,
        // better to be stateless here
        mUpdateCycleTime = .7f;
    }

    @Override
    public BodyDef.BodyType getBodyType() {
        return sBodyType;
    }

    public void startLifecycle() {
        registerUpdateHandler(new TimerHandler(mUpdateCycleTime, true, new StationaryUnitTimerCallback()));
    }

    @Override
    protected boolean needRotation(int angle) {
        return false;
    }

    @Override
    protected int getAngle(float x, float y) {
        return 0;
    }

    @Override
    protected void rotateWithAngle(int angle) {
        throw new UnsupportedOperationException("can't rotate stationary unit");
    }

    /** stationary unit behaviour */
    protected class StationaryUnitTimerCallback implements ITimerCallback {
        @Override
        public void onTimePassed(TimerHandler pTimerHandler) {
            // check for anything to attack
            if (mObjectToAttack != null && mObjectToAttack.isObjectAlive()
                    &&
                    PathHelper.getDistanceBetweenPoints(
                            getX(), getY(), mObjectToAttack.getX(), mObjectToAttack.getY())
                            < mAttackRadius) {
                attackTarget(mObjectToAttack);
                return;
            } else {
                // we can't reach previously attacked unit
                mObjectToAttack = null;
            }

            // search for new unit to attack
            mObjectToAttack = mEnemiesUpdater
                    .getFirstEnemyInRange(StationaryUnit.this, mAttackRadius);
        }
    }
}