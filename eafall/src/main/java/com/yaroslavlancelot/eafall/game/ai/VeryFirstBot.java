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
//TODO stop bot when game finished
public class VeryFirstBot implements Runnable {
    public static final String TAG = VeryFirstBot.class.getCanonicalName();
    public static final int DELAY_BETWEEN_ITERATIONS = 300;
    private final IPlayer mBotPlayer;

    public VeryFirstBot(IPlayer botPlayer) {
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
                if (GameState.isPaused()) {
                    delay();
                    continue;
                }
                delay();
                planet = mBotPlayer.getPlanet();
                // win
                if (planet == null) {
                    return;
                }

                //suppressor
                if (planet.getObjectCurrentHealth() < planet.getMaximumObjectHealth() / 2 && !planet.isSuppressorUsed()) {
                    planet.useSuppressor();
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
                        if (buildingDummy.getBuildingType() == BuildingType.DEFENCE_BUILDING) {
                            continue;
                        }
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
                if (!buildingsToBuild.isEmpty()) {
                    final BuildingId buildingId = buildingsToBuild.get(
                            new Random().nextInt(buildingsToBuild.size()));
                    final IBuilding building;
                    if (buildingsToUpgrade.contains(buildingId)) {
                        building = planet.getBuilding(buildingId.getId());
                        building.upgradeBuilding();
                        randomizeUnitPath(building);
                        continue;
                    }
                    planet.createBuilding(buildingId);
                    building = planet.getBuilding(buildingId.getId());
                    randomizeUnitPath(building);
                } else if (!buildingsToUpgrade.isEmpty()) {
                    BuildingId buildingId = buildingsToUpgrade.get(
                            new Random().nextInt(buildingsToUpgrade.size()));
                    final IBuilding building = planet.getBuilding(buildingId.getId());
                    building.upgradeBuilding();
                    randomizeUnitPath(building);
                }
            }
        } catch (Exception ex) {
            //TODO this has to be marked somewhere to find why does this happen
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
