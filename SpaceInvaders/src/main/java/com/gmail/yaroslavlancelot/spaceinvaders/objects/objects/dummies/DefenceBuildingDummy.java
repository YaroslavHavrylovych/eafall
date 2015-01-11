package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dummies;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.SpaceInvadersApplication;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.BuildingType;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.loading.TeamColorArea;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.loading.buildings.DefenceBuildingLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.entity.shape.Area;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;

/** for creation orbital stations (defence buildings) */
public class DefenceBuildingDummy extends BuildingDummy {
    /** building id */
    private static final int BUILDING_ID = 250;
    /** data loaded from xml which store buildings data (in string format) */
    private DefenceBuildingLoader mDefenceBuildingLoader;

    public DefenceBuildingDummy(DefenceBuildingLoader buildingLoader) {
        super(SizeConstants.BUILDING_SIZE, SizeConstants.BUILDING_SIZE);
        mDefenceBuildingLoader = buildingLoader;

        /* how many upgrades does the building have */
        mTeamColorAreaArray = new Area[1];
        mTextureRegionArray = new ITextureRegion[1];

        TeamColorArea area = buildingLoader.team_color_area;
        mTeamColorAreaArray[0] = new Area(area.x, area.y, area.width, area.height);
        buildingLoader.team_color_area = null;

        Context context = SpaceInvadersApplication.getContext();
        mBuildingStringId = context.getResources().getIdentifier(
                buildingLoader.name, "string", context.getApplicationInfo().packageName);
    }

    @Override
    public int getUpgrades() {
        return 0;
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
    public void loadResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y, String raceName) {
        String pathToImage = StringsAndPathUtils.getPathToBuildings(raceName) + mDefenceBuildingLoader.image_name;
        GameObject.loadResource(pathToImage, context, textureAtlas, x + getWidth(), y + getHeight());
        mTextureRegionArray[0] = TextureRegionHolderUtils.getInstance().getElement(pathToImage);
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
