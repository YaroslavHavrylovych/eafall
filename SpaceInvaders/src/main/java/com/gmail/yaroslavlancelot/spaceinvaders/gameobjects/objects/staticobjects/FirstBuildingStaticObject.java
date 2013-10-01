package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** first building object */
public class FirstBuildingStaticObject extends StaticObject {
    public FirstBuildingStaticObject(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
        mIncomeIncreasingValue = 5;
    }
}
