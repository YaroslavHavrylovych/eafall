package com.yaroslavlancelot.eafall.game.ai;

import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.IUnitBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.UnitBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import timber.log.Timber;

/**
 * Easy to win bot.
 *
 * @author Yaroslav Havrylovych
 */
public class SingleWayHardBot extends Bot {
    private long mLastTickTime;
    private final static int ADDITION_INCOME_TIME = 20000;
    private List<BuildingId> mNewBuildingsToBuild = new ArrayList<>(10);
    private List<BuildingId> mUpgradedBuildingsToBuild = new ArrayList<>(5);
    private List<BuildingId> mBuildingsToUpgrade = new ArrayList<>(5);

    @Override
    GoalBuilding findTheGoalBuilding(IAlliance alliance, PlanetStaticObject planet) {
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
                            || buildingDummy.getBuildingType() == BuildingType.WEALTH_BUILDING)) {
                        if (planet.getBuildingsAmount() <= 3) {
                            continue;
                        } else {
                            if (amountOnPlanet < 1
                                    && buildingDummy.getBuildingType() == BuildingType.WEALTH_BUILDING
                                    && planet.getBuildingsAmount() >= 10) {
                                return new GoalBuilding(buildingId, false);
                            }
                            if (amountOnPlanet < 1
                                    && buildingDummy.getBuildingType() == BuildingType.SPECIAL_BUILDING
                                    && planet.getBuildingsAmount() >= 7) {
                                return new GoalBuilding(buildingId, false);
                            }
                        }
                    }
                    if (buildingDummy.getBuildingType() == BuildingType.DEFENCE_BUILDING
                            && planet.getBuildingsAmount() < 10) {
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
    void onBuildingChanged(IBuilding building, boolean upgrade) {
        if (building == null) {
            return;
        }
        if (building instanceof UnitBuilding) {
            ((UnitBuilding) building).setPath(new Random().nextBoolean());
        }
    }

    @Override
    protected void onTickCallback() {
        super.onTickCallback();
        long time = System.currentTimeMillis();
        if (time - mLastTickTime > ADDITION_INCOME_TIME) {
            mLastTickTime = time;
            mBotPlayer.incomeTime();
        }
    }

    @Override
    void onChangePathForExistingBuilding(PlanetStaticObject planetStaticObject) {
    }
}
