package com.yaroslavlancelot.eafall.game.alliance;

import android.content.Context;
import android.util.SparseArray;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.android.LoggerHelper;
import com.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.OffenceBuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.SpecialBuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.WealthBuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.BuildingListLoader;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.defence.DefenceUnitDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitListLoader;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitLoader;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnitDummy;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.TextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.simpleframework.xml.core.Persister;

import java.util.SortedSet;
import java.util.TreeSet;

/** Each alliance common functionality */
public abstract class Alliance implements IAlliance {
    private static final String TAG = Alliance.class.getCanonicalName();
    protected VertexBufferObjectManager mObjectManager;
    protected SparseArray<UnitDummy> mUnitDummies;
    protected SortedSet<Integer> mUnitsIds;
    protected SparseArray<BuildingDummy> mBuildingDummies;
    protected SortedSet<Integer> mBuildingsIds;
    private TextureAtlas mBuildingTextureAtlas;

    protected Alliance(final VertexBufferObjectManager objectManager) {
        mObjectManager = objectManager;
        initDummies();
    }

    protected abstract UnitListLoader getUnitListLoader();

    protected abstract BuildingListLoader getBuildingListLoader();

    protected int getBuildingsWithUpgradesAmount() {
        int buildingsWithUpgradesAmount = 0;
        for (int i = 0; i < mBuildingDummies.size(); i++) {
            buildingsWithUpgradesAmount += mBuildingDummies.valueAt(i).getUpgrades();
        }
        return buildingsWithUpgradesAmount;
    }

    @Override
    public int getBuildingsAmount() {
        return mBuildingDummies.size();
    }

    @Override
    public TextureAtlas getBuildingTextureAtlas() {
        return mBuildingTextureAtlas;
    }

    @Override
    public SortedSet<Integer> getBuildingsIds() {
        return mBuildingsIds;
    }

    @Override
    public SortedSet<Integer> getUnitsIds() {
        return mUnitsIds;
    }

    @Override
    public int getBuildingCost(BuildingId buildingId) {
        return getBuildingDummy(buildingId).getCost(buildingId.getUpgrade());
    }

    @Override
    public void loadBuildings(TextureManager textureManager) {
        Context context = EaFallApplication.getContext();
        loadBuildings_Sprites(textureManager, context);
        loadBuildings_Images(textureManager, context);
    }

    @Override
    public void loadUnits(TextureManager textureManager) {
        Context context = EaFallApplication.getContext();
        loadUnits_Images(textureManager, context);
        //Init after loading. Init will create a pool, so texture atlas he to be loaded.
        for (int i = 0; i < mUnitDummies.size(); i++) {
            mUnitDummies.valueAt(i)
                    .initDummy(SoundFactory.getInstance(), getAllianceName());
        }
    }

