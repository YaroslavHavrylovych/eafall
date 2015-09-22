package com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.unit;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.UnitBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.BasePopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.touch.TouchHelper;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/** present particular unit in description popup */
public class UnitPopupUpdater extends BasePopupUpdater {
    /** unit description object (update description area which u pass to it) */
    private IDescriptionAreaUpdater mDescriptionAreaUpdater;

    public UnitPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mDescriptionAreaUpdater = new com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.unit.DescriptionAreaUpdater(vertexBufferObjectManager, scene);
    }

    @Override
    public void clear() {
        super.clear();
        mDescriptionAreaUpdater.clearDescription();
    }

    @Override
    protected String getDescribedObjectName(Object objectId, String allianceName) {
        BuildingId buildingId = (BuildingId) objectId;
        IAlliance alliance = AllianceHolder.getInstance().getElement(allianceName);
        BuildingDummy buildingDummy = alliance.getBuildingDummy(buildingId);
        int unitId = ((UnitBuildingDummy) buildingDummy).getUnitId(buildingId.getUpgrade());
        return EaFallApplication.getContext().getResources()
                .getString(alliance.getUnitDummy(unitId).getUnitStringId());
    }

    @Override
    protected ITextureRegion getDescriptionImage(Object objectId, String allianceName) {
        BuildingId buildingId = (BuildingId) objectId;
        IAlliance alliance = AllianceHolder.getInstance().getElement(allianceName);
        BuildingDummy buildingDummy = alliance.getBuildingDummy(buildingId);
        int unitId = ((UnitBuildingDummy) buildingDummy).getUnitId(buildingId.getUpgrade());
        return alliance.getUnitDummy(unitId).getImageTextureRegion();
    }

    @Override
    public void updateDescription(Shape drawArea, final Object objectId, String allianceName,
                                  final String playerName) {
        //description
        mDescriptionAreaUpdater.updateDescription(drawArea, objectId, allianceName, playerName);
        //build button
        mBaseButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_back_button));
        mBaseButton.setPosition(mBaseButton.getWidth() / 2, mBaseButton.getHeight() / 2);
        drawArea.attachChild(mBaseButton);
        final BuildingId buildingId = (BuildingId) objectId;
        mBaseButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                EventBus.getDefault().post(new BuildingDescriptionShowEvent(buildingId, playerName));
            }
        });
    }

    @Override
    public void updateAdditionInfo(Shape drawArea, Object objectId, String allianceName,
                                   final String playerName) {
        if (mAdditionDescriptionImage != null) {
            mAdditionDescriptionImage.detachSelf();
        }

        final BuildingId buildingId = (BuildingId) objectId;
        ITextureRegion textureRegion = getAdditionalInformationImage(objectId, allianceName);
        mAdditionDescriptionImage = drawInArea(drawArea, textureRegion);

        mAdditionDescriptionImage.setTouchCallback(
                new TouchHelper.EntityCustomTouch(mAdditionDescriptionImage) {
                    @Override
                    public void click() {
                        super.click();
                        EventBus.getDefault().post(new BuildingDescriptionShowEvent(buildingId, playerName));
                    }
                });
    }

    protected ITextureRegion getAdditionalInformationImage(Object objectId, String allianceName) {
        IAlliance alliance = AllianceHolder.getAlliance(allianceName);
        BuildingId buildingId = (BuildingId) objectId;
        BuildingDummy buildingDummy = alliance.getBuildingDummy(buildingId);
        return buildingDummy.getImageTextureRegionArray(buildingId.getUpgrade());
    }
}
