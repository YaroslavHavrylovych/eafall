package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.SpaceInvadersApplication;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dummies.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.BuildingListLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.UnitListLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.UnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.simpleframework.xml.core.Persister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/** imperials */
public class Imperials implements IRace {
    public static final String TAG = Imperials.class.getCanonicalName();
    /** race name */
    public static final String RACE_NAME = "Imperials";
    private VertexBufferObjectManager mObjectManager;
    private SoundOperations mSoundOperations;
    private List<UnitDummy> mUnitDummies;
    private Map<Integer, CreepBuildingDummy> mCreepBuildingDummies;
    private SortedSet<Integer> mSortedBuildingsSet;

    public Imperials(final VertexBufferObjectManager objectManager, final SoundOperations soundOperations) {
        mObjectManager = objectManager;
        mSoundOperations = soundOperations;
    }

    @Override
    public SortedSet<Integer> getBuildingsIds() {
        return mSortedBuildingsSet;
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
    public CreepBuildingDummy getBuildingDummy(BuildingId buildingId) {
        CreepBuildingDummy dummy = mCreepBuildingDummies.get(buildingId.getId());
        return dummy;
    }

    @Override
    public int getBuildingCost(BuildingId buildingId) {
        return getBuildingDummy(buildingId).getCost(buildingId.getUpgrade());
    }

    @Override
    public Unit getUnit(int unitId, Color teamColor) {
        UnitDummy dummy = mUnitDummies.get(unitId);
        Unit unit = dummy.constructUnit(mObjectManager, mSoundOperations);
        unit.setBackgroundArea(dummy.getTeamColorArea());
        unit.setBackgroundColor(teamColor);
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

        int buildingsAmount = buildingListLoader.getList().size();
        mCreepBuildingDummies = new HashMap<Integer, CreepBuildingDummy>(buildingsAmount);

        //init list
        CreepBuildingDummy creepBuildingDummy;
        int buildingsWithUpgradesAmount = 0;
        for (int i = 0; i < buildingsAmount; i++) {
            creepBuildingDummy = new CreepBuildingDummy(buildingListLoader.getList().get(i));
            buildingsWithUpgradesAmount += (creepBuildingDummy.getUpgrades() * SizeConstants.BUILDING_SIZE);
            mCreepBuildingDummies.put(creepBuildingDummy.getBuildingId(), creepBuildingDummy);
        }

        int textureManagerElementsInLine = (int) Math.round(Math.sqrt(buildingsWithUpgradesAmount) + 1);
        int size = textureManagerElementsInLine * SizeConstants.BUILDING_SIZE;

        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(
                textureManager, size, size, TextureOptions.BILINEAR);

        //load
        int n, m, i = 0;
        for (CreepBuildingDummy dummy : mCreepBuildingDummies.values()) {
            n = (i % textureManagerElementsInLine) * dummy.getWidth();
            m = (i / textureManagerElementsInLine) * dummy.getHeight();
            dummy.loadResources(context, smallObjectTexture, n, m, getRaceName());
            i++;
        }
        smallObjectTexture.load();

        mSortedBuildingsSet = new TreeSet<Integer>(mCreepBuildingDummies.keySet());
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

    @Override
    public UnitDummy getUnitDummy(int unitId) {
        return mUnitDummies.get(unitId);
    }

    @Override
    public int getUpgradeCost(BuildingId buildingId) {
        CreepBuildingDummy dummy = mCreepBuildingDummies.get(buildingId.getId());
        if(buildingId.getUpgrade() + 1 >= dummy.getUpgrades()) {
            return -1;
        }
        int cost = dummy.getCost(buildingId.getUpgrade() + 1);
        int result = (int) Math.round(cost * 0.5 + 1);
        return result;
    }

    @Override
    public boolean isUpgradeAvailable(BuildingId buildingId) {
        CreepBuildingDummy dummy = mCreepBuildingDummies.get(buildingId.getId());
        return buildingId.getUpgrade() + 1 < dummy.getUpgrades();
    }
}
