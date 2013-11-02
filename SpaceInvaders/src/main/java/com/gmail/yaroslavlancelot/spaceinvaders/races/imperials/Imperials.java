package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.EightBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.FifthBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.FirstBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.FourthBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.SecondBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.SevenBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.SixBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.ThirdBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.EightUnit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.FifthUnit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.FirstUnit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.FourthUnit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.SecondUnit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.SevenUnit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.SixUnit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.ThirdUnit;
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
    private VertexBufferObjectManager mObjectManager;
    private Color mTeamColor;
    private EntityOperations mEntityOperations;
    private int mBuildingsAmount = 8;


    public Imperials(final VertexBufferObjectManager objectManager, Color teamColor, final EntityOperations entityOperations) {
        this.mObjectManager = objectManager;
        this.mTeamColor = teamColor;
        this.mEntityOperations = entityOperations;
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
    public StaticObject getBuildingById(final int buildingId) {
        StaticObject building;
        switch (buildingId) {
            case 0:
                building = new FirstBuilding(mObjectManager);
                break;
            case 1:
                building = new SecondBuilding(mObjectManager);
                break;
            case 2:
                building = new ThirdBuilding(mObjectManager);
                break;
            case 3:
                building = new FourthBuilding(mObjectManager);
                break;
            case 4:
                building = new FifthBuilding(mObjectManager);
                break;
            case 5:
                building = new SixBuilding(mObjectManager);
                break;
            case 6:
                building = new SevenBuilding(mObjectManager);
                break;
            case 7:
                building = new EightBuilding(mObjectManager);
                break;
            default:
                throw new IllegalArgumentException("unknown building type=" + buildingId);
        }
        initBuilding(building);
        return building;
    }

    @Override
    public Unit getUnitForBuilding(final int buildingId) {
        Unit unit;
        switch (buildingId) {
            case 0:
                unit = new FirstUnit(mObjectManager, mEntityOperations);
                break;
            case 1:
                unit = new SecondUnit(mObjectManager, mEntityOperations);
                break;
            case 2:
                unit = new ThirdUnit(mObjectManager, mEntityOperations);
                break;
            case 3:
                unit = new FourthUnit(mObjectManager, mEntityOperations);
                break;
            case 4:
                unit = new FifthUnit(mObjectManager, mEntityOperations);
                break;
            case 5:
                unit = new SixUnit(mObjectManager, mEntityOperations);
                break;
            case 6:
                unit = new SevenUnit(mObjectManager, mEntityOperations);
                break;
            case 7:
                unit = new EightUnit(mObjectManager, mEntityOperations);
                break;
            default:
                throw new IllegalArgumentException("unknown building type=" + buildingId);
        }
        initUnit(unit);
        return unit;
    }

    private void initUnit(Unit unit) {
        float width = SizeConstants.UNIT_TEAM_COLOR_INNER_SPRITE_SIZE;
        unit.setBackgroundArea(new Area(2.5f, 2.5f, width, width));
        unit.setBackgroundColor(mTeamColor);
    }

    @Override
    public void loadResources(final TextureManager textureManager, final Context context) {
        loadBuildings(context, textureManager);

        loadUnits(context, textureManager);
    }

    private void loadUnits(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 45, 45, TextureOptions.BILINEAR);
        FirstUnit.loadResources(context, smallObjectTexture);
        SecondUnit.loadResources(context, smallObjectTexture);
        ThirdUnit.loadResources(context, smallObjectTexture);
        FourthUnit.loadResources(context, smallObjectTexture);
        FifthUnit.loadResources(context, smallObjectTexture);
        SixUnit.loadResources(context, smallObjectTexture);
        SevenUnit.loadResources(context, smallObjectTexture);
        EightUnit.loadResources(context, smallObjectTexture);
        smallObjectTexture.load();
    }

    private void loadBuildings(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 45, 45, TextureOptions.BILINEAR);
        FirstBuilding.loadResources(context, smallObjectTexture);
        SecondBuilding.loadResources(context, smallObjectTexture);
        ThirdBuilding.loadResources(context, smallObjectTexture);
        FourthBuilding.loadResources(context, smallObjectTexture);
        FifthBuilding.loadResources(context, smallObjectTexture);
        SixBuilding.loadResources(context, smallObjectTexture);
        SevenBuilding.loadResources(context, smallObjectTexture);
        EightBuilding.loadResources(context, smallObjectTexture);
        smallObjectTexture.load();
    }

    private void initBuilding(StaticObject building) {
        building.setBackgroundArea(new Area(0f, 0f, SizeConstants.BUILDING_DIAMETER, SizeConstants.BUILDING_DIAMETER));
        building.setBackgroundColor(mTeamColor);
    }
}
