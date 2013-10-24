package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Higgs;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Annihilator;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.FirstImperialsBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
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
    /** race name */
    public static final String RACE_NAME = "Imperials";
    /*
     *  buildings
     */
    public static final String KEY_IMPERIALS_FIRST_BUILDING = GameStringsConstantsAndUtils.getPathToBuildings(RACE_NAME) + "imperials_first_building.png";
    public static final String KEY_IMPERIALS_SECOND_BUILDING = GameStringsConstantsAndUtils.getPathToBuildings(RACE_NAME) + "imperials_second_building.png";
    public static final String KEY_IMPERIALS_THIRD_BUILDING = GameStringsConstantsAndUtils.getPathToBuildings(RACE_NAME) + "imperials_third_building.png";
    public static final String KEY_IMPERIALS_FOURTH_BUILDING = GameStringsConstantsAndUtils.getPathToBuildings(RACE_NAME) + "imperials_fourth_building.png";
    public static final String KEY_IMPERIALS_FIFTH_BUILDING = GameStringsConstantsAndUtils.getPathToBuildings(RACE_NAME) + "imperials_fifth_building.png";
    public static final String KEY_IMPERIALS_SIX_BUILDING = GameStringsConstantsAndUtils.getPathToBuildings(RACE_NAME) + "imperials_six_building.png";
    public static final String KEY_IMPERIALS_SEVEN_BUILDING = GameStringsConstantsAndUtils.getPathToBuildings(RACE_NAME) + "imperials_seven_building.png";
    public static final String KEY_IMPERIALS_EIGHT_BUILDING = GameStringsConstantsAndUtils.getPathToBuildings(RACE_NAME) + "imperials_eight_building.png";
    private Map<Integer, ITextureRegion> mBuildingsTextureRegions;
    private int mBuildingsAmount = 8;


    public Imperials() {
    }

    @Override
    public String getRaceName() {
        return RACE_NAME;
    }

    @Override
    public int getBuildingsAmount() {
        return mBuildingsAmount;
    }

    @Override
    public StaticObject getBuildingById(final int id, final VertexBufferObjectManager objectManager) {
        return new FirstImperialsBuilding(0, 0, mBuildingsTextureRegions.get(id), objectManager);
    }

    @Override
    public Unit getUnitForBuilding(final int buildingId, final VertexBufferObjectManager objectManager, Color teamColor) {
        Unit unit = new ImperialsFirstUnit(0, 0, TextureRegionHolderUtils.getInstance().getElement(
                GameStringsConstantsAndUtils.KEY_IMPERIALS_WARRIOR), objectManager, new Annihilator(30), new Higgs(20));
        float width = SizeConstants.UNIT_TEAM_COLOR_INNER_SPRITE_SIZE;
        unit.setBackgroundArea(new Area(2.5f, 2.5f, width, width));
        unit.setBackgroundColor(teamColor);
        return unit;
    }

    @Override
    public void loadResources(final TextureManager textureManager, final Context context) {
        TextureRegionHolderUtils textureRegionHolderUtils = TextureRegionHolderUtils.getInstance();
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 15, 15, TextureOptions.BILINEAR);
        // warriors
        if (!textureRegionHolderUtils.isElementExist(GameStringsConstantsAndUtils.KEY_IMPERIALS_WARRIOR))
            textureRegionHolderUtils.addElement(GameStringsConstantsAndUtils.KEY_IMPERIALS_WARRIOR,
                    BitmapTextureAtlasTextureRegionFactory.createFromAsset(smallObjectTexture, context,
                            GameStringsConstantsAndUtils.FILE_IMPERIALS_WARRIOR, 0, 0));
        smallObjectTexture.load();

        loadBuildings(context, textureManager);

        loadUnits(context, textureManager);

        initResourcesMap();
    }

    @Override
    public float[] getBuildingPositionOnThePlanet(final int buildingId) {
        float sizePlanet = SizeConstants.PLANET_DIAMETER,
                sizeBuilding = SizeConstants.BUILDING_DIAMETER;
        float[] ret = new float[2];
        switch (buildingId) {
            case 0:
                ret[0] = sizePlanet / 2 - sizeBuilding;
                ret[1] = ret[0] - sizeBuilding;
                break;
            case 1:
                ret[0] = sizePlanet / 2;
                ret[1] = ret[0] - 2 * sizeBuilding;
                break;
            case 2:
                ret[0] = sizePlanet / 2 + sizeBuilding;
                ret[1] = sizePlanet / 2 - sizeBuilding;
                break;
            case 3:
                ret[0] = sizePlanet / 2 + sizeBuilding;
                ret[1] = sizePlanet / 2;
                break;
            case 4:
                ret[0] = sizePlanet / 2;
                ret[1] = ret[0] + sizeBuilding;
                break;
            case 5:
                ret[0] = sizePlanet / 2 - sizeBuilding;
                ret[1] = sizePlanet / 2 + sizeBuilding;
                break;
            case 6:
                ret[0] = sizePlanet / 2 - 2 * sizeBuilding;
                ret[1] = sizePlanet / 2;
                break;
            case 7:
                ret[0] = sizePlanet / 2 - 2 * sizeBuilding;
                ret[1] = sizePlanet / 2 - sizeBuilding;
                break;
            default:
                throw new IllegalArgumentException("unknown building id=" + buildingId);
        }
        return ret;
    }

    private void initResourcesMap() {
        mBuildingsTextureRegions = new HashMap<Integer, ITextureRegion>(getBuildingsAmount());
        TextureRegionHolderUtils utils = TextureRegionHolderUtils.getInstance();
        mBuildingsTextureRegions.put(0, utils.getElement(KEY_IMPERIALS_FIRST_BUILDING));
        mBuildingsTextureRegions.put(1, utils.getElement(KEY_IMPERIALS_SECOND_BUILDING));
        mBuildingsTextureRegions.put(2, utils.getElement(KEY_IMPERIALS_THIRD_BUILDING));
        mBuildingsTextureRegions.put(3, utils.getElement(KEY_IMPERIALS_FOURTH_BUILDING));
        mBuildingsTextureRegions.put(4, utils.getElement(KEY_IMPERIALS_FIFTH_BUILDING));
        mBuildingsTextureRegions.put(5, utils.getElement(KEY_IMPERIALS_SIX_BUILDING));
        mBuildingsTextureRegions.put(6, utils.getElement(KEY_IMPERIALS_SEVEN_BUILDING));
        mBuildingsTextureRegions.put(7, utils.getElement(KEY_IMPERIALS_EIGHT_BUILDING));
    }

    private void loadBuildings(Context context, TextureManager textureManager) {
        TextureRegionHolderUtils utils = TextureRegionHolderUtils.getInstance();
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 45, 45, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_FIRST_BUILDING, utils, smallObjectTexture, context, 0, 0);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_SECOND_BUILDING, utils, smallObjectTexture, context, 5, 0);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_THIRD_BUILDING, utils, smallObjectTexture, context, 10, 0);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_FOURTH_BUILDING, utils, smallObjectTexture, context, 0, 5);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_FIFTH_BUILDING, utils, smallObjectTexture, context, 5, 5);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_SIX_BUILDING, utils, smallObjectTexture, context, 0, 5);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_SEVEN_BUILDING, utils, smallObjectTexture, context, 0, 10);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_EIGHT_BUILDING, utils, smallObjectTexture, context, 5, 10);
        smallObjectTexture.load();
    }

    private void loadUnits(Context context, TextureManager textureManager) {
        TextureRegionHolderUtils utils = TextureRegionHolderUtils.getInstance();
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 45, 45, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_FIRST_BUILDING, utils, smallObjectTexture, context, 0, 0);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_SECOND_BUILDING, utils, smallObjectTexture, context, 5, 0);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_THIRD_BUILDING, utils, smallObjectTexture, context, 10, 0);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_FOURTH_BUILDING, utils, smallObjectTexture, context, 0, 5);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_FIFTH_BUILDING, utils, smallObjectTexture, context, 5, 5);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_SIX_BUILDING, utils, smallObjectTexture, context, 0, 5);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_SEVEN_BUILDING, utils, smallObjectTexture, context, 0, 10);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_EIGHT_BUILDING, utils, smallObjectTexture, context, 5, 10);
        smallObjectTexture.load();
    }
}
