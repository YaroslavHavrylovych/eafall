package com.gmail.yaroslavlancelot.eafall.game.ai;

import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;

/**
 * First created bot. Creates table of all units  with how one can kill another capabilities and try to
 * build unit which will kill more your units which are not covered by other his units. If he cover all yours
 * units with his own then will build strongest unit he have money to build.
 */
public class NormalBot implements Runnable {
    public static final String TAG = NormalBot.class.getCanonicalName();
    public static final int DELAY_BETWEEN_ITERATIONS = 50;
    private final ITeam mBotTeam;
    private final float[][] mUnitsEfficiencyArray;

    public NormalBot(ITeam botTeam) {
        LoggerHelper.methodInvocation(TAG, "NormalBot");
        mBotTeam = botTeam;
        mUnitsEfficiencyArray = null;//calculateEfficiencyMap(botTeam.getTeamRace(), botTeam.getEnemyTeam().getTeamRace());
    }

    //TODO bot is not working well after my buildingId's manipulations

    public static float[][] calculateEfficiencyMap(IAlliance race1, IAlliance race2) {
        int race1BuildingsAmount = race1.getBuildingsAmount(),
                race2BuildingsAmount = race2.getBuildingsAmount();
        float[][] unitsEfficiencyMap = new float[race1BuildingsAmount][];
        for (int i = 0; i < race1BuildingsAmount; i++) {
            unitsEfficiencyMap[i] = new float[race2BuildingsAmount];
            for (int j = 0; j < race2BuildingsAmount; j++)
                unitsEfficiencyMap[i][j] = calculateUnitEfficiency(race1.getUnitDummy(i), race2.getUnitDummy(j));
        }
        return unitsEfficiencyMap;
    }

    /**
     * represent what percent of the unit2 health will be destroyed in fight with unit1
     *
     * @param unit1 attacker
     * @param unit2 defender
     * @return float value, which represent how much unit2 health will be burned after fight with unit1
     */
    public static float calculateUnitEfficiency(UnitDummy unit1, UnitDummy unit2) {
        int amountOfPunches1 = unit2.getHealth() / getArmorDamage(unit1, unit2),
                amountOfPunches2 = unit1.getHealth() / getArmorDamage(unit2, unit1);
        return (((float) amountOfPunches1) / amountOfPunches2);
    }

    private static int getArmorDamage(UnitDummy unit1, UnitDummy unit2) {
        return unit1.getArmor().getDamage(unit2.getDamage());
    }

    @Override
    public void run() {
//        while (mBotTeam.getTeamPlanet() != null) {
//            delay();
//            synchronized (mBotTeam.getTeamPlanet()) {
//                // win
//                if (mBotTeam.getEnemyTeam().getTeamPlanet() == null) {
//                    return;
//                }
//
//                // start of the game
//                if (mBotTeam.getTeamPlanet().getExistingBuildingsTypesAmount() == 0) {
//                    buildFirstBuilding();
//                    continue;
//                }
//
//                // money amount is very low
//                if (mBotTeam.getMoney() < mBotTeam.getTeamRace().getBuildingCost(BuildingId.makeId(10, 0)))
//                    continue;
//
//                /*
//                 * Each building should cover some building from enemy planet.
//                 * So first we create array which contains not covered buildings from enemy planet.
//                 * Then we create buildings which can cover enemies buildings.
//                 */
//                // get uncovered buildings
//                int[] uncoveredBuildings = getUncoveredBuildings(mBotTeam, mUnitsEfficiencyArray);
////                if (isAllCovered(uncoveredBuildings)) {
////                    // build some building
////                    buildFirstBuilding();
////                } else {
//                // cover
//                int buildingId = getBuildingIdToCreate(mBotTeam, mUnitsEfficiencyArray, uncoveredBuildings);
//                BuildingId id = BuildingId.makeId((buildingId + 1) * 10, 0);
//                if (mBotTeam.getMoney() >= mBotTeam.getTeamRace().getBuildingCost(id)) {
//                    mBotTeam.getTeamPlanet().createBuilding(id);
//                }
////                }
//            }
//        }
    }

