package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dummies;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.SpaceInvadersApplication;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.buildings.CreepBuildingLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.buildings.CreepBuildingUpgradeLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.TeamColorArea;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.entity.shape.Area;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 * Contains data needed to create building (wrap creation logic).
 * Additionally contains data which can be used without building creation (cost, image etc)
 */
public class CreepBuildingDummy extends BuildingDummy {
    /** data loaded from xml which store buildings data (in string format) */
    private CreepBuildingLoader mCreepBuildingLoader;

    public CreepBuildingDummy(CreepBuildingLoader creepBuildingLoader) {
        super(SizeConstants.BUILDING_SIZE, SizeConstants.BUILDING_SIZE);
        mCreepBuildingLoader = creepBuildingLoader;

        /* how many upgrades does the building have */
        int upgrades = mCreepBuildingLoader.getUpdates().size();
        mTeamColorAreaArray = new Area[upgrades];
        mTextureRegionArray = new ITextureRegion[upgrades];

        for (int i = 0; i < creepBuildingLoader.getUpdates().size(); i++) {
            CreepBuildingUpgradeLoader upgradeLoader = creepBuildingLoader.getUpdates().get(i);
            TeamColorArea area = upgradeLoader.team_color_area;
            mTeamColorAreaArray[i] = new Area(area.x, area.y, area.width, area.height);
            upgradeLoader.team_color_area = null;
        }

        Context context = SpaceInvadersApplication.getContext();
        mBuildingStringId = context.getResources().getIdentifier(
                mCreepBuildingLoader.name, "string", context.getApplicationInfo().packageName);
    }

    @Override
    public void loadResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y, String raceName) {
        for (int i = 0; i < mCreepBuildingLoader.getUpdates().size(); i++) {
            CreepBuildingUpgradeLoader upgradeLoader = mCreepBuildingLoader.getUpdates().get(i);
            String pathToImage = StringsAndPathUtils.getPathToBuildings(raceName) + upgradeLoader.image_name;
            GameObject.loadResource(pathToImage, context, textureAtlas, x + getWidth() * i, y + getHeight() * i);
            mTextureRegionArray[i] = TextureRegionHolderUtils.getInstance().getElement(pathToImage);
        }
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

    public int getUnitCreationTime(int upgrade) {
        return mCreepBuildingLoader.getUpdates().get(upgrade).building_time;
    }

    public int getUnitId(int upgrade) {
        return mCreepBuildingLoader.getUpdates().get(upgrade).unit_id;
    }
}
