package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** first building of imperials */
public class Building extends StaticObject {

    public Building(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager, int cost, int income) {
        super(x, y, textureRegion, vertexBufferObjectManager);
        mIncomeIncreasingValue = income;
        mCost = cost;
        setWidth(SizeConstants.BUILDING_DIAMETER);
        setHeight(SizeConstants.BUILDING_DIAMETER);
    }
}
