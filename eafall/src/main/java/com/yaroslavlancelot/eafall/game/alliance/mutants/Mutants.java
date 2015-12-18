package com.yaroslavlancelot.eafall.game.alliance.mutants;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.alliance.Alliance;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.BuildingListLoader;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitListLoader;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Mutants extends Alliance {
    public static final String TAG = Mutants.class.getCanonicalName();
    /** alliance name */
    public static final String ALLIANCE_NAME = "Mutants";

    public Mutants(final VertexBufferObjectManager objectManager) {
        super(objectManager);
    }

    @Override
    public String getAllianceName() {
        return ALLIANCE_NAME;
    }

    @Override
    public int getAllianceStringRes() {
        return R.string.mutants;
    }

    @Override
    protected UnitListLoader getUnitListLoader() {
        return loadObjects(R.raw.mutants_units, UnitListLoader.class);
    }

    @Override
    protected BuildingListLoader getBuildingListLoader() {
        return loadObjects(R.raw.mutants_buildings, BuildingListLoader.class);
    }
}
