package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.entity.Area;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;

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
    /** building image (big one) texture region */
    protected ITextureRegion[] mSpriteTextureRegionArray;
    /** building image (big one) texture region */
    protected ITextureRegion[] mImageTextureRegionArray;
    /** you can get building name from the string resources by this id */
    protected int mBuildingStringId;
    /** upgrades amount */
    protected final int mUpgrades;

    public BuildingDummy(int width, int height, int upgrades) {
        mWidth = width;
        mHeight = height;
        mUpgrades = upgrades;
        mSpriteTextureRegionArray = new ITextureRegion[upgrades];
        mImageTextureRegionArray = new ITextureRegion[upgrades];
        mTeamColorAreaArray = new Area[upgrades];
    }

    public int getUpgrades() {
        return mUpgrades;
    }


    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {

        return mHeight;
    }

    public abstract int getCost(int upgrade);

    public abstract int getX();

    public abstract int getY();

    public abstract Area getTeamColorAreaArray(int upgrade);

    public ITextureRegion getSpriteTextureRegionArray(int upgrade) {
        return mSpriteTextureRegionArray[upgrade];
    }

    public ITextureRegion getImageTextureRegionArray(int upgrade) {
        return mImageTextureRegionArray[upgrade];
    }

    public abstract int getBuildingId();

    public abstract int getStringId();

    public abstract void loadSpriteResources(Context context, BitmapTextureAtlas textureAtlas,
                                             int x, int y, String allianceName);

    public abstract void loadImageResources(Context context, BitmapTextureAtlas textureAtlas,
                                            int x, int y, int upgrade, String allianceName);

    public abstract BuildingType getBuildingType();
}
