package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.SpaceInvadersApplication;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.CreepBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.BuildingListLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.UnitListLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.UnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
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
    private SoundOperations mSoundOperations;
    private List<UnitDummy> mUnitDummies;
    private List<CreepBuildingDummy> mCreepBuildingDummies;

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
        return mCreepBuildingDummies.size();
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
        CreepBuildingDummy dummy = mCreepBuildingDummies.get(buildingId);
        CreepBuilding unit = dummy.constructBuilding(mObjectManager);
        unit.setBackgroundArea(dummy.getTeamColorArea());
        unit.setBackgroundColor(mTeamColor);
        return unit;
    }

    @Override
    public int getBuildingCostById(final int buildingId) {
        return mCreepBuildingDummies.get(buildingId).getCost();
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
        loadBuildings(textureManager);

        loadUnits(textureManager);
    }

    private void loadBuildings(TextureManager textureManager) {
        LoggerHelper.printDebugMessage(TAG, "loading buildings");
        BuildingListLoader buildingListLoader = loadObjects(R.raw.buildings, BuildingListLoader.class);
        if (buildingListLoader == null) return;
        Context context = SpaceInvadersApplication.getContext();

        int unitsAmount = buildingListLoader.getList().size();
        mCreepBuildingDummies = new ArrayList<CreepBuildingDummy>(unitsAmount);

        int textureManagerElementsInLine = (int) Math.round(Math.sqrt(unitsAmount) + 1);
        int size = textureManagerElementsInLine * SizeConstants.UNIT_SIZE;

        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(
                textureManager, size, size, TextureOptions.BILINEAR);

        CreepBuildingDummy creepBuildingDummy;
        int n, m;
        for (int i = 0; i < unitsAmount; i++) {
            creepBuildingDummy = new CreepBuildingDummy(buildingListLoader.getList().get(i));
            n = (i % textureManagerElementsInLine) * creepBuildingDummy.getWidth();
            m = (i / textureManagerElementsInLine) * creepBuildingDummy.getHeight();
            creepBuildingDummy.loadResources(context, smallObjectTexture, n, m);
            mCreepBuildingDummies.add(creepBuildingDummy);
        }
        smallObjectTexture.load();
    }

    private void loadUnits(TextureManager textureManager) {
        LoggerHelper.printDebugMessage(TAG, "loading units");
        UnitListLoader unitListLoader = loadObjects(R.raw.units, UnitListLoader.class);
        if (unitListLoader == null) return;
        Context context = SpaceInvadersApplication.getContext();

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

    private <T> T loadObjects(int rawId, Class<T> cls) {
        T ret = null;
        Context context = SpaceInvadersApplication.getContext();
        try {
            ret = new Persister().read(cls, context.getResources().openRawResource(rawId));
        } catch (Exception e) {
            LoggerHelper.printErrorMessage(TAG, "Reading file exception = " + e.getMessage());
        }
        return ret;
    }
}
