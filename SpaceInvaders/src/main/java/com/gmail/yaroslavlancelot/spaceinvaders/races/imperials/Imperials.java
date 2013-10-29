package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Higgs;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Annihilator;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.Building;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
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
    /*
     *  units
     */
    public static final String KEY_IMPERIALS_FIRST_UNIT = GameStringsConstantsAndUtils.getPathToUnits(RACE_NAME) + "imperials_first_unit.png";
    public static final String KEY_IMPERIALS_SECOND_UNIT = GameStringsConstantsAndUtils.getPathToUnits(RACE_NAME) + "imperials_second_unit.png";
    public static final String KEY_IMPERIALS_THIRD_UNIT = GameStringsConstantsAndUtils.getPathToUnits(RACE_NAME) + "imperials_third_unit.png";
    public static final String KEY_IMPERIALS_FOURTH_UNIT = GameStringsConstantsAndUtils.getPathToUnits(RACE_NAME) + "imperials_fourth_unit.png";
    public static final String KEY_IMPERIALS_FIFTH_UNIT = GameStringsConstantsAndUtils.getPathToUnits(RACE_NAME) + "imperials_fifth_unit.png";
    public static final String KEY_IMPERIALS_SIX_UNIT = GameStringsConstantsAndUtils.getPathToUnits(RACE_NAME) + "imperials_six_unit.png";
    public static final String KEY_IMPERIALS_SEVEN_UNIT = GameStringsConstantsAndUtils.getPathToUnits(RACE_NAME) + "imperials_seven_unit.png";
    public static final String KEY_IMPERIALS_EIGHT_UNIT = GameStringsConstantsAndUtils.getPathToUnits(RACE_NAME) + "imperials_eight_unit.png";
    private Map<Integer, ITextureRegion> mBuildingsTextureRegions;
    private int mBuildingsAmount = 8;
    private Map<Integer, ITextureRegion> mUnitsTextureRegions;


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
    public StaticObject getBuildingById(final int id, final VertexBufferObjectManager objectManager, Color teamColor) {
        StaticObject building = new Building(0, 0, mBuildingsTextureRegions.get(id), objectManager, (id + 1) * 50, (id + 1) * 10);
        building.setBackgroundArea(new Area(0f, 0f, SizeConstants.BUILDING_DIAMETER, SizeConstants.BUILDING_DIAMETER));
        building.setBackgroundColor(teamColor);
        return building;
    }

    @Override
    public Unit getUnitForBuilding(final int buildingId, final VertexBufferObjectManager objectManager, Color teamColor) {
        Unit unit = new ImperialsUnit(0, 0, mUnitsTextureRegions.get(buildingId), objectManager,
                new Annihilator((buildingId + 1) * 5), new Higgs((buildingId + 1) * 3));
        float width = SizeConstants.UNIT_TEAM_COLOR_INNER_SPRITE_SIZE;
        unit.setBackgroundArea(new Area(2.5f, 2.5f, width, width));
        unit.setBackgroundColor(teamColor);
        return unit;
    }

    @Override
    public void loadResources(final TextureManager textureManager, final Context context) {
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
        TextureRegionHolderUtils utils = TextureRegionHolderUtils.getInstance();
        // buildings
        mBuildingsTextureRegions = new HashMap<Integer, ITextureRegion>(getBuildingsAmount());
        mBuildingsTextureRegions.put(0, utils.getElement(KEY_IMPERIALS_FIRST_BUILDING));
        mBuildingsTextureRegions.put(1, utils.getElement(KEY_IMPERIALS_SECOND_BUILDING));
        mBuildingsTextureRegions.put(2, utils.getElement(KEY_IMPERIALS_THIRD_BUILDING));
        mBuildingsTextureRegions.put(3, utils.getElement(KEY_IMPERIALS_FOURTH_BUILDING));
        mBuildingsTextureRegions.put(4, utils.getElement(KEY_IMPERIALS_FIFTH_BUILDING));
        mBuildingsTextureRegions.put(5, utils.getElement(KEY_IMPERIALS_SIX_BUILDING));
        mBuildingsTextureRegions.put(6, utils.getElement(KEY_IMPERIALS_SEVEN_BUILDING));
        mBuildingsTextureRegions.put(7, utils.getElement(KEY_IMPERIALS_EIGHT_BUILDING));
        // units
        mUnitsTextureRegions = new HashMap<Integer, ITextureRegion>(getBuildingsAmount());
        mUnitsTextureRegions.put(0, utils.getElement(KEY_IMPERIALS_FIRST_UNIT));
        mUnitsTextureRegions.put(1, utils.getElement(KEY_IMPERIALS_SECOND_UNIT));
        mUnitsTextureRegions.put(2, utils.getElement(KEY_IMPERIALS_THIRD_UNIT));
        mUnitsTextureRegions.put(3, utils.getElement(KEY_IMPERIALS_FOURTH_UNIT));
        mUnitsTextureRegions.put(4, utils.getElement(KEY_IMPERIALS_FIFTH_UNIT));
        mUnitsTextureRegions.put(5, utils.getElement(KEY_IMPERIALS_SIX_UNIT));
        mUnitsTextureRegions.put(6, utils.getElement(KEY_IMPERIALS_SEVEN_UNIT));
        mUnitsTextureRegions.put(7, utils.getElement(KEY_IMPERIALS_EIGHT_UNIT));
    }

    private void loadBuildings(Context context, TextureManager textureManager) {
        TextureRegionHolderUtils utils = TextureRegionHolderUtils.getInstance();
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 45, 45, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_FIRST_BUILDING, utils, smallObjectTexture, context, 0, 0);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_SECOND_BUILDING, utils, smallObjectTexture, context, 5, 0);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_THIRD_BUILDING, utils, smallObjectTexture, context, 10, 0);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_FOURTH_BUILDING, utils, smallObjectTexture, context, 0, 5);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_FIFTH_BUILDING, utils, smallObjectTexture, context, 5, 5);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_SIX_BUILDING, utils, smallObjectTexture, context, 10, 5);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_SEVEN_BUILDING, utils, smallObjectTexture, context, 0, 10);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_EIGHT_BUILDING, utils, smallObjectTexture, context, 5, 10);
        smallObjectTexture.load();
    }

    private void loadUnits(Context context, TextureManager textureManager) {
        TextureRegionHolderUtils utils = TextureRegionHolderUtils.getInstance();
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 45, 45, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_FIRST_UNIT, utils, smallObjectTexture, context, 0, 0);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_SECOND_UNIT, utils, smallObjectTexture, context, 15, 0);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_THIRD_UNIT, utils, smallObjectTexture, context, 30, 0);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_FOURTH_UNIT, utils, smallObjectTexture, context, 0, 15);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_FIFTH_UNIT, utils, smallObjectTexture, context, 15, 15);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_SIX_UNIT, utils, smallObjectTexture, context, 30, 15);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_SEVEN_UNIT, utils, smallObjectTexture, context, 0, 30);
        TextureRegionHolderUtils.addElementFromAssets(KEY_IMPERIALS_EIGHT_UNIT, utils, smallObjectTexture, context, 15, 30);
        smallObjectTexture.load();
    }
}
