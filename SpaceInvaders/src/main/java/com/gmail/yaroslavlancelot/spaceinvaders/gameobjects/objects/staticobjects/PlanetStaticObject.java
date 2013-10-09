package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameloop.UnitCreatorCycle;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.UnitFactory;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
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
    private Map<String, BuildingsHolder> buildings = new HashMap<String, BuildingsHolder>(15);
    /** current team money amount */
    private int mMoneyAmount;
    /** for creating new units */
    private EntityOperations mEntityOperations;
    /** */
    private ITeam mPlanetTeam;

    public PlanetStaticObject(float x, float y, ITextureRegion textureRegion, EntityOperations entityOperations, ITeam planetTeam) {
        super(x, y, textureRegion, entityOperations.getObjectManager());
        mEntityOperations = entityOperations;
        mIncomeIncreasingValue = 10;
        mPlanetTeam = planetTeam;
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

    /** build first building */
    public void buildFirstBuilding() {
        LoggerHelper.methodInvocation(TAG, "buildFirstBuilding");
        final String key = GameStringConstants.KEY_FIRST_BUILDING;
        if (buildings.get(key) == null) {
            final FirstBuildingStaticObject staticObject = new FirstBuildingStaticObject(
                    16 - 3, 5f, TextureRegionHolderUtils.getInstance().getElement(
                    key), getVertexBufferObjectManager());
            staticObject.setWidth(10);
            staticObject.setHeight(10);
            buildings.put(key, new BuildingsHolder(staticObject, UnitFactory.HANDS_ATTACKER));
        }
        addBuilding(key);
    }

    private void addBuilding(String key) {
        BuildingsHolder holder = buildings.get(key);
        StaticObject building = buildings.get(key).mStaticObject;
        LoggerHelper.printDebugMessage(TAG, "building creation : " + "existing money=" + mMoneyAmount
                + ", cost=" + building.mCost);
        if (mMoneyAmount < building.mCost)
            return;
        if (holder.mBuildingsAmount == 0) {
            LoggerHelper.printInformationMessage(TAG, "creating building on planet");
            attachChild(building);
        }
        holder.increaseBuildingsAmount();
        mMoneyAmount -= building.mCost;
        mIncomeIncreasingValue += building.getObjectIncomeIncreasingValue();
    }

    /** build second building */
    public void buildSecondBuilding() {
        LoggerHelper.methodInvocation(TAG, "buildSecondBuilding");
        final String key = GameStringConstants.KEY_SECOND_BUILDING;
        if (buildings.get(key) == null) {
            StaticObject staticObject = new FirstBuildingStaticObject(16 - 3, 22f, TextureRegionHolderUtils.getInstance().getElement(
                    key), getVertexBufferObjectManager());
            staticObject.setWidth(10);
            staticObject.setHeight(10);
            buildings.put(key, new BuildingsHolder(staticObject, UnitFactory.HANDS_ATTACKER));
        }
        addBuilding(key);
    }

    public int getMoneyAmount() {
        return mMoneyAmount;
    }

    public void setMoneyAmount(final int value) {
        mMoneyAmount = value;
    }

    private class BuildingsHolder {
        private final StaticObject mStaticObject;
        private int mBuildingsAmount;
        private UnitCreatorCycle mUnitCreatorCycle;
        private int mUnitKey;


        private BuildingsHolder(StaticObject staticObject, int unitKey) {
            mStaticObject = staticObject;
            mUnitKey = unitKey;
        }

        public void increaseBuildingsAmount() {
            if (mUnitCreatorCycle == null) {
                mUnitCreatorCycle = new UnitCreatorCycle(mPlanetTeam, mEntityOperations, mUnitKey);
                registerUpdateHandler(new TimerHandler(2, true, mUnitCreatorCycle));
            }
            mBuildingsAmount += 1;
            mUnitCreatorCycle.increaseUnitAmount();
        }
    }
}