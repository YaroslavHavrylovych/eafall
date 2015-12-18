package com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy;

import android.content.Context;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.SpecialBuildingLoader;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.bonus.Bonus;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

/** dummy for special building */
public class SpecialBuildingDummy extends BuildingDummy {
    /** building id */
    public static final int BUILDING_ID = 54321;
    private SpecialBuildingLoader mBuildingLoader;
    private int mDescriptionStringId;

    public SpecialBuildingDummy(SpecialBuildingLoader buildingLoader) {
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

    /** return building bonus */
    public Bonus getBonus() {
        String characteristic = mBuildingLoader.characteristic;
        if (characteristic.startsWith("attack")) {
            Bonus.BonusType type = mBuildingLoader.percentage ? Bonus.BonusType.ATTACK_PERCENTS : Bonus.BonusType.ATTACK;
            return Bonus.getBonus(mBuildingLoader.value, type, true);
        }
        if (characteristic.startsWith("defence")) {
            Bonus.BonusType type = mBuildingLoader.percentage ? Bonus.BonusType.DEFENCE_PERCENTS : Bonus.BonusType.DEFENCE;
            return Bonus.getBonus(mBuildingLoader.value, type, true);
        }
        if (characteristic.contains("avoid_attack")) {
            return Bonus.getBonus(mBuildingLoader.value, Bonus.BonusType.AVOID_ATTACK_CHANCE, true);
        }
        if (characteristic.startsWith("health")) {
            Bonus.BonusType type = mBuildingLoader.percentage ? Bonus.BonusType.HEALTH_PERCENTS : Bonus.BonusType.HEALTH;
            return Bonus.getBonus(mBuildingLoader.value, type, true);
        }
        return null;
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
        return BuildingType.SPECIAL_BUILDING;
    }

    @Override
    public int getAmountLimit() {
        return 1;
    }

    @Override
    public int getCost(int upgrade) {
        return mBuildingLoader.cost;
    }

    @Override
    public void loadSpriteResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y, String allianceName) {
        String pathToImage = StringConstants.getPathToBuildings(allianceName) + mBuildingLoader.image_name;
        mSpriteTextureRegionArray[0] = TextureRegionHolder.addElementFromAssets(
                pathToImage, textureAtlas, context, x, y);
    }

    @Override
    public void loadImageResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y, int upgrade, String allianceName) {
        String pathToImage = StringConstants.getPathToBuildings_Image(allianceName)
                + mBuildingLoader.image_name;
        mImageTextureRegionArray[0] =
                TextureRegionHolder.addElementFromAssets(pathToImage, textureAtlas, context, x, y);
    }
}
