package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.entity.AfterInitializationPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.MovableUnit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.MovableUnitBuilder;

/** used to create concrete object pool */
public class MovableUnitsPool extends AfterInitializationPool<MovableUnit> {
    public static final String TAG = MovableUnitsPool.class.getCanonicalName();
    private final MovableUnitBuilder mMovableUnitBuilder;

    public MovableUnitsPool(MovableUnitBuilder unitBuilder) {
        mMovableUnitBuilder = unitBuilder;
        initPool(2, 2);
    }

    @Override
    protected MovableUnit allocatePoolItem() {
        LoggerHelper.printVerboseMessage(TAG, "New movable unit allocated. Available items count=" + getAvailableItems());
        return new MovableUnit(mMovableUnitBuilder) {
            @Override
            protected void onUnitDestroyed() {
                super.onUnitDestroyed();
                LoggerHelper.printVerboseMessage(TAG, "Movable unit recycled. Available items count=" + getAvailableItems());
                recycle(this);
            }
        };
    }
}
