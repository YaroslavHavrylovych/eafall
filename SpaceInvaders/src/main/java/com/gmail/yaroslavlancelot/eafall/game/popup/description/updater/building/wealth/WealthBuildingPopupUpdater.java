package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.wealth;

import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.building.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.BaseBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;

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
    public void updateAdditionInfo(Shape drawArea, Object objectId, String allianceName, String teamName) {

    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId, String allianceName, String teamName) {
        super.updateDescription(drawArea, objectId, allianceName, teamName);
        final BuildingId buildingId = (BuildingId) objectId;
        BuildingDummy buildingDummy = AllianceHolder.getAlliance(allianceName).getBuildingDummy(buildingId);
        ITeam team = TeamsHolder.getTeam(teamName);
        final Object event;
        if (team.getPlanet().getBuildingsAmount(buildingId.getId())
                >= buildingDummy.getAmountLimit()) {
            event = null;
        } else {
            event = new CreateBuildingEvent(mTeamName, buildingId);
        }
        mButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (event != null) {
                    EventBus.getDefault().post(event);
                }
            }
        });
    }
}
