package com.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool;

import com.yaroslavlancelot.eafall.game.entity.AfterInitializationPool;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.defence.DefenceUnit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.defence.DefenceUnitBuilder;

/** used to create concrete object pool */
public class DefenceUnitsPool extends AfterInitializationPool<DefenceUnit> {
    public static final String TAG = DefenceUnitsPool.class.getCanonicalName();
    private final DefenceUnitBuilder mDefenceUnitBuilder;
    private final String mPlayerName;

    public DefenceUnitsPool(DefenceUnitBuilder unitBuilder, String playerName) {
        mDefenceUnitBuilder = unitBuilder;
        mPlayerName = playerName;
        initPool(3, 1);
    }

    @Override
    protected DefenceUnit allocatePoolItem() {
        //TODO logger was here
        DefenceUnit defenceUnit = new DefenceUnit(mDefenceUnitBuilder) {
            @Override
            protected void onDestroyed() {
                super.onDestroyed();
                //TODO logger was here
                recycle(this);
            }
        };
        defenceUnit.setPlayer(mPlayerName);
        return defenceUnit;
    }
}
