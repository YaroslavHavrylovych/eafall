package com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy;

import android.content.Context;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.DefenceBuildingLoader;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

/** for creation orbital stations (defence buildings) */
public class DefenceBuildingDummy extends UnitBuildingDummy {
    /** building id */
    private static final int BUILDING_ID = 250;
    /** data loaded from xml which store buildings data (in string format) */
    private DefenceBuildingLoader mDefenceBuildingLoader;

    public DefenceBuildingDummy(DefenceBuildingLoader buildingLoader) {
        super(SizeConstants.BUILDING_SIZE, SizeConstants.BUILDING_SIZE, 1);
        mDefenceBuildingLoader = buildingLoader;

        Context context = EaFallApplication.getContext();
        mBuildingStringId = context.getResources().getIdentifier(
                buildingLoader.name, "string", context.getApplicationInfo().packageName);
    }

    @Override
    public int getUnitCreationTime(int upgrade) {
        return mDefenceBuildingLoader.building_time;
    }

    @Override
    public int getUnitId(int upgrade) {
        return mDefenceBuildingLoader.unit_id;
    }

    @Override
    public int getX() {
        return mDefenceBuildingLoader.position_x;
    }

    @Override
    public int getY() {
        return mDefenceBuildingLoader.position_y;
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
        return BuildingType.DEFENCE_BUILDING;
    }

    @Override
    public int getAmountLimit() {
        return 1;
    }

    @Override
    public int getCost(int upgrade) {
        return mDefenceBuildingLoader.cost;
    }

    @Override
    public void loadSpriteResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y, String allianceName) {
        String pathToImage = StringConstants.getPathToBuildings(allianceName) + mDefenceBuildingLoader.image_name;
        mSpriteTextureRegionArray[0] = TextureRegionHolder.addElementFromAssets(
                pathToImage, textureAtlas, context, x, y);
    }

    @Override
    public void loadImageResources(Context context, BitmapTextureAtlas textureAtlas,
                                   int x, int y, int upgrade, String allianceName) {
        String pathToImage = StringConstants.getPathToBuildings_Image(allianceName)
                + mDefenceBuildingLoader.image_name;
        mImageTextureRegionArray[0] =
                TextureRegionHolder.addElementFromAssets(pathToImage, textureAtlas, context, x, y);
    }
}
