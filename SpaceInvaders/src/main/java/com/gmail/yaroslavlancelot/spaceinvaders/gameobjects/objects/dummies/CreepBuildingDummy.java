package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dummies;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.SpaceInvadersApplication;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.BuildingLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.BuildingUpgradeLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.TeamColorArea;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.entity.shape.Area;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 * Contains data needed to create building (wrap creation logic).
 * Additionally contains data which can be used without building creation (cost, image etc)
 */
public class CreepBuildingDummy {
    /** unit image height */
    private final int mHeight;
    /** unit image width */
    private final int mWidth;
    /** array which contain areas for team colors */
    private final Area[] mTeamColorAreaArray;
    /** data loaded from xml which store buildings data (in string format) */
    private BuildingLoader mBuildingLoader;
    /** unit texture region (do not create it each time when u want to create unit) */
    private ITextureRegion[] mTextureRegionArray;
    /** you can get building name from the string resources by this id */
    private int mBuildingStringId;

    public CreepBuildingDummy(BuildingLoader buildingLoader) {
        mBuildingLoader = buildingLoader;
        mHeight = SizeConstants.BUILDING_SIZE;
        mWidth = SizeConstants.BUILDING_SIZE;

        /* how many upgrades does the building have */
        int upgrades = mBuildingLoader.getUpdates().size();
        mTeamColorAreaArray = new Area[upgrades];
        mTextureRegionArray = new ITextureRegion[upgrades];

        for (int i = 0; i < buildingLoader.getUpdates().size(); i++) {
            BuildingUpgradeLoader upgradeLoader = buildingLoader.getUpdates().get(i);
            TeamColorArea area = upgradeLoader.team_color_area;
            mTeamColorAreaArray[i] = new Area(area.x, area.y, area.width, area.height);
            upgradeLoader.team_color_area = null;
        }

        Context context = SpaceInvadersApplication.getContext();
        mBuildingStringId = context.getResources().getIdentifier(
                mBuildingLoader.name, "string", context.getApplicationInfo().packageName);
    }

    public void loadResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y, String raceName) {
        for (int i = 0; i < mBuildingLoader.getUpdates().size(); i++) {
            BuildingUpgradeLoader upgradeLoader = mBuildingLoader.getUpdates().get(i);
            String pathToImage = StringsAndPathUtils.getPathToBuildings(raceName) + upgradeLoader.image_name;
            GameObject.loadResource(pathToImage, context, textureAtlas, x + getWidth() * i, y + getHeight() * i);
            mTextureRegionArray[i] = TextureRegionHolderUtils.getInstance().getElement(pathToImage);
        }
    }

    public int getUpgrades() {
        return mBuildingLoader.getUpdates().size();
    }

    public int getCost(int upgrade) {
        return mBuildingLoader.getUpdates().get(upgrade).cost;
    }

    public int getUnitId(int upgrade) {
        return mBuildingLoader.getUpdates().get(upgrade).unit_id;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getX() {
        return mBuildingLoader.position_x;
    }

    public int getY() {
        return mBuildingLoader.position_y;
    }

    public Area getTeamColorAreaArray(int upgrade) {
        return mTeamColorAreaArray[upgrade];
    }

    public ITextureRegion getTextureRegionArray(int upgrade) {
        return mTextureRegionArray[upgrade];
    }

    public int getBuildingId() {
        return mBuildingLoader.id;
    }

    public int getStringId() {
        return mBuildingStringId;
    }
}
