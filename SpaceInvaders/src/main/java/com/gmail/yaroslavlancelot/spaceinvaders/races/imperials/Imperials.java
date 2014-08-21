package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.UnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.loading.UnitListLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.Barracks;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.Bunker;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.Camp;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.Laboratory;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.ShootersHall;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.Tent;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.TrainingCenter;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings.Workshop;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.simpleframework.xml.core.Persister;

import java.util.ArrayList;
import java.util.List;

/** imperials */
public class Imperials implements IRace {
    public static final String TAG = Imperials.class.getCanonicalName();
    /** race name */
    public static final String RACE_NAME = "Imperials";
    private Color mTeamColor;
    private VertexBufferObjectManager mObjectManager;
    private int mBuildingsAmount = 8;
    private SoundOperations mSoundOperations;
    private List<UnitDummy> mUnitDummies;

    public Imperials(Color teamColor, final VertexBufferObjectManager objectManager, final SoundOperations soundOperations) {
        mTeamColor = teamColor;
        mObjectManager = objectManager;
        mSoundOperations = soundOperations;
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
    public String[] getBuildingsNames() {
        String[] result = new String[getBuildingsAmount()];
        for (int i = 0; i < getBuildingsAmount(); i++) {
            result[i] = LocaleImpl.getInstance().getStringById(getBuildingById(i).getObjectStringId());
        }
        return result;
    }

    @Override
    public StaticObject getBuildingById(final int buildingId) {
        StaticObject building;
        switch (buildingId) {
            case 0:
                building = new Camp(mObjectManager);
                break;
            case 1:
                building = new Tent(mObjectManager);
                break;
            case 2:
                building = new Barracks(mObjectManager);
                break;
            case 3:
                building = new ShootersHall(mObjectManager);
                break;
            case 4:
                building = new TrainingCenter(mObjectManager);
                break;
            case 5:
                building = new Workshop(mObjectManager);
                break;
            case 6:
                building = new Laboratory(mObjectManager);
                break;
            case 7:
                building = new Bunker(mObjectManager);
                break;
            default:
                throw new IllegalArgumentException("unknown building type=" + buildingId);
        }
        initBuilding(building);
        return building;
    }

    @Override
    public int getBuildingCostById(final int buildingId) {
        return getBuildingById(buildingId).getObjectCost();
    }

    @Override
    public Unit getUnitForBuilding(final int buildingId) {
        UnitDummy dummy = mUnitDummies.get(buildingId);
        Unit unit = dummy.constructUnit(mObjectManager, mSoundOperations);
        unit.setBackgroundArea(dummy.getTeamColorArea());
        unit.setBackgroundColor(mTeamColor);
        return unit;
    }

    @Override
    public void loadResources(final TextureManager textureManager, final Context context) {
        loadBuildings(context, textureManager);

        loadUnits(context, textureManager);
    }

    private void loadBuildings(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 50, 50, TextureOptions.BILINEAR);
        Camp.loadResources(context, smallObjectTexture);
        Tent.loadResources(context, smallObjectTexture);
        Barracks.loadResources(context, smallObjectTexture);
        ShootersHall.loadResources(context, smallObjectTexture);
        TrainingCenter.loadResources(context, smallObjectTexture);
        Workshop.loadResources(context, smallObjectTexture);
        Laboratory.loadResources(context, smallObjectTexture);
        Bunker.loadResources(context, smallObjectTexture);
        smallObjectTexture.load();
    }

    private void loadUnits(Context context, TextureManager textureManager) {
        UnitListLoader unitListLoader = null;
        try {
            unitListLoader = new Persister().read(UnitListLoader.class, context.getResources().openRawResource(R.raw.units));
        } catch (Exception e) {
            LoggerHelper.printErrorMessage(TAG, "Reading file (units) exception = " + e.getMessage());
        }

        if (unitListLoader == null) return;

        int unitsAmount = unitListLoader.getList().size();
        mUnitDummies = new ArrayList<UnitDummy>(unitsAmount);

        int textureManagerElementsInLine = (int) Math.round(Math.sqrt(unitsAmount) + 1);
        int size = textureManagerElementsInLine * SizeConstants.UNIT_SIZE;

        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(
                textureManager, size, size, TextureOptions.BILINEAR);

        UnitDummy unitDummy;
        int n, m;
        for (int i = 0; i < unitsAmount; i++) {
            unitDummy = new UnitDummy(unitListLoader.getList().get(i));
            n = (i % textureManagerElementsInLine) * unitDummy.getWidth();
            m = (i / textureManagerElementsInLine) * unitDummy.getHeight();
            unitDummy.loadResources(context, smallObjectTexture, n, m);
            mUnitDummies.add(unitDummy);
        }
        smallObjectTexture.load();
    }

    private void initBuilding(StaticObject building) {
        building.setBackgroundColor(mTeamColor);
        building.setBackgroundArea();
    }
}
