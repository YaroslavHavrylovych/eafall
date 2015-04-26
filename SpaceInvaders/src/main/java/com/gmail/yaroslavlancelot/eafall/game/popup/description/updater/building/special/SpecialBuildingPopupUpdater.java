package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.special;

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
public class SpecialBuildingPopupUpdater extends BaseBuildingPopupUpdater {
    public SpecialBuildingPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mDescriptionAreaUpdater = new DescriptionAreaUpdater(vertexBufferObjectManager, scene);
    }

    @Override
    public void updateAdditionInfo(Shape drawArea, Object objectId, String allianceName, String teamName) {

    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId, String allianceName, String teamName) {
        super.updateDescription(drawArea, objectId, allianceName, teamName);
        final BuildingId buildingId = (BuildingId) objectId;

        mButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                EventBus.getDefault().post(new CreateBuildingEvent(mTeamName, buildingId));
            }
        });
    }
}
