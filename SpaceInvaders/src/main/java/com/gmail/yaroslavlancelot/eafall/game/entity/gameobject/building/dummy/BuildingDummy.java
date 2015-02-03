package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;

import org.andengine.entity.shape.Area;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;

/** dummy for all existing buildings */
public abstract class BuildingDummy {
    /** unit image height */
    protected final int mHeight;
    /** unit image width */
    protected final int mWidth;
    /** array which contain areas for team colors */
    protected Area[] mTeamColorAreaArray;
    /** unit texture region (do not create it each time when u want to create unit) */
    protected ITextureRegion[] mTextureRegionArray;
    /** you can get building name from the string resources by this id */
    protected int mBuildingStringId;

    public BuildingDummy(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {

        return mHeight;
    }

    public abstract int getUpgrades();

    public abstract int getCost(int upgrade);

    public abstract int getX();

    public abstract int getY();

    public abstract Area getTeamColorAreaArray(int upgrade);

    public ITextureRegion getTextureRegionArray(int upgrade) {
        return mTextureRegionArray[upgrade];
    }

    public abstract int getBuildingId();

    public abstract int getStringId();

    public abstract void loadResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y, String raceName);

    public abstract BuildingType getBuildingType();
}
