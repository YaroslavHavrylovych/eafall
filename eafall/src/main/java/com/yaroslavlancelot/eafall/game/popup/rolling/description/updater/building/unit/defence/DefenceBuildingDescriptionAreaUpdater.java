package com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.unit.defence;

import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.unit.UnitBuildingDescriptionAreaUpdater;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * For CREEP building : <br/>
 * writes building description on given area (p.s. only description popup). Knows about other
 * buttons (e.g. build button) on the description popup so place text with knowing of this.
 */
public class DefenceBuildingDescriptionAreaUpdater extends UnitBuildingDescriptionAreaUpdater {
    public DefenceBuildingDescriptionAreaUpdater(VertexBufferObjectManager vboManager, Scene scene) {
        super(vboManager, scene);
    }
}
