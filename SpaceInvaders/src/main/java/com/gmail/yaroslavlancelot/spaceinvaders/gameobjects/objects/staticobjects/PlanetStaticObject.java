package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameloop.UnitCreatorCycle;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Higgs;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.HashMap;
import java.util.Map;

/** represent team planet */
public class PlanetStaticObject extends StaticObject {
    /** tag, which is used for debugging purpose */
    public static final String TAG = PlanetStaticObject.class.getCanonicalName();
    // unit spawn point
    private float mSpawnPointX, mSpawnPointY;
    // buildings in current planet
    private Map<Integer, BuildingsHolder> buildings = new HashMap<Integer, BuildingsHolder>(15);
    /** for creating new units */
    private EntityOperations mEntityOperations;
    /** the team, current planet belongs to */
    private ITeam mPlanetTeam;

    public PlanetStaticObject(float x, float y, ITextureRegion textureRegion, EntityOperations entityOperations, ITeam planetTeam) {
        super(x, y, textureRegion, entityOperations.getObjectManager());
        mEntityOperations = entityOperations;
        mIncomeIncreasingValue = 10;
        mPlanetTeam = planetTeam;
        setWidth(SizeConstants.PLANET_DIAMETER);
        setHeight(SizeConstants.PLANET_DIAMETER);
        mObjectArmor = new Higgs(2);
        mObjectHealth = 100;
    }

    public void setSpawnPoint(float spawnPointX, float spawnPointY) {
        mSpawnPointX = spawnPointX;
        mSpawnPointY = spawnPointY;
    }

    public float getSpawnPointX() {
        return mSpawnPointX;
    }

    public float getSpawnPointY() {
        return mSpawnPointY;
    }

    public void createBuildingById(int buildingId) {
        LoggerHelper.methodInvocation(TAG, "buildBuilding");
        if (buildings.get(buildingId) == null) {
            final StaticObject staticObject =
                    mPlanetTeam.getTeamRace().getBuildingById(buildingId);
            buildings.put(buildingId, new BuildingsHolder(staticObject, 0));
        }
        addBuilding(buildingId);
    }

    private void addBuilding(int key) {
        BuildingsHolder holder = buildings.get(key);
        StaticObject building = buildings.get(key).mStaticObject;
        LoggerHelper.printDebugMessage(TAG, "building creation : " + "existing money=" + getMoneyAmount()
                + ", cost=" + building.mCost);
        if (getMoneyAmount() < building.mCost)
            return;
        if (holder.mBuildingsAmount == 0) {
            LoggerHelper.printInformationMessage(TAG, "creating building on planet");
            attachChild(building);
        }
        holder.increaseBuildingsAmount();
        buyBuilding(building.mCost);
        mIncomeIncreasingValue += building.getObjectIncomeIncreasingValue();
    }

    private void buyBuilding(int cost) {
        if (mPlanetTeam != null)
            mPlanetTeam.changeMoney(-cost);
    }

    private int getMoneyAmount() {
        return mPlanetTeam == null ? 0 : mPlanetTeam.getMoney();
    }

    private class BuildingsHolder {
        private final StaticObject mStaticObject;
        private int mBuildingsAmount;
        private UnitCreatorCycle mUnitCreatorCycle;
        private int mBuildingId;


        private BuildingsHolder(StaticObject staticObject, int buildingId) {
            mStaticObject = staticObject;
            mBuildingId = buildingId;
        }

        public void increaseBuildingsAmount() {
            if (mUnitCreatorCycle == null) {
                mUnitCreatorCycle = new UnitCreatorCycle(mPlanetTeam, mEntityOperations, mBuildingId);
                registerUpdateHandler(new TimerHandler(7, true, mUnitCreatorCycle));
            }
            mBuildingsAmount += 1;
            mUnitCreatorCycle.increaseUnitAmount();
        }
    }
}