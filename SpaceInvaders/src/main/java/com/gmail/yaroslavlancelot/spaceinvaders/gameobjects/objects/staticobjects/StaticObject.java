package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** Basic class for all dynamic units in the game */
public abstract class StaticObject extends GameObject {
    /** amount of money that brings current object */
    protected int mIncomeIncreasingValue;
    /** how much this object is cost */
    protected int mCost;

    public StaticObject(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
    }

    public int getObjectIncomeIncreasingValue() {
        return mIncomeIncreasingValue;
    }

    @SuppressWarnings("unused")
    public int getObjectCost() {
        return mCost;
    }
}
