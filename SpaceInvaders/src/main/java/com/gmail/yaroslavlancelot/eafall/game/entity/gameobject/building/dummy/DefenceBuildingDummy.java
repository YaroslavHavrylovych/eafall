package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.Area;
import com.gmail.yaroslavlancelot.eafall.game.entity.TeamColorArea;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.DefenceBuildingLoader;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

/** for creation orbital stations (defence buildings) */
public class DefenceBuildingDummy extends BuildingDummy {
    /** building id */
    private static final int BUILDING_ID = 250;
    /** data loaded from xml which store buildings data (in string format) */
    private DefenceBuildingLoader mDefenceBuildingLoader;

    public DefenceBuildingDummy(DefenceBuildingLoader buildingLoader) {
        super(SizeConstants.BUILDING_SIZE, SizeConstants.BUILDING_SIZE, 1);
        mDefenceBuildingLoader = buildingLoader;

        TeamColorArea area = buildingLoader.team_color_area;
        mTeamColorAreaArray[0] = Area.getArea(area.x, area.y, area.width, area.height);
        buildingLoader.team_color_area = null;

        Context context = EaFallApplication.getContext();
        mBuildingStringId = context.getResources().getIdentifier(
                buildingLoader.name, "string", context.getApplicationInfo().packageName);
    }

    @Override
    public int getCost(int upgrade) {
        return mDefenceBuildingLoader.cost;
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
        String pathToImage = StringConstants.getPathToBuildings(allianceName) + mDefenceBuildingLoader.image_name;
        mSpriteTextureRegionArray[0] = TextureRegionHolder.addElementFromAssets(
                pathToImage, textureAtlas, context, x + getWidth(), y + getHeight());
    }

    @Override
    public void loadImageResources(Context context, BitmapTextureAtlas textureAtlas,
                                   int x, int y, int upgrade, String allianceName) {
        String pathToImage = StringConstants.getPathToBuildings_Image(allianceName)
                + mDefenceBuildingLoader.image_name;
        mImageTextureRegionArray[0] =
                TextureRegionHolder.addElementFromAssets(pathToImage, textureAtlas, context, x, y);
    }

    @Override
    public BuildingType getBuildingType() {
        return BuildingType.DEFENCE_BUILDING;
    }

    public int getOrbitalStationCreationTime() {
        return mDefenceBuildingLoader.unit_id;
    }

    public int getOrbitalStationUnitId() {
        return mDefenceBuildingLoader.unit_id;
    }
}
