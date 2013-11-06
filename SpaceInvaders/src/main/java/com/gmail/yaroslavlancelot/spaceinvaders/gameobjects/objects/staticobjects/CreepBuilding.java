package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class CreepBuilding extends StaticObject {
    private static final int NO_VALUE = Integer.MAX_VALUE;

    public CreepBuilding(final float x, final float y,
                         final ITextureRegion textureRegion, final VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
        mIncomeIncreasingValue = NO_VALUE;
    }

    @Override
    public int getObjectIncomeIncreasingValue() {
        if (mIncomeIncreasingValue == NO_VALUE)
            mIncomeIncreasingValue = (int) (getObjectCost() * 0.03);
        return mIncomeIncreasingValue;
    }
}
