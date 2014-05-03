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

    /** set unit spawn point */
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

    /** handle logic of creating a building on a non-fake planet */
    public boolean purchaseBuilding(int buildingId) {
        return createBuildingById(buildingId, false);
    }

    /** perform {@code createBuildingById} invocation with parameter of is this planet is fake */
    public boolean createBuildingById(int buildingId) {
        LoggerHelper.methodInvocation(TAG, "buildBuilding");
        return createBuildingById(buildingId, mIsFakePlanet);
    }

    /** create new building object and add it to holder so then call {@code addBuilding()} */
    private boolean createBuildingById(int buildingId, boolean isFakePlanet) {
        LoggerHelper.methodInvocation(TAG, "buildBuilding. IsFakePlanet=" + isFakePlanet);
        if (buildings.get(buildingId) == null) {
            final StaticObject staticObject =
                    mPlanetTeam.getTeamRace().getBuildingById(buildingId);
            buildings.put(buildingId, new BuildingsHolder(staticObject, buildingId));
        }
        return addBuilding(buildingId, isFakePlanet);
    }

    /**
     * construction new building on the planet. If planet is fake then it will just creating building
     * if it's first instance of particular building type in other way (if it's not first) will just
     * increase building amount
     */
    private boolean addBuilding(int key, boolean isFakePlanet) {
        BuildingsHolder holder = buildings.get(key);
        StaticObject building = buildings.get(key).mBuilding;
        if (isFakePlanet) {
            if (holder.mBuildingsAmount == 0)
                attachChild(building);
        } else {
            LoggerHelper.printDebugMessage(TAG, "building creation : " + "existing money=" + getMoneyAmount()
                    + ", cost=" + building.mCost);
            if (getMoneyAmount() < building.mCost)
                return false;
            if (holder.mBuildingsAmount == 0) {
                LoggerHelper.printInformationMessage(TAG, "creating building on planet");
                attachChild(building);
            }
            buyBuilding(building.mCost);
        }
        holder.increaseBuildingsAmount();
        return true;
    }

    private int getMoneyAmount() {
        return mPlanetTeam == null ? 0 : mPlanetTeam.getMoney();
    }

    /** pay money for creating building on the planet */
    private void buyBuilding(int cost) {
        if (mPlanetTeam != null)
            mPlanetTeam.changeMoney(-cost);
    }

    public Map<Integer, BuildingsHolder> getBuildings() {
        return buildings;
    }

    /**
     * holds building image (for current planet) and building id and amount of current building in the planet
     * (for use it when income and new building creation etc)
     */
    public class BuildingsHolder {
        /** hold building object */
        private final StaticObject mBuilding;
        /** hold building id */
        private final int mBuildingId;
        /** predefine time to create a unit (now it's common for all buildings) */
        private final int mUnitCreationCycleTime = 20;
        /** amount of current building instances on the planet */
        private int mBuildingsAmount;
        /** contains logic of unit creation in the cycle */
        private UnitCreatorCycle mUnitCreatorCycle;


        private BuildingsHolder(StaticObject building, int buildingId) {
            mBuilding = building;
            mBuildingId = buildingId;
        }

        /** contains logic of the new building creation */
        private void increaseBuildingsAmount() {
            LoggerHelper.methodInvocation(TAG, "increaseBuildingsAmount");
            if (!mIsFakePlanet) {
                mIncomeIncreasingValue += mBuilding.getObjectIncomeIncreasingValue();
                if (mUnitCreatorCycle == null) {
                    mUnitCreatorCycle = new UnitCreatorCycle(mPlanetTeam, mEntityOperations, mBuildingId);
                    registerUpdateHandler(new TimerHandler(mUnitCreationCycleTime, true, mUnitCreatorCycle));
                }
                mUnitCreatorCycle.increaseUnitAmount();
            }
            mBuildingsAmount += 1;
        }

        /** returns current building amount/instances on the planet */
        public int getBuildingsAmount() {
            return mBuildingsAmount;
        }
    }
}