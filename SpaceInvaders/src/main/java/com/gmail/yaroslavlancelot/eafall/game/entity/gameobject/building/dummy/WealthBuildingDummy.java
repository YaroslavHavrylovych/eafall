package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.Area;
import com.gmail.yaroslavlancelot.eafall.game.entity.TeamColorArea;
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

        TeamColorArea area = mBuildingLoader.team_color_area;
        mTeamColorAreaArray[0] = Area.getArea(area.x, area.y, area.width, area.height);
        mBuildingLoader.team_color_area = null;

        Context context = EaFallApplication.getContext();
        mBuildingStringId = context.getResources().getIdentifier(
                mBuildingLoader.name, "string", context.getApplicationInfo().packageName);

        mDescriptionStringId = context.getResources().getIdentifier(
                mBuildingLoader.name + "_description", "string", context.getApplicationInfo().packageName);
    }

    @Override
    public int getCost(int upgrade) {
        return mBuildingLoader.cost;
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
    public Area getTeamColorAreaArray(int upgrade) {
        return mTeamColorAreaArray[0];
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
    public void loadSpriteResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y, String allianceName) {
        String pathToImage = StringConstants.getPathToBuildings(allianceName) + mBuildingLoader.image_name;
        TextureRegionHolder.addElementFromAssets(pathToImage, textureAtlas, context, x + getWidth(), y + getHeight());
        mSpriteTextureRegionArray[0] = TextureRegionHolder.getInstance().getElement(pathToImage);
    }

    @Override
    public void loadImageResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y, int upgrade, String allianceName) {
        String pathToImage = StringConstants.getPathToBuildings_Image(allianceName)
                + mBuildingLoader.image_name;
        mImageTextureRegionArray[0] =
                TextureRegionHolder.addElementFromAssets(pathToImage, textureAtlas, context, x, y);
    }

    @Override
    public BuildingType getBuildingType() {
        return BuildingType.WEALTH_BUILDING;
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
}
