package com.gmail.yaroslavlancelot.eafall.game.alliance.rebels;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.Alliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.BuildingListLoader;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitListLoader;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Rebels extends Alliance {
    public static final String TAG = Rebels.class.getCanonicalName();
    /** alliance name */
    public static final String ALLIANCE_NAME = "Rebels";

    public Rebels(final VertexBufferObjectManager objectManager) {
        super(objectManager);
    }

    @Override
    public String getAllianceName() {
        return ALLIANCE_NAME;
    }

    @Override
    public void loadAllianceResources(final TextureManager textureManager) {
        loadBuildings(textureManager);
        loadUnits(textureManager);
    }

    @Override
    protected UnitListLoader getUnitListLoader() {
        return loadObjects(R.raw.rebels_units, UnitListLoader.class);
    }

    @Override
    protected BuildingListLoader getBuildingListLoader() {
        return loadObjects(R.raw.rebels_buildings, BuildingListLoader.class);
    }
}
