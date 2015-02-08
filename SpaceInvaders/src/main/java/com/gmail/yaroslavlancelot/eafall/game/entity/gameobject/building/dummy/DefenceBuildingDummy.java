package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringsAndPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.TeamColorArea;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.DefenceBuildingLoader;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

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
        super(Sizes.BUILDING_SIZE, Sizes.BUILDING_SIZE);
        mDefenceBuildingLoader = buildingLoader;

        /* how many upgrades does the building have */
        mTeamColorAreaArray = new Area[1];
        mTextureRegionArray = new ITextureRegion[1];

        TeamColorArea area = buildingLoader.team_color_area;
        mTeamColorAreaArray[0] = Area.getArea(area.x, area.y, area.width, area.height);
        buildingLoader.team_color_area = null;

        Context context = EaFallApplication.getContext();
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
        String pathToImage = StringsAndPath.getPathToBuildings(raceName) + mDefenceBuildingLoader.image_name;
        GameObject.loadResource(pathToImage, context, textureAtlas, x + getWidth(), y + getHeight());
        mTextureRegionArray[0] = TextureRegionHolder.getInstance().getElement(pathToImage);
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