    private void delay() {
        try {
            Thread.sleep(DELAY_BETWEEN_ITERATIONS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void buildFirstBuilding() {
        int money = mBotTeam.getMoney();
        if (money <= 0) return;
        int income = mBotTeam.getTeamPlanet().getIncome();
        int coolestBuildingBotCanBuildId = 1;
        for (int i = 1; i < mBotTeam.getTeamRace().getBuildingsAmount(); i++) {
            if (money + income >= mBotTeam.getTeamRace().getBuildingCost(BuildingId.makeId(coolestBuildingBotCanBuildId * 10, 0))) {
                coolestBuildingBotCanBuildId++;
            }
        }
        if (coolestBuildingBotCanBuildId <= 0) {
            return;
        }
        mBotTeam.getTeamPlanet().createBuilding(BuildingId.makeId(coolestBuildingBotCanBuildId * 10, 0));
    }

    private int[] getUncoveredBuildings(ITeam team, float[][] efficiencyArray) {
        int[] buildingsOfRace1 = getBuildings(team),
                buildingsOfRace2 = getBuildings(team.getEnemyTeam());
        if (isAllCovered(buildingsOfRace2))
            return buildingsOfRace2;
        int maxKillsValue;
        int maxKillBuildingId = 0;
        int tmp;
        for (int i = 0; i < buildingsOfRace1.length; i++) {
            if (isAllCovered(buildingsOfRace2)) break;
            maxKillsValue = 0;
            for (int j = 0; j < buildingsOfRace2.length; j++) {
                if (buildingsOfRace2[j] <= 0) continue;
                tmp = (int) (efficiencyArray[i][j] * buildingsOfRace2[j]);
                if (tmp > maxKillsValue) {
                    maxKillsValue = tmp;
                    maxKillBuildingId = j;
                }
            }
            if (maxKillsValue == 0) continue;
            buildingsOfRace2[maxKillBuildingId] = buildingsOfRace2[maxKillBuildingId] - maxKillsValue;
        }
        return buildingsOfRace2;
    }

    private int[] getBuildings(ITeam team) {
        int[] buildings = new int[team.getTeamRace().getBuildingsAmount()];
        for (int i = 0; i < buildings.length; i++) {
            IBuilding building = team.getTeamPlanet().getBuilding(new Integer(i + 1) * 10);
            buildings[i] = building == null ? 0 : building.getAmount();
        }
        return buildings;
    }

    private boolean isAllCovered(int[] array) {
        for (int i = 0; i < array.length; i++)
            if (array[i] > 0) return false;
        return true;
    }

    private int getBuildingIdToCreate(ITeam team, float[][] efficiencyArray, int[] uncoveredBuildings) {
        int firstUncoveredBuildingId = 0;
        for (int i = 0; i < uncoveredBuildings.length; i++) {
            if (uncoveredBuildings[i] > 0) {
                firstUncoveredBuildingId = i;
                break;
            }
        }
        int race1BuildingAmount = team.getTeamRace().getBuildingsAmount();
        float[] efficiencyComparingWithMoneyCost = new float[race1BuildingAmount];
        float minValue = Float.MAX_VALUE;
        int minValueId = 0;
        for (int i = 0; i < race1BuildingAmount; i++) {
            float result = 0;
            for (int j = 1; efficiencyComparingWithMoneyCost[i] < uncoveredBuildings[firstUncoveredBuildingId]; j++) {
                result = efficiencyArray[i][firstUncoveredBuildingId] * j;
            }
            efficiencyComparingWithMoneyCost[i] = result * team.getTeamRace().getBuildingCost(BuildingId.makeId((i + 1) * 10, 0));
            if (efficiencyComparingWithMoneyCost[i] < minValue) {
                minValue = efficiencyComparingWithMoneyCost[i];
                minValueId = i;
            }
        }
        return minValueId;
    }
}
