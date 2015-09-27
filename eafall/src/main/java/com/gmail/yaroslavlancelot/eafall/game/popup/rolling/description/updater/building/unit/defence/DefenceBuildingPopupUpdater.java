package com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.unit.defence;

import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.unit.UnitBuildingPopupUpdater;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * For DEFENCE building :
 * <br/>
 * updates buildings description popup
 */
public class DefenceBuildingPopupUpdater extends UnitBuildingPopupUpdater {
    public DefenceBuildingPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mDescriptionAreaUpdater = new DefenceBuildingDescriptionAreaUpdater(vertexBufferObjectManager, scene);
    }
}
