package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameObjectsConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.List;

/**  */
public class PlanetStaticObject extends StaticObject {
    private float mSpawnPointX, mSpawnPointY;
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

    public void buildFirstBuilding() {
        final FirstBuildingStaticObject staticObject = new FirstBuildingStaticObject(16 - 3, 5f, TextureRegionHolderUtils.getInstance().getElement(
                GameObjectsConstants.KEY_FIRST_BUILDING), getVertexBufferObjectManager());
        attachChild(staticObject);
        buildings.add(staticObject);
        mIncomeIncreasingValue += staticObject.getObjectIncomeIncreasingValue();
    }

    public void buildSecondBuilding() {
        StaticObject staticObject = new FirstBuildingStaticObject(16 - 3, 22f, TextureRegionHolderUtils.getInstance().getElement(
                GameObjectsConstants.KEY_SECOND_BUILDING), getVertexBufferObjectManager());
        attachChild(staticObject);
        buildings.add(staticObject);
        mIncomeIncreasingValue += staticObject.getObjectIncomeIncreasingValue();
    }
}