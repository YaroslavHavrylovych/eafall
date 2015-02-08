package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.entity.AfterInitializationPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary.StationaryUnit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary.StationaryUnitBuilder;

/** used to create concrete object pool */
public class StationaryUnitsPool extends AfterInitializationPool<StationaryUnit> {
    public static final String TAG = StationaryUnitsPool.class.getCanonicalName();
    private final StationaryUnitBuilder mStationaryUnitBuilder;

    public StationaryUnitsPool(StationaryUnitBuilder unitBuilder) {
        mStationaryUnitBuilder = unitBuilder;
        initPool(3, 1);
    }

    @Override
    protected StationaryUnit allocatePoolItem() {
        LoggerHelper.printVerboseMessage(TAG, "New stat. unit allocated. Available items count=" + getAvailableItems());
        return new StationaryUnit(mStationaryUnitBuilder) {
            @Override
            protected void onUnitDestroyed() {
                super.onUnitDestroyed();
                LoggerHelper.printVerboseMessage(TAG, "Stationary unit recycled. Available items count=" + getAvailableItems());
                recycle(this);
            }
        };
    }
}
