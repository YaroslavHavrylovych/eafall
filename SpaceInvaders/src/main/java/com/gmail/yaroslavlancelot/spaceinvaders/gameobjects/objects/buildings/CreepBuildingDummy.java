package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.SpaceInvadersApplication;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.BuildingLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.TeamColorArea;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Contains data needed to create building (wrap creation logic).
 * Additionally contains data which can be used without building creation (cost, image etc)
 */
public class CreepBuildingDummy {
    /** path to building image so it can be retrieved separate from unit creation if needed */
    private final String mPathToImage;
    /** unit image height */
    private final int mHeight;
    /** unit image width */
    private final int mWidth;
    /** area which contains team colors */
    private final Area mTeamColorArea;
    /** data loaded from xml which store buildings data (in string format) */
    private BuildingLoader mBuildingLoader;
    /** unit texture region (do not create it each time when u want to create unit) */
    private ITextureRegion mTextureRegion;

    public CreepBuildingDummy(BuildingLoader buildingLoader) {
        mBuildingLoader = buildingLoader;
        mPathToImage = GameStringsConstantsAndUtils.getPathToBuildings(Imperials.RACE_NAME) + mBuildingLoader.name + ".png";
        mHeight = SizeConstants.BUILDING_DIAMETER;
        mWidth = SizeConstants.BUILDING_DIAMETER;

        TeamColorArea area = mBuildingLoader.team_color_area;
        mTeamColorArea = new Area(area.x, area.y, area.width, area.height);
        mBuildingLoader.team_color_area = null;
    }

    public void loadResources(Context context, BitmapTextureAtlas textureAtlas, int x, int y) {
        GameObject.loadResource(mPathToImage, context, textureAtlas, x, y);
        mTextureRegion = TextureRegionHolderUtils.getInstance().getElement(mPathToImage);
    }

    public CreepBuilding constructBuilding(VertexBufferObjectManager objectManager) {
        BuildingBuilder buildingBuilder = new BuildingBuilder(mTextureRegion, objectManager);

        Context context = SpaceInvadersApplication.getContext();
        buildingBuilder.setCost(mBuildingLoader.cost)
                .setWidth(mWidth)
                .setHeight(mHeight)
                .setPosition(mBuildingLoader.position_x, mBuildingLoader.position_y)
                .setObjectStringId(context.getResources().getIdentifier(
                        mBuildingLoader.name, "string", context.getApplicationInfo().packageName));

        return new CreepBuilding(buildingBuilder);
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }

    public Area getTeamColorArea() {
        return mTeamColorArea;
    }

    public int getCost() {
        return mBuildingLoader.cost;
    }

    public ITextureRegion getTextureRegion() {
        return mTextureRegion;
    }

    public int getNameId() {
        Context context = SpaceInvadersApplication.getContext();
        return context.getResources().getIdentifier(
                mBuildingLoader.name, "string", context.getApplicationInfo().packageName);
    }
}
