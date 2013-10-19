package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** first building of imperials */
public class FirstImperialsBuilding extends StaticObject {

    public FirstImperialsBuilding(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
        mIncomeIncreasingValue = 5;
        mCost = 100;
        setWidth(SizeConstants.BUILDING_DIAMETER);
        setHeight(SizeConstants.BUILDING_DIAMETER);
    }
}
