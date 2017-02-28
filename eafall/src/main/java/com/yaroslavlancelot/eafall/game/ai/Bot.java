package com.yaroslavlancelot.eafall.game.ai;

import com.yaroslavlancelot.eafall.game.GameState;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.player.IPlayer;

import timber.log.Timber;

/** Base bot methods etc */
abstract class Bot implements IBot {
    int mDelayBetweenIterations = 300;
    IPlayer mBotPlayer;

    @Override
    public synchronized void init(final IPlayer botPlayer) {
        mBotPlayer = botPlayer;
    }

    public void run() {
        Timber.v("bot [%s] started", this.getClass().getName());
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        IAlliance alliance = mBotPlayer.getAlliance();
        PlanetStaticObject planet;
        //planet not initialized yet
        while (mBotPlayer.getPlanet() == null || mBotPlayer.getEnemyPlayer().getPlanet() == null) {
            delay();
        }
        GoalBuilding goalBuilding = null;
        try {
            //start the bot logic
            while (mBotPlayer.getPlanet() != null && mBotPlayer.getEnemyPlayer().getPlanet() != null) {
                try {
                    delay();
                    if (GameState.isPaused()) {
                        continue;
                    }
                    planet = mBotPlayer.getPlanet();
                    if (planet == null) {
                        return;
                    }

                    onTickCallback();

                    //suppressor
                    if (planet.getObjectCurrentHealth() < planet.getMaximumObjectHealth() / 2
                            && !planet.isSuppressorUsed()) {
                        planet.useSuppressor();
                    }

                    onChangePathForExistingBuilding(planet);
                    //find to build or try to build
                    // (== 0 is needed, as we could have bigger first income, which cold destroy the logic)
                    if (goalBuilding == null) {
                        goalBuilding = findTheGoalBuilding(alliance, planet);
                    } else {
                        Timber.v("goal building exists, checking build");
                        boolean result;
                        BuildingId buildingId = goalBuilding.getBuildingId();
                        int neededMoneyAmount = goalBuilding.isUpdate()
                                ? mBotPlayer.getAlliance().getUpgradeCost(buildingId, planet.getBuildingsAmount(buildingId.getId()))
                                : alliance.getBuildingCost(buildingId);
                        if (mBotPlayer.getMoney() < neededMoneyAmount) {
                            continue;
                        }
                        if (goalBuilding.isUpdate()) {
                            Timber.v("updating %s", goalBuilding.getBuildingId().toString());
                            result = planet.getBuilding(goalBuilding.getBuildingId().getId())
                                    .upgradeBuilding();
                        } else {
                            Timber.v("building %s", goalBuilding.getBuildingId().toString());
                            result = planet.createBuilding(goalBuilding.getBuildingId());
                        }
                        if (result) {
                            Timber.v("building operation success result %s", goalBuilding.getBuildingId().toString());
                            onBuildingChanged(
                                    planet.getBuilding(goalBuilding.getBuildingId().getId()),
                                    goalBuilding.isUpdate());
                            goalBuilding = null;
                        }
                    }
                } catch (Exception ex) {
                    Timber.w(ex, "exception in cyclic bot working logic");
                    goalBuilding = null;
                }
            }
        } catch (Exception ex) {
            Timber.w(ex, "exception in bot working logic");
        }
    }

    abstract void onChangePathForExistingBuilding(PlanetStaticObject planetStaticObject);

    abstract GoalBuilding findTheGoalBuilding(IAlliance alliance, PlanetStaticObject planet);

    abstract void onBuildingChanged(IBuilding building, boolean upgrade);

    /** Triggered each iteration in bot logic before changes (after checking planets exists) */
    protected void onTickCallback() {
    }

    boolean mediumWaitTimeToBuild(IAlliance alliance, BuildingId buildingId,
                                  int currentMoney, int income) {
        return alliance.getBuildingCost(buildingId) < currentMoney + 7 * income;
    }

    boolean mediumWaitTimeToUpgrade(IAlliance alliance, BuildingId buildingId,
                                    int amount, int currentMoney, int income) {
        return alliance.getUpgradeCost(buildingId, amount) < currentMoney + 3 * income;
    }

    boolean shortWaitTimeToBuild(IAlliance alliance, BuildingId buildingId,
                                 int currentMoney, int income) {
        return alliance.getBuildingCost(buildingId) < currentMoney + 2 * income;
    }

    void delay() {
        try {
            Thread.sleep(mDelayBetweenIterations);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
