package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.entity.AfterInitializationPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary.StationaryUnit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary.StationaryUnitBuilder;

/** used to create concrete object pool */
//TODO create UnitPool later (common func for unit and orbital station have to be there)
public class StationaryUnitsPool extends AfterInitializationPool<StationaryUnit> {
    public static final String TAG = StationaryUnitsPool.class.getCanonicalName();
    private final StationaryUnitBuilder mStationaryUnitBuilder;
    private final String mPlayerName;

    public StationaryUnitsPool(StationaryUnitBuilder unitBuilder, String playerName) {
        mStationaryUnitBuilder = unitBuilder;
        mPlayerName = playerName;
        initPool(3, 1);
    }

    @Override
    protected StationaryUnit allocatePoolItem() {
        LoggerHelper.printVerboseMessage(TAG, "New stat. unit allocated. Available items count=" + getAvailableItems());
        StationaryUnit stationaryUnit = new StationaryUnit(mStationaryUnitBuilder) {
            @Override
            protected void onDestroyed() {
                super.onDestroyed();
                LoggerHelper.printVerboseMessage(TAG, "Stationary unit recycled. Available items count=" + getAvailableItems());
                recycle(this);
            }
        };
        stationaryUnit.setPlayer(mPlayerName);
        return stationaryUnit;
    }
}
