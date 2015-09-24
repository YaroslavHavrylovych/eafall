package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.defence;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gmail.yaroslavlancelot.eafall.game.engine.ManualFinishRotationModifier;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.PathHelper;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

/** Basic class for all stationary/unmovable game units ( */
public class DefenceUnit extends Unit {
    public static final String TAG = DefenceUnit.class.getCanonicalName();
    /** body type */
    private static final BodyDef.BodyType sBodyType = BodyDef.BodyType.StaticBody;

    /** create unit from appropriate builder */
    public DefenceUnit(DefenceUnitBuilder unitBuilder) {
        super(unitBuilder);
        mUpdateCycleTime = .7f;
        ((ManualFinishRotationModifier) mUnitRotationModifier).setFinished(true);
    }

    @Override
    public BodyDef.BodyType getBodyType() {
        return sBodyType;
    }

    public void startLifecycle() {
        registerUpdateHandler(new TimerHandler(mUpdateCycleTime, true, new DefenceUnitTimerCallback()));
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
    protected class DefenceUnitTimerCallback implements ITimerCallback {
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
                    .getFirstEnemyInRange(DefenceUnit.this, mAttackRadius);
        }
    }
}