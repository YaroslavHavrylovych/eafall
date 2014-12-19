package com.gmail.yaroslavlancelot.spaceinvaders.alliances;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.SpaceInvadersApplication;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dummies.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.BuildingListLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.UnitListLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.UnitDummy;
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

public abstract class Alliance implements IAlliance {
    private static final String TAG = Alliance.class.getCanonicalName();
    protected VertexBufferObjectManager mObjectManager;
    protected SoundOperations mSoundOperations;
    protected Map<Integer, UnitDummy> mUnitDummies;
    protected Map<Integer, CreepBuildingDummy> mCreepBuildingDummies;
    protected SortedSet<Integer> mSortedBuildingsSet;

    protected Alliance(final VertexBufferObjectManager objectManager, final SoundOperations soundOperations) {
        mObjectManager = objectManager;
        mSoundOperations = soundOperations;
    }

    @Override
    public int getBuildingsAmount() {
        return mCreepBuildingDummies.size();
    }

    @Override
    public int getBuildingCost(BuildingId buildingId) {
        return getBuildingDummy(buildingId).getCost(buildingId.getUpgrade());
    }

    @Override
    public Unit getUnit(int unitId, Color teamColor) {
        UnitDummy dummy = mUnitDummies.get(unitId);
        Unit unit = dummy.constructUnit(mObjectManager, mSoundOperations, getAllianceName());
        unit.setBackgroundArea(dummy.getTeamColorArea());
        unit.setBackgroundColor(teamColor);
        return unit;
    }

    @Override
    public UnitDummy getUnitDummy(int unitId) {
        return mUnitDummies.get(unitId);
    }

    @Override
    public CreepBuildingDummy getBuildingDummy(BuildingId buildingId) {
        CreepBuildingDummy dummy = mCreepBuildingDummies.get(buildingId.getId());
        return dummy;
    }

    @Override
    public SortedSet<Integer> getBuildingsIds() {
        return mSortedBuildingsSet;
    }

    @Override
    public int getUpgradeCost(BuildingId buildingId) {
        CreepBuildingDummy dummy = mCreepBuildingDummies.get(buildingId.getId());
        if (buildingId.getUpgrade() + 1 >= dummy.getUpgrades()) {
            return -1;
        }
        int cost = dummy.getCost(buildingId.getUpgrade() + 1);
        return (int) Math.round(cost * 0.5 + 1);
    }

    @Override
    public boolean isUpgradeAvailable(BuildingId buildingId) {
        CreepBuildingDummy dummy = mCreepBuildingDummies.get(buildingId.getId());
        return buildingId.getUpgrade() + 1 < dummy.getUpgrades();
    }

    protected <T> T loadObjects(int rawId, Class<T> cls) {
        T ret = null;
        Context context = SpaceInvadersApplication.getContext();
        try {
            ret = new Persister().read(cls, context.getResources().openRawResource(rawId));
        } catch (Exception e) {
            LoggerHelper.printErrorMessage(TAG, "Reading file exception = " + e.getMessage());
        }
        return ret;
    }

    protected void loadBuildings(TextureManager textureManager, BuildingListLoader buildingListLoader) {
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
            dummy.loadResources(context, smallObjectTexture, n, m, getAllianceName());
            i++;
        }
        smallObjectTexture.load();

        mSortedBuildingsSet = new TreeSet<Integer>(mCreepBuildingDummies.keySet());
    }

    protected void loadUnits(TextureManager textureManager, UnitListLoader unitListLoader) {
        Context context = SpaceInvadersApplication.getContext();

        int unitsAmount = unitListLoader.getList().size();
        mUnitDummies = new HashMap<Integer, UnitDummy>(unitsAmount);

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
            mUnitDummies.put(unitDummy.getId(), unitDummy);
        }
        smallObjectTexture.load();
    }
}
