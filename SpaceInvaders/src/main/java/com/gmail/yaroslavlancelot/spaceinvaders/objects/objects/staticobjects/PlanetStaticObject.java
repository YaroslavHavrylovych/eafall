package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.staticobjects;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.BuildingType;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings.CreepBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings.DefenceBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings.IBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings.SpecialBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings.WealthBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dummies.BuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dummies.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dummies.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dummies.SpecialBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dummies.WealthBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** represent team planet */
public class PlanetStaticObject extends StaticObject {
    /** tag, which is used for debugging purpose */
    public static final String TAG = PlanetStaticObject.class.getCanonicalName();
    // unit spawn point
    private float mSpawnPointX, mSpawnPointY;
    // buildings in current planet
    private Map<Integer, IBuilding> mBuildings = new HashMap<Integer, IBuilding>(10);
    /** the team, current planet belongs to */
    private ITeam mPlanetTeam;

    public PlanetStaticObject(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager objectManager, ITeam planetTeam) {
        super(x, y, textureRegion, objectManager);
        mIncomeIncreasingValue = 10;
        mPlanetTeam = planetTeam;
        mObjectArmor = new Armor(Armor.ArmorType.MIXED.name(), 10);
        setWidth(SizeConstants.PLANET_DIAMETER);
        setHeight(SizeConstants.PLANET_DIAMETER);
        initHealth(3000);
    }

    @Override
    public int getIncome() {
        int value = super.getIncome();
        int percentIncrease = 0;
        for (IBuilding building : mBuildings.values()) {
            if (building.getBuildingType() == BuildingType.SPECIAL_BUILDING) {
                continue;
            }
            if (building.getBuildingType() == BuildingType.WEALTH_BUILDING) {
                percentIncrease += building.getIncome();
                continue;
            }
            value += building.getIncome();
        }
        return value + (int) (value * (((float) percentIncrease) / 100));
    }

    /** set unit spawn point */
    public void setSpawnPoint(float spawnPointX, float spawnPointY) {
        mSpawnPointX = spawnPointX;
        mSpawnPointY = spawnPointY;
    }

    public float getSpawnPointX() {
        return mSpawnPointX;
    }

    public float getSpawnPointY() {
        return mSpawnPointY;
    }

    /**
     * Invoke if you want to create new building. If building is doing some real action
     * (it's on the server side or it's single player and not an client which just is showing)
     * then team money will be reduced.
     *
     * @param buildingId id of the building you want to create
     * @return true if building amount was increased and false in other case
     */
    public boolean createBuilding(BuildingId buildingId) {
        LoggerHelper.methodInvocation(TAG, "createBuilding");
        IBuilding building = mBuildings.get(buildingId.getId());
        if (building == null) {
            final BuildingDummy buildingDummy = mPlanetTeam.getTeamRace().getBuildingDummy(buildingId);
            if (buildingDummy == null) {
                throw new IllegalArgumentException("no building with id " + buildingId);
            }

            switch (buildingDummy.getBuildingType()) {
                case CREEP_BUILDING: {
                    building = new CreepBuilding((CreepBuildingDummy) buildingDummy, getVertexBufferObjectManager(),
                            mPlanetTeam.getTeamName());
                    break;
                }
                case WEALTH_BUILDING: {
                    building = new WealthBuilding((WealthBuildingDummy) buildingDummy, getVertexBufferObjectManager(),
                            mPlanetTeam.getTeamName());
                    break;
                }
                case SPECIAL_BUILDING: {
                    building = new SpecialBuilding((SpecialBuildingDummy) buildingDummy, getVertexBufferObjectManager(),
                            mPlanetTeam.getTeamName());
                    break;
                }
                case DEFENCE_BUILDING: {
                    building = new DefenceBuilding((DefenceBuildingDummy) buildingDummy, getVertexBufferObjectManager(),
                            mPlanetTeam.getTeamName());
                    break;
                }
                default: {
                    throw new IllegalStateException("unknown building type in create building");
                }
            }
            attachChild(building.getEntity());
            mBuildings.put(buildingId.getId(), building);
        }
        return building.buyBuilding();
    }

    /** get buildings amount for passed building type */
    public int getBuildingsAmount(int buildingId) {
        IBuilding buildings = mBuildings.get(buildingId);
        if (buildings == null) return 0;
        return buildings.getAmount();
    }

    public int getExistingBuildingsTypesAmount() {
        return mBuildings.size();
    }

    public Set<Integer> getExistingBuildingsTypes() {
        return mBuildings.keySet();
    }

    public IBuilding getBuilding(int id) {
        return mBuildings.get(id);
    }

    public static void loadImages(Context context, TextureManager textureManager){
        BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(textureManager,
                256, 128, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.getInstance().addElement(StringsAndPathUtils.KEY_RED_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        bitmapTextureAtlas, context, StringsAndPathUtils.FILE_RED_PLANET, 0, 0));
        TextureRegionHolderUtils.getInstance().addElement(StringsAndPathUtils.KEY_BLUE_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        bitmapTextureAtlas, context, StringsAndPathUtils.FILE_BLUE_PLANET, 128, 0));
        bitmapTextureAtlas.load();
    }
}