    public BitmapTextureAtlas loadUnitsToTexture(String playerName, TextureManager textureManager) {
        int unitsAmount = mUnitDummies.size();
        int textureManagerElementsInLine = (int) Math.round(Math.sqrt(unitsAmount) + 1);
        int size = textureManagerElementsInLine * SizeConstants.UNIT_FILE_SIZE;
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(
                textureManager, size, size, TextureOptions.BILINEAR);
        int n, m;
        for (int i = 0; i < unitsAmount; i++) {
            UnitDummy unitDummy = mUnitDummies.valueAt(i);
            n = (i % textureManagerElementsInLine) * SizeConstants.UNIT_FILE_SIZE;
            m = (i / textureManagerElementsInLine) * SizeConstants.UNIT_FILE_SIZE;
            unitDummy.loadSpriteResources(playerName, textureAtlas, n, m);
        }
        textureAtlas.load();
        return textureAtlas;
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
    public int getUpgradeCost(BuildingId buildingId) {
        BuildingDummy dummy = mBuildingDummies.get(buildingId.getId());
        if (buildingId.getUpgrade() + 1 >= dummy.getUpgrades()) {
            return -1;
        }
        int costBefore = dummy.getCost(buildingId.getUpgrade());
        int costAfter = dummy.getCost(buildingId.getUpgrade() + 1);
        return calculateBuildingUpgradeCost(costBefore, costAfter);
    }

    @Override
    public int getUpgradeCost(final BuildingId buildingId, final int amount) {
        return getUpgradeCost(buildingId) * amount;
    }

    @Override
    public boolean isUpgradeAvailable(BuildingId buildingId) {
        BuildingDummy dummy = mBuildingDummies.get(buildingId.getId());
        return buildingId.getUpgrade() + 1 < dummy.getUpgrades();
    }

    private void initDummies() {
        initUnitDummies(getUnitListLoader());
        initBuildingDummies(getBuildingListLoader());
    }

    private void initUnitDummies(UnitListLoader unitListLoader) {
        int unitsAmount = unitListLoader.getList().size();
        mUnitDummies = new SparseArray<>(unitsAmount);
        UnitDummy unitDummy;
        for (int i = 0; i < unitsAmount; i++) {
            UnitLoader unitLoader = unitListLoader.getList().get(i);
            if (unitLoader.speed > 1f) {
                unitDummy = new OffenceUnitDummy(unitLoader, getAllianceName());
            } else {
                unitDummy = new DefenceUnitDummy(unitLoader, getAllianceName());
            }
            mUnitDummies.put(unitDummy.getId(), unitDummy);
        }

        mUnitsIds = new TreeSet<>();
        for (int i = 0; i < mUnitDummies.size(); i++) {
            mUnitsIds.add(mUnitDummies.keyAt(i));
        }
    }

    private void initBuildingDummies(BuildingListLoader buildingListLoader) {
        int unitBuildingsAmount = buildingListLoader.getList().size();
        //+3 it's addition buildings (i.e. +defence, +wealth, + special buildings)
        mBuildingDummies = new SparseArray<>(unitBuildingsAmount + 3);
        //units
        BuildingDummy buildingDummy;
        for (int i = 0; i < unitBuildingsAmount; i++) {
            buildingDummy = new OffenceBuildingDummy(buildingListLoader.getList().get(i));
            mBuildingDummies.put(buildingDummy.getBuildingId(), buildingDummy);
        }
        //defence
        buildingDummy = new DefenceBuildingDummy(buildingListLoader.defenceBuildingLoader);
        mBuildingDummies.put(buildingDummy.getBuildingId(), buildingDummy);
        //wealth
        buildingDummy = new WealthBuildingDummy(buildingListLoader.wealthBuildingLoader);
        mBuildingDummies.put(buildingDummy.getBuildingId(), buildingDummy);
        //special
        buildingDummy = new SpecialBuildingDummy(buildingListLoader.specialBuildingLoader);
        mBuildingDummies.put(buildingDummy.getBuildingId(), buildingDummy);

        mBuildingsIds = new TreeSet<>();
        for (int i = 0; i < mBuildingDummies.size(); i++) {
            mBuildingsIds.add(mBuildingDummies.keyAt(i));
        }
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

    protected void loadBuildings_Sprites(TextureManager textureManager, Context context) {
        //creating texture atlas for loading buildings
        int textureManagerElementsInLine = (int)
                Math.round(Math.sqrt(getBuildingsWithUpgradesAmount()) + 1);
        int size = textureManagerElementsInLine * SizeConstants.BUILDING_IMAGE_SIZE;
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(
                textureManager, size, size, TextureOptions.BILINEAR);
        //load
        int n, m;
        for (int i = 0; i < mBuildingDummies.size(); i++) {
            BuildingDummy buildingDummy = mBuildingDummies.valueAt(i);
            n = (i % textureManagerElementsInLine) * SizeConstants.BUILDING_IMAGE_SIZE;
            m = (i / textureManagerElementsInLine) * SizeConstants.BUILDING_IMAGE_SIZE;
            buildingDummy.loadSpriteResources(context, textureAtlas, n, m, getAllianceName());
        }
        textureAtlas.load();
        mBuildingTextureAtlas = textureAtlas;
    }

    protected void loadBuildings_Images(TextureManager textureManager, Context context) {
        int buildingsWithUpgradesAmount = getBuildingsWithUpgradesAmount();
        int atlases = buildingsWithUpgradesAmount % 4 == 0
                ? buildingsWithUpgradesAmount / 4
                : buildingsWithUpgradesAmount / 4 + 1;
        BitmapTextureAtlas[] bitmapTextureAtlases = new BitmapTextureAtlas[atlases];
        int imageSize = SizeConstants.BUILDING_BIG_IMAGE_SIZE;
        int width, height = width = imageSize * 4;
        BuildingDummy buildingDummy;
        int atlasAbscissa = 0, atlasOrdinate = 0, atlasNumber = -1;
        for (int i = 0; i < mBuildingDummies.size(); i++) {
            buildingDummy = mBuildingDummies.valueAt(i);
            for (int upgrade = 0; upgrade < buildingDummy.getUpgrades(); upgrade++) {
                if (atlasAbscissa == 0 && atlasOrdinate == 0) {
                    bitmapTextureAtlases[++atlasNumber] =
                            new BitmapTextureAtlas(
                                    textureManager, width, height, TextureOptions.BILINEAR);
                }
                buildingDummy.loadImageResources(context, bitmapTextureAtlases[atlasNumber],
                        atlasAbscissa * imageSize, atlasOrdinate * imageSize,
                        upgrade, getAllianceName());
                if (++atlasOrdinate == 2) {
                    atlasOrdinate = 0;
                    atlasAbscissa++;
                }
                if (atlasAbscissa == 2) {
                    atlasAbscissa = 0;
                }
            }
        }
        for (BitmapTextureAtlas textureAtlas : bitmapTextureAtlases) {
            textureAtlas.load();
        }
    }

    protected void loadUnits_Images(TextureManager textureManager, Context context) {
        int unitsAmount = mUnitDummies.size();
        int atlases = unitsAmount % 4 == 0 ? unitsAmount / 4 : unitsAmount / 4 + 1;
        int imageSize = SizeConstants.UNIT_BIG_IMAGE_SIZE;
        int width, height = width = imageSize * 4;
        int atlasPosition;
        int position = 0;
        end:
        for (int i = 0; i < atlases; i++) {
            BitmapTextureAtlas textureAtlas =
                    new BitmapTextureAtlas(textureManager, width, height, TextureOptions.BILINEAR);
            for (atlasPosition = 0; atlasPosition < 4; atlasPosition++) {
                if (position >= mUnitDummies.size()) {
                    if (atlasPosition != 0) {
                        textureAtlas.load();
                    }
                    break end;
                }
                mUnitDummies.valueAt(position++).loadImageResources(context, textureAtlas,
                        (atlasPosition % 2) * imageSize, (atlasPosition / 2) * imageSize);
            }
            textureAtlas.load();
        }
    }

    /**
     * calculates building upgrade cost
     *
     * @param costBefore building cost before upgrade
     * @param costAfter  upgraded building cost
     * @return upgrade cost of the single building
     */
    public static int calculateBuildingUpgradeCost(int costBefore, int costAfter) {
        return (int) Math.round(Math.abs(costBefore - costAfter) * 1.5);
    }

    /**
     * calculates multiple buildings upgrade cost
     *
     * @param costBefore building cost before upgrade
     * @param costAfter  upgraded building cost
     * @param amount     amount of buildings
     * @return upgrade cost of multiple buildings
     */
    public static int calculateBuildingUpgradeCost(int costBefore, int costAfter, int amount) {
        return calculateBuildingUpgradeCost(costBefore, costAfter) * amount;
    }
}
