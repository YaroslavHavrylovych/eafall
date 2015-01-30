package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.staticobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.GameObject;

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

    public int getIncome() {
        return mIncomeIncreasingValue;
    }

    protected void setIncome(int incomeIncreasingValue) {
        mIncomeIncreasingValue = incomeIncreasingValue;
    }

    @SuppressWarnings("unused")
    public int getCost() {
        return mCost;
    }

    protected void setCost(int cost) {
        mCost = cost;
    }
}
