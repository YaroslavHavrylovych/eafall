package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringsAndPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.bonus.Bonus;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.TeamColorArea;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.SpecialBuildingLoader;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.entity.shape.Area;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;

/** dummy for special building */
public class SpecialBuildingDummy extends BuildingDummy {
    /** building id */
    private static final int BUILDING_ID = 54321;
    private SpecialBuildingLoader mBuildingLoader;
    private int mDescriptionStringId;

    public SpecialBuildingDummy(SpecialBuildingLoader buildingLoader) {
        super(Sizes.BUILDING_SIZE, Sizes.BUILDING_SIZE);
        mBuildingLoader = buildingLoader;

        /* how many upgrades does the building have */
        mTeamColorAreaArray = new Area[1];
        mTextureRegionArray = new ITextureRegion[1];

        TeamColorArea area = mBuildingLoader.team_color_area;
        mTeamColorAreaArray[0] = new Area(area.x, area.y, area.width, area.height);
        mBuildingLoader.team_color_area = null;

        Context context = EaFallApplication.getContext();
        mBuildingStringId = context.getResources().getIdentifier(
                mBuildingLoader.name, "string", context.getApplicationInfo().packageName);

        mDescriptionStringId = context.getResources().getIdentifier(
                mBuildingLoader.name + "_description", "string", context.getApplicationInfo().packageName);
    }

    @Override
    public int getUpgrades() {
        return 0;
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
    public void loadResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y, String raceName) {
        String pathToImage = StringsAndPath.getPathToBuildings(raceName) + mBuildingLoader.image_name;
        GameObject.loadResource(pathToImage, context, textureAtlas, x + getWidth(), y + getHeight());
        mTextureRegionArray[0] = TextureRegionHolder.getInstance().getElement(pathToImage);
    }

    @Override
    public BuildingType getBuildingType() {
        return BuildingType.SPECIAL_BUILDING;
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
}