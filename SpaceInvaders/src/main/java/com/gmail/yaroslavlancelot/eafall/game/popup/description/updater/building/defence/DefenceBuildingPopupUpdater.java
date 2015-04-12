package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.defence;

import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.building.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.BaseBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/**
 * For DEFENCE building : <br/>
 * updates buildings description popup
 */
public class DefenceBuildingPopupUpdater extends BaseBuildingPopupUpdater {

    public DefenceBuildingPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mDescriptionAreaUpdater = new DescriptionAreaUpdater(vertexBufferObjectManager, scene);
    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId, String raceName, final String teamName) {
        super.updateDescription(drawArea, objectId, raceName, teamName);
        final BuildingId buildingId = (BuildingId) objectId;
        final Object event = new CreateBuildingEvent(mTeamName, buildingId);
        mBuildOrBackButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                EventBus.getDefault().post(event);
            }
        });
    }

    @Override
    public void updateAdditionInfo(Shape drawArea, Object objectId, String raceName, final String teamName) {
        if (mAdditionDescriptionImage != null) {
            mAdditionDescriptionImage.detachSelf();
            mAdditionInfoRectangle.detachSelf();
        }
        mAdditionDescriptionImage = new Sprite(0, 0, drawArea.getWidth(), drawArea.getHeight(),
                getAdditionalInformationImage(objectId, raceName), mVertexBufferObjectManager);

        mAdditionInfoRectangle.setWidth(drawArea.getWidth());
        mAdditionInfoRectangle.setHeight(drawArea.getHeight());

        final BuildingId buildingId = (BuildingId) objectId;
        mAdditionInfoRectangle.setTouchCallback(
                new StaticHelper.CustomTouchListener(mAdditionDescriptionImage) {
                    @Override
                    public void click() {
                        super.click();
                        EventBus.getDefault().post(new UnitByBuildingDescriptionShowEvent(buildingId, teamName));
                    }
                });
        mAdditionInfoRectangle.attachChild(mAdditionDescriptionImage);
        drawArea.attachChild(mAdditionInfoRectangle);
    }

    protected ITextureRegion getAdditionalInformationImage(Object objectId, String raceName) {
        IAlliance race = AllianceHolder.getRace(raceName);
        BuildingId buildingId = (BuildingId) objectId;
        DefenceBuildingDummy dummy = (DefenceBuildingDummy) race.getBuildingDummy(buildingId);
        final int unitId = dummy.getOrbitalStationUnitId();
        return race.getUnitDummy(unitId).getTextureRegion();
    }
}
