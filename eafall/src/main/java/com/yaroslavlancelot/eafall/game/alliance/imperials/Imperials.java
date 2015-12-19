package com.yaroslavlancelot.eafall.game.alliance.imperials;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.alliance.Alliance;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.loader.BuildingListLoader;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitListLoader;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** imperials */
public class Imperials extends Alliance {
    public static final String TAG = Imperials.class.getCanonicalName();
    /** alliance name */
    public static final String ALLIANCE_NAME = "Imperials";

    public Imperials(final VertexBufferObjectManager objectManager) {
        super(objectManager);
    }

    @Override
    public String getAllianceName() {
        return ALLIANCE_NAME;
    }

    @Override
    public int getAllianceStringRes() {
        return R.string.imperials;
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
