package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.TeamColorArea;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.CreepBuildingLoader;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.CreepBuildingUpgradeLoader;

import com.gmail.yaroslavlancelot.eafall.game.entity.Area;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 * Contains data needed to create building (wrap creation logic).
 * Additionally contains data which can be used without building creation (cost, image etc)`
 */
public class CreepBuildingDummy extends BuildingDummy {
    /** data loaded from xml which store buildings data (in string format) */
    private CreepBuildingLoader mCreepBuildingLoader;

    public CreepBuildingDummy(CreepBuildingLoader buildingLoader) {
        super(SizeConstants.BUILDING_SIZE, SizeConstants.BUILDING_SIZE);
        mCreepBuildingLoader = buildingLoader;

        /* how many upgrades does the building have */
        int upgrades = mCreepBuildingLoader.getUpdates().size();
        mTeamColorAreaArray = new Area[upgrades];
        mTextureRegionArray = new ITextureRegion[upgrades];

        for (int i = 0; i < buildingLoader.getUpdates().size(); i++) {
            CreepBuildingUpgradeLoader upgradeLoader = buildingLoader.getUpdates().get(i);
            TeamColorArea area = upgradeLoader.team_color_area;
            mTeamColorAreaArray[i] = Area.getArea(area.x, area.y, area.width, area.height);
            upgradeLoader.team_color_area = null;
        }

        Context context = EaFallApplication.getContext();
        mBuildingStringId = context.getResources().getIdentifier(
                mCreepBuildingLoader.name, "string", context.getApplicationInfo().packageName);
    }

    @Override
    public int getUpgrades() {
        return mCreepBuildingLoader.getUpdates().size();
    }

    @Override
    public int getCost(int upgrade) {
        return mCreepBuildingLoader.getUpdates().get(upgrade).cost;
    }

    @Override
    public int getX() {
        return mCreepBuildingLoader.position_x;
    }

    @Override
    public int getY() {
        return mCreepBuildingLoader.position_y;
    }

    @Override
    public Area getTeamColorAreaArray(int upgrade) {
        return mTeamColorAreaArray[upgrade];
    }

    @Override
    public int getBuildingId() {
        return mCreepBuildingLoader.id;
    }

    @Override
    public int getStringId() {
        return mBuildingStringId;
    }

    @Override
    public void loadResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y, String raceName) {
        for (int i = 0; i < mCreepBuildingLoader.getUpdates().size(); i++) {
            CreepBuildingUpgradeLoader upgradeLoader = mCreepBuildingLoader.getUpdates().get(i);
            String pathToImage = StringConstants.getPathToBuildings(raceName) + upgradeLoader.image_name;
            BodiedSprite.loadResource(pathToImage, context, textureAtlas, x + getWidth() * i, y + getHeight() * i);
            mTextureRegionArray[i] = TextureRegionHolder.getInstance().getElement(pathToImage);
        }
    }

    @Override
    public BuildingType getBuildingType() {
        return BuildingType.CREEP_BUILDING;
    }

    public int getUnitCreationTime(int upgrade) {
        return mCreepBuildingLoader.getUpdates().get(upgrade).building_time;
    }

    public int getMovableUnitId(int upgrade) {
        return mCreepBuildingLoader.getUpdates().get(upgrade).unit_id;
    }
}
