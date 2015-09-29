package com.gmail.yaroslavlancelot.eafall.game.ai;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.UnitBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.PauseGameEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Logic:
 * <br/>
 * <li>
 * <ul>1. Calculate list of buildings you can build this or next income.</ul>
 * <ul>2. Calculate list of buildings, with more than 3 (three) building ready,
 * you can upgrade this or next income.</ul>
 * <ul>3. Pick the cost most building you can (to build).
 * If you can upgrade it this or next move that do that in other case - build</ul>
 * </li>
 * <p/>
 * Randomly decides the path for the units.
 *
 * @author Yaroslav Havrylovych
 */
//TODO FYI : bot works in another thread and it has to do some actions in the update thread
public class VeryFirstBot implements Runnable {
    public static final String TAG = VeryFirstBot.class.getCanonicalName();
    public static final int DELAY_BETWEEN_ITERATIONS = 500;
    private final IPlayer mBotPlayer;

    public VeryFirstBot(IPlayer botPlayer) {
        LoggerHelper.methodInvocation(TAG, "VeryFirstBot");
        mBotPlayer = botPlayer;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        List<BuildingId> buildingsToBuild = new ArrayList<>(10);
        List<BuildingId> buildingsToUpgrade = new ArrayList<>(10);
        IAlliance alliance = mBotPlayer.getAlliance();
        BuildingDummy buildingDummy;
        PlanetStaticObject planet;
        int amountOnPlanet;
        //planet not initialized yet
        while (mBotPlayer.getPlanet() == null) {
            delay();
        }
        try {
            //start the bot logic
            while (mBotPlayer.getPlanet() != null) {
                if (PauseGameEvent.getInstance().isPause()) {
                    delay();
                    continue;
                }
                delay();
                planet = mBotPlayer.getPlanet();
                // win
                if (planet == null) {
                    return;
                }
                int money = mBotPlayer.getMoney();
                buildingsToBuild.clear();
                buildingsToUpgrade.clear();

                BuildingId[] buildingIds = mBotPlayer.getBuildingsIds();
                //1. Calculate list of buildings you can build this or next income.
                //2. Calculate list of buildings, with not less than 3 (three) building ready,
                //you can upgrade this or next income.
                for (BuildingId buildingId : buildingIds) {
                    buildingDummy = alliance.getBuildingDummy(buildingId);
                    amountOnPlanet = planet.getBuildingsAmount(buildingId.getId());
                    if (amountOnPlanet < buildingDummy.getAmountLimit()) {
                        if (money >= buildingDummy.getCost(buildingId.getUpgrade())) {
                            buildingsToBuild.add(buildingId);
                        }
                    }
                    //more than 3
                    if (amountOnPlanet >= 3 && alliance.isUpgradeAvailable(buildingId)
                            && (amountOnPlanet * buildingDummy.getCost(buildingId.getUpgrade()) >= money)) {
                        buildingsToUpgrade.add(buildingId);
                    }
                }

                //3. Pick the cost most building you can (to build).
                //If you can upgrade it this or next move that do that in other case - build
                LoggerHelper.printVerboseMessage(TAG, "buildingsToBuild.size()=" + buildingsToBuild.size());
                LoggerHelper.printVerboseMessage(TAG, "buildingsToUpgrade.size()=" + buildingsToUpgrade.size());
                if (!buildingsToBuild.isEmpty()) {
                    final BuildingId buildingId = buildingsToBuild.get(
                            new Random().nextInt(buildingsToBuild.size()));
                    LoggerHelper.printVerboseMessage(TAG, "picked building id=" + buildingId);
                    final IBuilding building;
                    if (buildingsToUpgrade.contains(buildingId)) {
                        LoggerHelper.printVerboseMessage(TAG, "Upgrade instead of build");
                        building = planet.getBuilding(buildingId.getId());
                        building.upgradeBuilding();
                        randomizeUnitPath(building);
                        continue;
                    }
                    LoggerHelper.printVerboseMessage(TAG, "Build");
                    final PlanetStaticObject finalPlanet = planet;
                    finalPlanet.createBuilding(buildingId);
                    building = planet.getBuilding(buildingId.getId());
                    randomizeUnitPath(building);
                } else if (!buildingsToUpgrade.isEmpty()) {
                    BuildingId buildingId = buildingsToUpgrade.get(
                            new Random().nextInt(buildingsToUpgrade.size()));
                    LoggerHelper.printVerboseMessage(TAG, "picked building id=" + buildingId);
                    LoggerHelper.printVerboseMessage(TAG, "Upgrade");
                    final IBuilding building = planet.getBuilding(buildingId.getId());
                    building.upgradeBuilding();
                    randomizeUnitPath(building);
                }
            }
        } catch (Exception ex) {
            //TODO this has to be marked somewhere to find why does this happen
            LoggerHelper.printErrorMessage(TAG, ex.getMessage());
        }
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
