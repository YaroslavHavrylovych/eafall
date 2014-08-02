package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
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
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.Agent;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.Conscript;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.Demolisher;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.Infantrymen;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.Robot;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.Scout;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.Sniper;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units.Superman;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

/** imperials */
public class Imperials implements IRace {
    /** race name */
    public static final String RACE_NAME = "Imperials";
    private Color mTeamColor;
    private VertexBufferObjectManager mObjectManager;
    private int mBuildingsAmount = 8;
    private SoundOperations mSoundOperations;

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
    public String[] getBuildingsNames() {
        String[] result = new String[getBuildingsAmount()];
        for(int i = 0; i < getBuildingsAmount(); i++) {
            result[i] = LocaleImpl.getInstance().getStringById(getBuildingById(i).getObjectStringId());
        }
        return result;
    }

    private void initBuilding(StaticObject building) {
        building.setBackgroundColor(mTeamColor);
        building.setBackgroundArea();
    }

    @Override
    public int getBuildingCostById(final int buildingId) {
        return getBuildingById(buildingId).getObjectCost();
    }

    @Override
    public Unit getUnitForBuilding(final int buildingId) {
        Unit unit;
        switch (buildingId) {
            case 0:
                unit = new Conscript(mObjectManager, mSoundOperations);
                break;
            case 1:
                unit = new Scout(mObjectManager, mSoundOperations);
                break;
            case 2:
                unit = new Infantrymen(mObjectManager, mSoundOperations);
                break;
            case 3:
                unit = new Sniper(mObjectManager, mSoundOperations);
                break;
            case 4:
                unit = new Agent(mObjectManager, mSoundOperations);
                break;
            case 5:
                unit = new Robot(mObjectManager, mSoundOperations);
                break;
            case 6:
                unit = new Demolisher(mObjectManager, mSoundOperations);
                break;
            case 7:
                unit = new Superman(mObjectManager, mSoundOperations);
                break;
            default:
                throw new IllegalArgumentException("unknown building type=" + buildingId);
        }
        initUnit(unit);
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
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 50, 50, TextureOptions.BILINEAR);
        Conscript.loadResources(context, smallObjectTexture);
        Scout.loadResources(context, smallObjectTexture);
        Infantrymen.loadResources(context, smallObjectTexture);
        Sniper.loadResources(context, smallObjectTexture);
        Agent.loadResources(context, smallObjectTexture);
        Robot.loadResources(context, smallObjectTexture);
        Demolisher.loadResources(context, smallObjectTexture);
        Superman.loadResources(context, smallObjectTexture);
        smallObjectTexture.load();
    }

    private void initUnit(Unit unit) {
        unit.setBackgroundArea();
        unit.setBackgroundColor(mTeamColor);
    }
}
