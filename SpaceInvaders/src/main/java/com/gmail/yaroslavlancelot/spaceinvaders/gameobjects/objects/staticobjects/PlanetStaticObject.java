package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameloop.UnitCreatorCycle;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Higgs;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;
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

    /**
     * used on client side. Means that planet will only display without handling units creation logic and
     * money calculation but can  calculate buildings
     */
    private boolean mIsFakePlanet = false;

    private PlanetStaticObject(float x, float y, ITextureRegion textureRegion, EntityOperations entityOperations, ITeam planetTeam) {
        super(x, y, textureRegion, entityOperations.getObjectManager());
        mEntityOperations = entityOperations;
        mIncomeIncreasingValue = 10;
        mPlanetTeam = planetTeam;
        mObjectArmor = new Higgs(2);
        setWidth(SizeConstants.PLANET_DIAMETER);
        setHeight(SizeConstants.PLANET_DIAMETER);
        initHealth(3000);
    }

    public PlanetStaticObject(float x, float y, ITextureRegion textureRegion, EntityOperations entityOperations, ITeam planetTeam, boolean isFakePlanet) {
        this(x, y, textureRegion, entityOperations, planetTeam);
        mIsFakePlanet = isFakePlanet;
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
            buildings.put(buildingId, new BuildingsHolder(staticObject, buildingId));
        }
        addBuilding(buildingId);
    }

    private void addBuilding(int key) {
        BuildingsHolder holder = buildings.get(key);
        StaticObject building = buildings.get(key).mBuilding;
        if (mIsFakePlanet) {
            if (holder.mBuildingsAmount == 0)
                attachChild(building);
        } else {
            LoggerHelper.printDebugMessage(TAG, "building creation : " + "existing money=" + getMoneyAmount()
                    + ", cost=" + building.mCost);
            if (getMoneyAmount() < building.mCost)
                return;
            if (holder.mBuildingsAmount == 0) {
                LoggerHelper.printInformationMessage(TAG, "creating building on planet");
                attachChild(building);
            }
            buyBuilding(building.mCost);
        }
        holder.increaseBuildingsAmount();
    }

    private int getMoneyAmount() {
        return mPlanetTeam == null ? 0 : mPlanetTeam.getMoney();
    }

    private void buyBuilding(int cost) {
        if (mPlanetTeam != null)
            mPlanetTeam.changeMoney(-cost);
    }

    public Map<Integer, BuildingsHolder> getBuildings() {
        return buildings;
    }

    public class BuildingsHolder {
        private final StaticObject mBuilding;
        private final int mBuildingId;
        private final int mUnitCreationCycleTime = 20;
        private int mBuildingsAmount;
        private UnitCreatorCycle mUnitCreatorCycle;


        private BuildingsHolder(StaticObject building, int buildingId) {
            mBuilding = building;
            mBuildingId = buildingId;
        }

        private void increaseBuildingsAmount() {
            if (!mIsFakePlanet) {
                mIncomeIncreasingValue += mBuilding.getObjectIncomeIncreasingValue();
                if (mUnitCreatorCycle == null) {
                    mUnitCreatorCycle = new UnitCreatorCycle(mPlanetTeam, mEntityOperations, mBuildingId);
                    registerUpdateHandler(new TimerHandler(mUnitCreationCycleTime, true, mUnitCreatorCycle));
                }
            }
            mBuildingsAmount += 1;
            mUnitCreatorCycle.increaseUnitAmount();
        }

        public int getBuildingsAmount() {
            return mBuildingsAmount;
        }
    }
}