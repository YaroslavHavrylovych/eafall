package com.gmail.yaroslavlancelot.spaceinvaders.alliances.imperials;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.buildings.BuildingListLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.loading.units.UnitListLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.Alliance;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** imperials */
public class Imperials extends Alliance {
    public static final String TAG = Imperials.class.getCanonicalName();
    /** race name */
    public static final String ALLIANCE_NAME = "Imperials";

    public Imperials(final VertexBufferObjectManager objectManager, final SoundOperations soundOperations) {
        super(objectManager, soundOperations);
    }

    @Override
    public String getAllianceName() {
        return ALLIANCE_NAME;
    }

    @Override
    public void loadResources(final TextureManager textureManager) {
        loadBuildings(textureManager);

        loadUnits(textureManager);
    }

    private void loadBuildings(TextureManager textureManager) {
        LoggerHelper.printDebugMessage(TAG, "loading buildings");
        BuildingListLoader buildingListLoader = loadObjects(R.raw.imperials_buildings, BuildingListLoader.class);

        loadBuildings(textureManager, buildingListLoader);
    }

    private void loadUnits(TextureManager textureManager) {
        LoggerHelper.printDebugMessage(TAG, "loading units");
        UnitListLoader unitListLoader = loadObjects(R.raw.imperials_units, UnitListLoader.class);

        loadUnits(textureManager, unitListLoader);
    }
}
