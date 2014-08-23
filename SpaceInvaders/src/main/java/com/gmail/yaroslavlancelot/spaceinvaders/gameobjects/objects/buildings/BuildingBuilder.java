package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** building builder */
public class BuildingBuilder {
    private ITextureRegion mTextureRegion;
    private VertexBufferObjectManager mObjectManager;
    private int mWidth;
    private int mHeight;
    private int mCost;
    private int mObjectStringId;
    private float mX, mY;

    public BuildingBuilder(ITextureRegion textureRegion, VertexBufferObjectManager objectManager) {
        mTextureRegion = textureRegion;
        mObjectManager = objectManager;
    }

    public BuildingBuilder setPosition(float x, float y) {
        mX = x;
        mY = y;
        return this;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public ITextureRegion getTextureRegion() {
        return mTextureRegion;
    }

    public VertexBufferObjectManager getObjectManager() {
        return mObjectManager;
    }

    public int getWidth() {
        return mWidth;
    }

    public BuildingBuilder setWidth(int width) {
        mWidth = width;
        return this;
    }

    public int getHeight() {
        return mHeight;
    }

    public BuildingBuilder setHeight(int height) {
        mHeight = height;
        return this;
    }

    public int getCost() {
        return mCost;
    }

    public BuildingBuilder setCost(int cost) {
        mCost = cost;
        return this;
    }

    public int getObjectStringId() {
        return mObjectStringId;
    }

    public BuildingBuilder setObjectStringId(int objectStringId) {
        mObjectStringId = objectStringId;
        return this;
    }
}
