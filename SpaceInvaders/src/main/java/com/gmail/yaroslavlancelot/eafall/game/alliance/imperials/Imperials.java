package com.gmail.yaroslavlancelot.eafall.game.alliance.imperials;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.Alliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.BuildingListLoader;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitListLoader;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;

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
        loadUnits(textureManager);
        loadBuildings(textureManager);
    }

    @Override
    protected UnitListLoader getUnitListLoader() {
        return loadObjects(R.raw.imperials_units, UnitListLoader.class);
    }

    @Override
    protected BuildingListLoader getBuildingListLoader() {
        return loadObjects(R.raw.imperials_buildings, BuildingListLoader.class);
    }
}
