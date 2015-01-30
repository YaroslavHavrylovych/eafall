package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.staticobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class SunStaticObject extends StaticObject {
    public SunStaticObject(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
        setWidth(SizeConstants.SUN_DIAMETER);
        setHeight(SizeConstants.SUN_DIAMETER);
        mIncomeIncreasingValue = 0;
    }
}
