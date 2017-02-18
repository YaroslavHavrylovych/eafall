package com.yaroslavlancelot.eafall.game.ai;

import com.yaroslavlancelot.eafall.game.GameState;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.UnitBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.player.IPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import timber.log.Timber;

/**
 * Still easy but probably changed soon bot.
 *
 * @author Yaroslav Havrylovych
 */
public class TwoWaysHardBot implements IBot {
    public static final String TAG = TwoWaysEasyBot.class.getCanonicalName();
    public static final int DELAY_BETWEEN_ITERATIONS = 300;
    private IPlayer mBotPlayer;
    private List<BuildingId> mNewBuildingsToBuild = new ArrayList<>(10);
    private List<BuildingId> mUpgradedBuildingsToBuild = new ArrayList<>(5);
    private List<BuildingId> mBuildingsToUpgrade = new ArrayList<>(5);

    @Override
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

                    //suppressor
                    if (planet.getObjectCurrentHealth() < planet.getMaximumObjectHealth() / 2
                            && !planet.isSuppressorUsed()) {
                        planet.useSuppressor();
                    }

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
                            //TODO why 2 buildings created sometimes? Check it's true
                            //probably need to play with debugging. I saw when it's 2 buildings created and both not upgraded
                            Timber.v("building %s", goalBuilding.getBuildingId().toString());
                            result = planet.createBuilding(goalBuilding.getBuildingId());
                        }
                        if (result) {
                            Timber.v("building operation success result %s", goalBuilding.getBuildingId().toString());
                            randomizeUnitPath(planet.getBuilding(goalBuilding.getBuildingId().getId()));
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

    private boolean mediumWaitTimeToBuild(IAlliance alliance, BuildingId buildingId,
                                          int currentMoney, int income) {
        return alliance.getBuildingCost(buildingId) < currentMoney + 7 * income;
    }

    private boolean mediumWaitTimeToUpgrade(IAlliance alliance, BuildingId buildingId,
                                            int amount, int currentMoney, int income) {
        return alliance.getUpgradeCost(buildingId, amount) < currentMoney + 3 * income;
    }

    private boolean shortWaitTimeToBuild(IAlliance alliance, BuildingId buildingId,
                                         int currentMoney, int income) {
        return alliance.getBuildingCost(buildingId) < currentMoney + 2 * income;
    }

    private GoalBuilding findTheGoalBuilding(IAlliance alliance, PlanetStaticObject planet) {
        Timber.v("findTheGoalBuilding");
        int amountOnPlanet;
        BuildingDummy buildingDummy;
        mNewBuildingsToBuild.clear();
        mBuildingsToUpgrade.clear();
        mUpgradedBuildingsToBuild.clear();
        int currentMoney = mBotPlayer.getMoney();
        int incomeValue = planet.getIncome();

        BuildingId[] buildingIds = mBotPlayer.getBuildingsIds();
        //1. Calculate list of buildings you could build (not upgraded)
        //2. Calculate list of buildings, with not less than 1 (one) building ready for upgrade
        for (BuildingId buildingId : buildingIds) {
            buildingDummy = alliance.getBuildingDummy(buildingId);
            amountOnPlanet = planet.getBuildingsAmount(buildingId.getId());
            if (planet.getExistingBuildingsTypesAmount() > 0) {
                Timber.v("One building already build");
                if (amountOnPlanet < buildingDummy.getAmountLimit()) {
                    //we could build special building only if there is a lot of other buildings
                    if ((buildingDummy.getBuildingType() == BuildingType.SPECIAL_BUILDING
                            || buildingDummy.getBuildingType() == BuildingType.WEALTH_BUILDING
                            || buildingDummy.getBuildingType() == BuildingType.DEFENCE_BUILDING)
                            && planet.getBuildingsAmount() < 12) {
                        continue;
                    }
                    if (amountOnPlanet == 0
                            && mediumWaitTimeToBuild(alliance, buildingId, currentMoney, incomeValue)) {
                        Timber.v("mNewBuildingsToBuild %s", buildingId.toString());
                        mNewBuildingsToBuild.add(buildingId);
                    }
                }
                //more than 1
                if (amountOnPlanet >= 1) {
                    if (alliance.isUpgradeAvailable(buildingId)) {
                        if (mediumWaitTimeToUpgrade(alliance, buildingId,
                                amountOnPlanet, currentMoney, incomeValue)) {
                            Timber.v("mBuildingsToUpgrade %s", buildingId.toString());
                            mBuildingsToUpgrade.add(buildingId);
                        }
                    } else {
                        if (amountOnPlanet < buildingDummy.getAmountLimit() &&
                                mediumWaitTimeToBuild(alliance, buildingId,
                                        currentMoney, incomeValue)) {
                            Timber.v("mUpgradedBuildingsToBuild %s", buildingId.toString());
                            mUpgradedBuildingsToBuild.add(buildingId);
                        }
                    }
                }
            } else {
                Timber.v("No buildings built before");
                if (buildingDummy.getBuildingType() == BuildingType.DEFENCE_BUILDING
                        || buildingDummy.getBuildingType() == BuildingType.SPECIAL_BUILDING
                        || buildingDummy.getBuildingType() == BuildingType.WEALTH_BUILDING) {
                    continue;
                }
                if (shortWaitTimeToBuild(alliance, buildingId, currentMoney, incomeValue)) {
                    mNewBuildingsToBuild.add(buildingId);
                }
            }
        }

        //next we would try (with chance) to build upgraded most building,
        // or upgrade existing building
        // or create a new building
        if (!mUpgradedBuildingsToBuild.isEmpty()) {
            if ((System.currentTimeMillis() & 3) != 3) {
                return new GoalBuilding(
                        mUpgradedBuildingsToBuild.get(new Random()
                                .nextInt(mUpgradedBuildingsToBuild.size())),
                        false);
            }
        }
        if (!mBuildingsToUpgrade.isEmpty()) {
            if ((System.currentTimeMillis() & 1) == 1) {
                return new GoalBuilding(
                        mBuildingsToUpgrade.get(new Random().nextInt(mBuildingsToUpgrade.size())),
                        true);
            }
        }
        if (!mNewBuildingsToBuild.isEmpty()) {
            return new GoalBuilding(
                    mNewBuildingsToBuild.get(new Random().nextInt(mNewBuildingsToBuild.size())),
                    false);
        }
        return null;
    }

    @Override
    public synchronized void init(final IPlayer botPlayer) {
        mBotPlayer = botPlayer;
    }

    private void delay() {
        try {
            Thread.sleep(DELAY_BETWEEN_ITERATIONS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void randomizeUnitPath(IBuilding building) {
        if (building == null) {
            return;
        }
        if (building instanceof UnitBuilding) {
            ((UnitBuilding) building).setPath(new Random().nextBoolean());
        }
    }
}