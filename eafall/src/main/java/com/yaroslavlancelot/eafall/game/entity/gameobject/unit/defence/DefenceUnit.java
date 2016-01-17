package com.yaroslavlancelot.eafall.game.entity.gameobject.unit.defence;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.yaroslavlancelot.eafall.game.engine.InstantRotationModifier;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering.IUnitMap;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.PathHelper;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import java.util.Random;

/** Basic class for all stationary/unmovable game units ( */
public class DefenceUnit extends Unit {
    public static final String TAG = DefenceUnit.class.getCanonicalName();
    /** body type */
    private static final BodyDef.BodyType sBodyType = BodyDef.BodyType.StaticBody;
    /** health points per half-second */
    private int mRepairingSpeed = 10;

    /** create unit from appropriate builder */
    public DefenceUnit(DefenceUnitBuilder unitBuilder) {
        super(unitBuilder);
        mUpdateCycleTime = .7f;
        registerEntityModifier(new InstantRotationModifier(20 + new Random().nextInt(20)));
    }

    @Override
    public BodyDef.BodyType getBodyType() {
        return sBodyType;
    }

    @Override
    protected void onNegativeHealth() {
        PlayersHolder.getPlayer(mPlayerName).removeObjectFromPlayer(this);
        registerUpdateHandler(new TimerHandler(.5f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                if (mObjectCurrentHealth < 0) {
                    mObjectCurrentHealth = 0;
                }
                mObjectCurrentHealth += mRepairingSpeed;
                if (mObjectCurrentHealth > mObjectMaximumHealth) {
                    unregisterUpdateHandler(pTimerHandler);
                    PlayersHolder.getPlayer(mPlayerName).addObjectToPlayer(DefenceUnit.this);
                }
            }
        }));
    }

    @Override
    public void destroy() {
    }

    @Override
    public void lifecycleTick(IUnitMap enemiesMap) {
        mObjectCurrentHealth += mRepairingSpeed;
        if (mObjectCurrentHealth > mObjectMaximumHealth) {
            mObjectCurrentHealth = mObjectMaximumHealth;
        }

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
        mObjectToAttack = enemiesMap.getClosestUnit(mX, mY, mAttackRadius);
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
}