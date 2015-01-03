package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building.wealth;

import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building.BaseBuildingPopupUpdater;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * For WEALTH building : <br/>
 * updates buildings description popup
 */
public class WealthBuildingPopupUpdater extends BaseBuildingPopupUpdater {
    public WealthBuildingPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mDescriptionAreaUpdater = new DescriptionAreaUpdater(vertexBufferObjectManager, scene);
    }

    @Override
    public void updateAdditionInfo(RectangularShape drawArea, Object objectId, String raceName, String teamName) {

    }
}
