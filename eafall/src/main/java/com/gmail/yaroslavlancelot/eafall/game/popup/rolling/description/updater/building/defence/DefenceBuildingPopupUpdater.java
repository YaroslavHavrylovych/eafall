package com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.defence;

import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.BaseBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.touch.TouchHelper;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/**
 * For DEFENCE building :
 * <br/>
 * updates buildings description popup
 */
public class DefenceBuildingPopupUpdater extends BaseBuildingPopupUpdater {

    public DefenceBuildingPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mDescriptionAreaUpdater = new DescriptionAreaUpdater(vertexBufferObjectManager, scene);
    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId, String allianceName, final String playerName) {
        super.updateDescription(drawArea, objectId, allianceName, playerName);
        final BuildingId buildingId = (BuildingId) objectId;
        final Object event = new CreateBuildingEvent(mPlayerName, buildingId);
        mFirstButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                EventBus.getDefault().post(event);
            }
        });
    }

    @Override
    public void updateAdditionInfo(Shape drawArea, Object objectId, String allianceName, final String playerName) {
        super.updateAdditionInfo(drawArea, objectId, allianceName, playerName);

        ITextureRegion textureRegion = getAdditionalInformationImage(objectId, allianceName);
        mAdditionDescriptionImage = drawInArea(drawArea, textureRegion);

        final BuildingId buildingId = (BuildingId) objectId;
        mAdditionDescriptionImage.setTouchCallback(
                new TouchHelper.EntityCustomTouch(mAdditionDescriptionImage) {
                    @Override
                    public void click() {
                        super.click();
                        EventBus.getDefault().post(new UnitByBuildingDescriptionShowEvent(buildingId, playerName));
                    }
                });
    }

    protected ITextureRegion getAdditionalInformationImage(Object objectId, String allianceName) {
        IAlliance alliance = AllianceHolder.getAlliance(allianceName);
        BuildingId buildingId = (BuildingId) objectId;
        DefenceBuildingDummy dummy = (DefenceBuildingDummy) alliance.getBuildingDummy(buildingId);
        final int unitId = dummy.getOrbitalStationUnitId();
        return alliance.getUnitDummy(unitId).getImageTextureRegion();
    }
}
