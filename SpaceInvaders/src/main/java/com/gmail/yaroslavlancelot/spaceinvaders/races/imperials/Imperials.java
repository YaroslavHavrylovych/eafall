package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.FirstImperialsBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.util.HashMap;
import java.util.Map;

/** imperials */
public class Imperials implements IRace {
    public static final String RACE_NAME = "Imperials";
    private Map<Integer, ITextureRegion> mBuildingsTextureRegions;


    public Imperials() {
    }

    @Override
    public String getRaceName() {
        return RACE_NAME;
    }

    @Override
    public StaticObject getBuildingById(final int id, final VertexBufferObjectManager objectManager) {
        return new FirstImperialsBuilding(0, 0, mBuildingsTextureRegions.get(id), objectManager);
    }

    @Override
    public Unit getUnitForBuilding(final int buildingId, final VertexBufferObjectManager objectManager, Color teamColor) {
        Unit unit = new ImperialsFirstUnit(0, 0, TextureRegionHolderUtils.getInstance().getElement(
                GameStringConstants.KEY_IMPERIALS_WARRIOR), objectManager);
        float width = SizeConstants.UNIT_TEAM_COLOR_INNER_SPRITE_SIZE;
        unit.setBackgroundArea(new Area(2.5f, 2.5f, width, width));
        unit.setBackgroundColor(teamColor);
        return unit;
    }

    @Override
    public void loadResources(final TextureManager textureManager, final Context context) {
        TextureRegionHolderUtils textureRegionHolderUtils = TextureRegionHolderUtils.getInstance();
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 15, 20, TextureOptions.BILINEAR);
        // warriors
        if (!textureRegionHolderUtils.isElementExist(GameStringConstants.KEY_IMPERIALS_WARRIOR))
            textureRegionHolderUtils.addElement(GameStringConstants.KEY_IMPERIALS_WARRIOR,
                    BitmapTextureAtlasTextureRegionFactory.createFromAsset(smallObjectTexture, context,
                            GameStringConstants.FILE_IMPERIALS_WARRIOR, 0, 0));
        // buildings
        if (!textureRegionHolderUtils.isElementExist(GameStringConstants.KEY_IMPERIALS_FIRST_BUILDING))
            textureRegionHolderUtils.addElement(GameStringConstants.KEY_IMPERIALS_FIRST_BUILDING,
                    BitmapTextureAtlasTextureRegionFactory.createFromAsset(smallObjectTexture, context,
                            GameStringConstants.FILE_IMPERIALS_FIRST_BUILDING, 0, 15));
        if (!textureRegionHolderUtils.isElementExist(GameStringConstants.KEY_IMPERIALS_SECOND_BUILDING))
            textureRegionHolderUtils.addElement(GameStringConstants.KEY_IMPERIALS_SECOND_BUILDING,
                    BitmapTextureAtlasTextureRegionFactory.createFromAsset(smallObjectTexture, context,
                            GameStringConstants.FILE_IMPERIALS_SECOND_BUILDING, 5, 15));

        // loading
        smallObjectTexture.load();

        initResourcesMap();
    }

    private void initResourcesMap() {
        mBuildingsTextureRegions = new HashMap<Integer, ITextureRegion>(BUILDINGS_AMOUNT);
        TextureRegionHolderUtils utils = TextureRegionHolderUtils.getInstance();
        mBuildingsTextureRegions.put(0, utils.getElement(GameStringConstants.KEY_IMPERIALS_FIRST_BUILDING));
        mBuildingsTextureRegions.put(1, utils.getElement(GameStringConstants.KEY_IMPERIALS_SECOND_BUILDING));
        mBuildingsTextureRegions.put(2, utils.getElement(GameStringConstants.KEY_IMPERIALS_THIRD_BUILDING));
        mBuildingsTextureRegions.put(3, utils.getElement(GameStringConstants.KEY_IMPERIALS_FOURTH_BUILDING));
        mBuildingsTextureRegions.put(4, utils.getElement(GameStringConstants.KEY_IMPERIALS_FIFTH_BUILDING));
        mBuildingsTextureRegions.put(5, utils.getElement(GameStringConstants.KEY_IMPERIALS_SIX_BUILDING));
        mBuildingsTextureRegions.put(6, utils.getElement(GameStringConstants.KEY_IMPERIALS_SEVEN_BUILDING));
        mBuildingsTextureRegions.put(7, utils.getElement(GameStringConstants.KEY_IMPERIALS_EIGHT_BUILDING));
        mBuildingsTextureRegions.put(8, utils.getElement(GameStringConstants.KEY_IMPERIALS_NINE_BUILDING));
        mBuildingsTextureRegions.put(9, utils.getElement(GameStringConstants.KEY_IMPERIALS_TEN_BUILDING));
        mBuildingsTextureRegions.put(10, utils.getElement(GameStringConstants.KEY_IMPERIALS_ELEVEN_BUILDING));
        mBuildingsTextureRegions.put(11, utils.getElement(GameStringConstants.KEY_IMPERIALS_TWELVE_BUILDING));
        mBuildingsTextureRegions.put(12, utils.getElement(GameStringConstants.KEY_IMPERIALS_THIRTEEN_BUILDING));
        mBuildingsTextureRegions.put(13, utils.getElement(GameStringConstants.KEY_IMPERIALS_FOURTEEN_BUILDING));
        mBuildingsTextureRegions.put(14, utils.getElement(GameStringConstants.KEY_IMPERIALS_FIFTEEN_BUILDING));
    }
}