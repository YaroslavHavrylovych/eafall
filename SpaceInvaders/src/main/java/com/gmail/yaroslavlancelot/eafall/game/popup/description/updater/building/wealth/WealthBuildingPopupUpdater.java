package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.wealth;

import com.gmail.yaroslavlancelot.eafall.game.eventbus.building.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.BaseBuildingPopupUpdater;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

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
    public void updateAdditionInfo(Shape drawArea, Object objectId, String raceName, String teamName) {

    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId, String raceName, String teamName) {
        super.updateDescription(drawArea, objectId, raceName, teamName);
        final BuildingId buildingId = (BuildingId) objectId;

        mBuildOrBackButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                EventBus.getDefault().post(new CreateBuildingEvent(mTeamName, buildingId));
            }
        });
    }
}
