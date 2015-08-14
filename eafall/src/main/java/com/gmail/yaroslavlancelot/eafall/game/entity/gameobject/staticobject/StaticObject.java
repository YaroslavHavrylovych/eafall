package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;

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
    
    public StaticObject(float x, float y, float width ,float height, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, width, height, textureRegion, vertexBufferObjectManager);
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
