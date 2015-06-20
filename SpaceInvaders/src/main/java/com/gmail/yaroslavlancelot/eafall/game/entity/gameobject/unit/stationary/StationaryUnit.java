package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path.StaticHelper;

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
        mUpdateCycleTime = .7f;
    }

    @Override
    public BodyDef.BodyType getBodyType() {
        return sBodyType;
    }

    public void registerUpdateHandler() {
        registerUpdateHandler(new TimerHandler(mUpdateCycleTime, true, new StationaryUnitTimerCallback()));
    }

    @Override
    protected void rotationBeforeFire(GameObject attackedObject) {
    }

    /** stationary unit behaviour */
    protected class StationaryUnitTimerCallback implements ITimerCallback {
        @Override
        public void onTimePassed(TimerHandler pTimerHandler) {
            // check for anything to attack
            if (mObjectToAttack != null && mObjectToAttack.isObjectAlive()
                    && StaticHelper
                    .getDistanceBetweenPoints(
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