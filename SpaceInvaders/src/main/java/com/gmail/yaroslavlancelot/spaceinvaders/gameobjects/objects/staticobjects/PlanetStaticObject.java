package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.List;

/** represent team planet */
public class PlanetStaticObject extends StaticObject {
    // unit spawn point
    private float mSpawnPointX, mSpawnPointY;
    // buildings in current planet
    private List<StaticObject> buildings = new ArrayList<StaticObject>(15);

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
        final FirstBuildingStaticObject staticObject = new FirstBuildingStaticObject(16 - 3, 5f, TextureRegionHolderUtils.getInstance().getElement(
                GameStringConstants.KEY_FIRST_BUILDING), getVertexBufferObjectManager());
        attachChild(staticObject);
        buildings.add(staticObject);
        mIncomeIncreasingValue += staticObject.getObjectIncomeIncreasingValue();
    }

    /** build second building */
    public void buildSecondBuilding() {
        StaticObject staticObject = new FirstBuildingStaticObject(16 - 3, 22f, TextureRegionHolderUtils.getInstance().getElement(
                GameStringConstants.KEY_SECOND_BUILDING), getVertexBufferObjectManager());
        attachChild(staticObject);
        buildings.add(staticObject);
        mIncomeIncreasingValue += staticObject.getObjectIncomeIncreasingValue();
    }
}