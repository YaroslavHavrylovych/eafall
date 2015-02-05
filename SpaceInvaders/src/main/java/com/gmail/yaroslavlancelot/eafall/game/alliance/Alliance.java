package com.gmail.yaroslavlancelot.eafall.game.alliance;

import android.content.Context;
import android.util.SparseArray;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.SpecialBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.WealthBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.BuildingListLoader;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitListLoader;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitLoader;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.MovableUnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary.StationaryUnitDummy;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.simpleframework.xml.core.Persister;

import java.util.SortedSet;
import java.util.TreeSet;

/** Each alliance common functionality */
public abstract class Alliance implements IAlliance {
    private static final String TAG = Alliance.class.getCanonicalName();
    protected VertexBufferObjectManager mObjectManager;
    protected SoundOperations mSoundOperations;
    protected SparseArray<UnitDummy> mUnitDummies;
    protected SparseArray<BuildingDummy> mBuildingDummies;
    protected SortedSet<Integer> mSortedBuildingsSet;

    protected Alliance(final VertexBufferObjectManager objectManager, final SoundOperations soundOperations) {
        mObjectManager = objectManager;
        mSoundOperations = soundOperations;
    }

    @Override
    public int getBuildingsAmount() {
        return mBuildingDummies.size();
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
    public BuildingDummy getBuildingDummy(BuildingId buildingId) {
        return mBuildingDummies.get(buildingId.getId());
    }

    @Override
    public SortedSet<Integer> getBuildingsIds() {
        return mSortedBuildingsSet;
    }

    @Override
    public int getUpgradeCost(BuildingId buildingId) {
        BuildingDummy dummy = mBuildingDummies.get(buildingId.getId());
        if (buildingId.getUpgrade() + 1 >= dummy.getUpgrades()) {
            return -1;
        }
        int cost = dummy.getCost(buildingId.getUpgrade() + 1);
        return (int) Math.round(cost * 0.5 + 1);
    }

    @Override
    public boolean isUpgradeAvailable(BuildingId buildingId) {
        BuildingDummy dummy = mBuildingDummies.get(buildingId.getId());
        return buildingId.getUpgrade() + 1 < dummy.getUpgrades();
    }

    protected <T> T loadObjects(int rawId, Class<T> cls) {
        T ret = null;
        Context context = EaFallApplication.getContext();
        try {
            ret = new Persister().read(cls, context.getResources().openRawResource(rawId));
        } catch (Exception e) {
            LoggerHelper.printErrorMessage(TAG, "Reading file exception = " + e.getMessage());
        }
        return ret;
    }

    protected void loadBuildings(TextureManager textureManager, BuildingListLoader buildingListLoader) {
        Context context = EaFallApplication.getContext();
        mBuildingDummies = new SparseArray<BuildingDummy>(buildingListLoader.getList().size() //units
                + 1); //wealth buildings

        //units
        BuildingDummy buildingDummy;
        int buildingsWithUpgradesAmount = 0;
        for (int i = 0; i < buildingListLoader.getList().size(); i++) {
            buildingDummy = new CreepBuildingDummy(buildingListLoader.getList().get(i));
            buildingsWithUpgradesAmount += (buildingDummy.getUpgrades() * Sizes.BUILDING_SIZE);
            mBuildingDummies.put(buildingDummy.getBuildingId(), buildingDummy);
        }
        //defence
        buildingDummy = new DefenceBuildingDummy(buildingListLoader.defenceBuildingLoader);
        buildingsWithUpgradesAmount++;
        mBuildingDummies.put(buildingDummy.getBuildingId(), buildingDummy);
        //wealth
        buildingDummy = new WealthBuildingDummy(buildingListLoader.wealthBuildingLoader);
        buildingsWithUpgradesAmount++;
        mBuildingDummies.put(buildingDummy.getBuildingId(), buildingDummy);
        //special
        buildingDummy = new SpecialBuildingDummy(buildingListLoader.specialBuildingLoader);
        buildingsWithUpgradesAmount++;
        mBuildingDummies.put(buildingDummy.getBuildingId(), buildingDummy);

        //creating texture atlas for loading buildings
        int textureManagerElementsInLine = (int) Math.round(Math.sqrt(buildingsWithUpgradesAmount) + 1);
        int size = textureManagerElementsInLine * Sizes.BUILDING_SIZE;

        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(
                textureManager, size, size, TextureOptions.BILINEAR);

        //load
        int n, m;
        for (int i = 0; i < mBuildingDummies.size(); i++) {
            buildingDummy = mBuildingDummies.valueAt(i);
            n = (i % textureManagerElementsInLine) * buildingDummy.getWidth();
            m = (i / textureManagerElementsInLine) * buildingDummy.getHeight();
            buildingDummy.loadResources(context, smallObjectTexture, n, m, getAllianceName());
        }
        smallObjectTexture.load();

        mSortedBuildingsSet = new TreeSet<Integer>();
        for (int i = 0; i < mBuildingDummies.size(); i++) {
            mSortedBuildingsSet.add(mBuildingDummies.keyAt(i));
        }
    }

    protected void loadUnits(TextureManager textureManager, UnitListLoader unitListLoader) {
        Context context = EaFallApplication.getContext();

        int unitsAmount = unitListLoader.getList().size();
        mUnitDummies = new SparseArray<UnitDummy>(unitsAmount);

        int textureManagerElementsInLine = (int) Math.round(Math.sqrt(unitsAmount) + 1);
        int size = textureManagerElementsInLine * Sizes.UNIT_SIZE;

        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(
                textureManager, size, size, TextureOptions.BILINEAR);

        UnitDummy unitDummy;
        UnitLoader unitLoader;
        int n, m;
        for (int i = 0; i < unitsAmount; i++) {
            unitLoader = unitListLoader.getList().get(i);
            if (unitLoader.speed > 1f) {
                unitDummy = new MovableUnitDummy(unitLoader, getAllianceName().toLowerCase());
            } else {
                unitDummy = new StationaryUnitDummy(unitLoader, getAllianceName().toLowerCase());
            }
            n = (i % textureManagerElementsInLine) * unitDummy.getWidth();
            m = (i / textureManagerElementsInLine) * unitDummy.getHeight();
            unitDummy.loadResources(context, smallObjectTexture, n, m);
            mUnitDummies.put(unitDummy.getId(), unitDummy);
        }
        smallObjectTexture.load();
    }
}
