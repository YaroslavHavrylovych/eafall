package com.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool;

import com.yaroslavlancelot.eafall.game.entity.AfterInitializationPool;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnitBuilder;

/** used to create concrete object pool */
//TODO create UnitPool later (common func for unit and orbital station have to be there)
public class OffenceUnitsPool extends AfterInitializationPool<OffenceUnit> {
    public static final String TAG = OffenceUnitsPool.class.getCanonicalName();
    private final OffenceUnitBuilder mOffenceUnitBuilder;
    private final String mPlayerName;

    public OffenceUnitsPool(OffenceUnitBuilder unitBuilder, String playerName) {
        mOffenceUnitBuilder = unitBuilder;
        mPlayerName = playerName;
        initPool(2, 2);
    }

    @Override
    protected OffenceUnit allocatePoolItem() {
        //TODO logger was here
        OffenceUnit offenceUnit = new OffenceUnit(mOffenceUnitBuilder) {
            @Override
            protected void onDestroyed() {
                super.onDestroyed();
                //TODO logger was here
                recycle(this);
            }
        };
        offenceUnit.setPlayer(mPlayerName);
        return offenceUnit;
    }
}
