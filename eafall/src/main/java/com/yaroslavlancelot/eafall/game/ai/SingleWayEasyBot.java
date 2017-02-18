package com.yaroslavlancelot.eafall.game.ai;

import com.yaroslavlancelot.eafall.game.GameState;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.player.IPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import timber.log.Timber;

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
 *
 * @author Yaroslav Havrylovych
 */
public class SingleWayEasyBot implements IBot {
    public static final String TAG = SingleWayEasyBot.class.getCanonicalName();
    public static final int DELAY_BETWEEN_ITERATIONS = 300;
    private IPlayer mBotPlayer;

    @Override
    public void run() {
        Timber.v("bot [%s] started", this.getClass().getName());
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        List<BuildingId> buildingsToBuild = new ArrayList<>(10);
        List<BuildingId> buildingsToUpgrade = new ArrayList<>(10);
        IAlliance alliance = mBotPlayer.getAlliance();
        BuildingDummy buildingDummy;
        PlanetStaticObject planet;
        int amountOnPlanet;
        //planet not initialized yet
        while (mBotPlayer.getPlanet() == null && mBotPlayer.getEnemyPlayer().getPlanet() == null) {
            delay();
        }
        try {
            //start the bot logic
            while (mBotPlayer.getPlanet() != null && mBotPlayer.getEnemyPlayer().getPlanet() != null) {
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
                        continue;
                    }
                    planet.createBuilding(buildingId);
                } else if (!buildingsToUpgrade.isEmpty()) {
                    BuildingId buildingId = buildingsToUpgrade.get(
                            new Random().nextInt(buildingsToUpgrade.size()));
                    final IBuilding building = planet.getBuilding(buildingId.getId());
                    building.upgradeBuilding();
                }
            }
        } catch (Exception ex) {
            Timber.w(ex, "exception in bot working logic");
        }
    }

    @Override
    public void init(final IPlayer botPlayer) {
        mBotPlayer = botPlayer;
    }

    private void delay() {
        try {
            Thread.sleep(DELAY_BETWEEN_ITERATIONS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
