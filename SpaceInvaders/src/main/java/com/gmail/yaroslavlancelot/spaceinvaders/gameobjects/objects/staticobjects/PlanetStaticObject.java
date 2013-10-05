package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.HashMap;
import java.util.Map;

/** represent team planet */
public class PlanetStaticObject extends StaticObject {
    // unit spawn point
    private float mSpawnPointX, mSpawnPointY;
    // buildings in current planet
    private Map<String, BuildingsHolder> buildings = new HashMap<String, BuildingsHolder>(15);
    /** current team money amount */
    private int mMoneyAmount;

    public PlanetStaticObject(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
        mIncomeIncreasingValue = 10;
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
        final String key = GameStringConstants.KEY_FIRST_BUILDING;
        if (buildings.get(key) == null) {
            final FirstBuildingStaticObject staticObject = new FirstBuildingStaticObject(
                    16 - 3, 5f, TextureRegionHolderUtils.getInstance().getElement(
                    key), getVertexBufferObjectManager());
            staticObject.setWidth(10);
            staticObject.setHeight(10);
            buildings.put(key, new BuildingsHolder(GameStringConstants.KEY_FIRST_BUILDING, staticObject));
        }
        addBuilding(key);
    }

    private void addBuilding(String key) {
        BuildingsHolder holder = buildings.get(key);
        StaticObject building = buildings.get(key).mStaticObject;
        if (mMoneyAmount < building.mCost)
            return;
        if(holder.mBuildingsAmount == 0)
            attachChild(building);
        mMoneyAmount -= building.mCost;
        holder.increaseBuildingsAmount();
        mIncomeIncreasingValue += building.getObjectIncomeIncreasingValue();
    }

    /** build second building */
    public void buildSecondBuilding() {
        final String key = GameStringConstants.KEY_SECOND_BUILDING;
        if (buildings.get(key) == null) {
            StaticObject staticObject = new FirstBuildingStaticObject(16 - 3, 22f, TextureRegionHolderUtils.getInstance().getElement(
                    key), getVertexBufferObjectManager());
            staticObject.setWidth(10);
            staticObject.setHeight(10);
            attachChild(staticObject);
            buildings.put(key, new BuildingsHolder(GameStringConstants.KEY_SECOND_BUILDING, staticObject));
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
        private final String mKey;
        private final StaticObject mStaticObject;
        private int mBuildingsAmount;

        private BuildingsHolder(String key, StaticObject staticObject) {
            mKey = key;
            mStaticObject = staticObject;
        }

        public void increaseBuildingsAmount() {
            mBuildingsAmount += 1;
        }

        public void decreaseBuildingsAmount() {
            mBuildingsAmount -= 1;
        }
    }
}