package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.stationary;

import com.badlogic.gdx.math.Vector2;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dynamicobjects.Bullet;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.UnitPathUtil;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import java.util.List;

/** Basic class for all stationary/unmovable game units ( */
public class StationaryUnit extends Unit {
    public static final String TAG = StationaryUnit.class.getCanonicalName();

    /** create unit from appropriate builder */
    public StationaryUnit(StationaryUnitBuilder unitBuilder) {
        super(unitBuilder);
        mUpdateCycleTime = .7f;
    }

    public void registerUpdateHandler() {
        registerUpdateHandler(new TimerHandler(mUpdateCycleTime, true, new StationaryUnitTimerCallback()));
    }

    @Override
    protected void rotationBeforeFire(GameObject attackedObject) {
    }

    @Override
    protected void setBulletFirePosition(GameObject attackedObject, Bullet bullet) {
        Vector2 objectPosition = getBody().getPosition();
        bullet.fireFromPosition(objectPosition.x + SizeConstants.UNIT_SIZE / 2 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                objectPosition.y - Bullet.BULLET_SIZE / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                attackedObject);
    }

    /** stationary unit behaviour */
    protected class StationaryUnitTimerCallback implements ITimerCallback {
        @Override
        public void onTimePassed(TimerHandler pTimerHandler) {
            // check for anything to attack
            if (mObjectToAttack != null && mObjectToAttack != mEnemiesUpdater.getMainTarget() &&
                    mObjectToAttack.isObjectAlive() && UnitPathUtil.getDistanceBetweenPoints(getX(), getY(),
                    mObjectToAttack.getCenterX(), mObjectToAttack.getCenterY()) < mAttackRadius) {
                attackGoal(mObjectToAttack);
                return;
            } else {
                // we can't reach previously attacked unit
                mObjectToAttack = null;
            }

            // search for new unit to attack
            if (mEnemiesUpdater != null) {
                List<GameObject> units = mEnemiesUpdater.getEnemiesInRangeForUnit(StationaryUnit.this, mAttackRadius);
                if (units != null && !units.isEmpty()) {
                    mObjectToAttack = units.get(0);
                    attackGoal(mObjectToAttack);
                    return;
                }
            }
        }
    }
}