package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.WealthBuildingLoader;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

/** dummy for wealth building */
public class WealthBuildingDummy extends BuildingDummy {
    /** building id */
    private static final int BUILDING_ID = 12345;
    private WealthBuildingLoader mBuildingLoader;
    private int mDescriptionStringId;

    public WealthBuildingDummy(WealthBuildingLoader buildingLoader) {
        super(SizeConstants.BUILDING_SIZE, SizeConstants.BUILDING_SIZE, 1);
        mBuildingLoader = buildingLoader;

        Context context = EaFallApplication.getContext();
        mBuildingStringId = context.getResources().getIdentifier(
                mBuildingLoader.name, "string", context.getApplicationInfo().packageName);

        mDescriptionStringId = context.getResources().getIdentifier(
                mBuildingLoader.name + "_description", "string", context.getApplicationInfo().packageName);
    }

    public int getDescriptionStringId() {
        return mDescriptionStringId;
    }

    public int getFirstBuildingIncome() {
        return mBuildingLoader.first_build_income;
    }

    public int getNextBuildingsIncome() {
        return mBuildingLoader.next_buildings_income;
    }

    @Override
    public int getX() {
        return mBuildingLoader.position_x;
    }

    @Override
    public int getY() {
        return mBuildingLoader.position_y;
    }

    @Override
    public int getBuildingId() {
        return BUILDING_ID;
    }

    @Override
    public int getStringId() {
        return mBuildingStringId;
    }

    @Override
    public BuildingType getBuildingType() {
        return BuildingType.WEALTH_BUILDING;
    }

    @Override
    public int getAmountLimit() {
        return EaFallApplication.getConfig().getWealthBuildingsLimit();
    }

    @Override
    public int getCost(int upgrade) {
        return mBuildingLoader.cost;
    }

    @Override
    public void loadSpriteResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y, String allianceName) {
        String pathToImage = StringConstants.getPathToBuildings(allianceName) + mBuildingLoader.image_name;
        TextureRegionHolder.addElementFromAssets(pathToImage, textureAtlas, context, x, y);
        mSpriteTextureRegionArray[0] = TextureRegionHolder.getInstance().getElement(pathToImage);
    }

    @Override
    public void loadImageResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y, int upgrade, String allianceName) {
        String pathToImage = StringConstants.getPathToBuildings_Image(allianceName)
                + mBuildingLoader.image_name;
        mImageTextureRegionArray[0] =
                TextureRegionHolder.addElementFromAssets(pathToImage, textureAtlas, context, x, y);
    }
}
