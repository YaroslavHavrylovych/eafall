package com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy;

import android.content.Context;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.UnitBuildingLoader;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.UnitBuildingUpgradeLoader;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

/**
 * Contains data needed to create building (wrap creation logic).
 * Additionally contains data which can be used without building creation (cost, image etc)`
 */
public class OffenceBuildingDummy extends UnitBuildingDummy {
    /** data loaded from xml which store buildings data (in string format) */
    private UnitBuildingLoader mUnitBuildingLoader;

    public OffenceBuildingDummy(UnitBuildingLoader buildingLoader) {
        super(SizeConstants.BUILDING_SIZE, SizeConstants.BUILDING_SIZE,
                buildingLoader.getUpdates().size());
        mUnitBuildingLoader = buildingLoader;

        Context context = EaFallApplication.getContext();
        mBuildingStringId = context.getResources().getIdentifier(
                mUnitBuildingLoader.name, "string", context.getApplicationInfo().packageName);
    }

    @Override
    public int getX() {
        return mUnitBuildingLoader.position_x;
    }

    @Override
    public int getY() {
        return mUnitBuildingLoader.position_y;
    }

    @Override
    public int getBuildingId() {
        return mUnitBuildingLoader.id;
    }

    @Override
    public int getStringId() {
        return mBuildingStringId;
    }

    @Override
    public BuildingType getBuildingType() {
        return BuildingType.CREEP_BUILDING;
    }

    @Override
    public int getAmountLimit() {
        return EaFallApplication.getConfig().getUnitBuildingsLimit();
    }

    @Override
    public int getCost(int upgrade) {
        return mUnitBuildingLoader.getUpdates().get(upgrade).cost;
    }

    @Override
    public void loadSpriteResources(Context context, BitmapTextureAtlas textureAtlas,
                                    int x, int y, String allianceName) {
        for (int i = 0; i < mUnitBuildingLoader.getUpdates().size(); i++) {
            UnitBuildingUpgradeLoader upgradeLoader = mUnitBuildingLoader.getUpdates().get(i);
            String pathToSprite = StringConstants.getPathToBuildings(allianceName) + upgradeLoader.image_name;
            mSpriteTextureRegionArray[i] = TextureRegionHolder
                    .addElementFromAssets(pathToSprite, textureAtlas, context,
                            x + getWidth() * i, y + getHeight() * i);
        }
    }

    @Override
    public void loadImageResources(Context context, BitmapTextureAtlas textureAtlas,
                                   int x, int y, int upgrade, String allianceName) {
        UnitBuildingUpgradeLoader unitUpdate = mUnitBuildingLoader.getUpdates().get(upgrade);
        String pathToImage = StringConstants.getPathToBuildings_Image(allianceName)
                + unitUpdate.image_name;
        mImageTextureRegionArray[upgrade] =
                TextureRegionHolder.addElementFromAssets(pathToImage, textureAtlas, context, x, y);
    }

    @Override
    public int getUnitCreationTime(int upgrade) {
        return mUnitBuildingLoader.getUpdates().get(upgrade).building_time;
    }

    @Override
    public int getUnitId(int upgrade) {
        return mUnitBuildingLoader.getUpdates().get(upgrade).unit_id;
    }
}
