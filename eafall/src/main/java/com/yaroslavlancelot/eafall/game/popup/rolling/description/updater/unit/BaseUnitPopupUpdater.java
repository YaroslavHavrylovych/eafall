package com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.unit;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.UnitBuildingDummy;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.BasePopupUpdater;
import com.yaroslavlancelot.eafall.game.touch.TouchHelper;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/** present particular unit in description popup */
public abstract class BaseUnitPopupUpdater extends BasePopupUpdater {
    /** unit description object (update description area which u pass to it) */
    private IDescriptionAreaUpdater mDescriptionAreaUpdater;

    public BaseUnitPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mDescriptionAreaUpdater = new DescriptionAreaUpdater(vertexBufferObjectManager, scene);
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
    public void updateDescription(Shape drawArea, Object objectId, String allianceName, String playerName) {
        //description
        mDescriptionAreaUpdater.updateDescription(drawArea, objectId, allianceName, playerName);
        //build button
        updateBaseButton(drawArea, objectId, playerName);
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

    protected abstract void updateBaseButton(Shape drawArea, final Object objectId, final String playerName);

    protected ITextureRegion getAdditionalInformationImage(Object objectId, String allianceName) {
        IAlliance alliance = AllianceHolder.getAlliance(allianceName);
        BuildingId buildingId = (BuildingId) objectId;
        BuildingDummy buildingDummy = alliance.getBuildingDummy(buildingId);
        return buildingDummy.getImageTextureRegionArray(buildingId.getUpgrade());
    }
}
