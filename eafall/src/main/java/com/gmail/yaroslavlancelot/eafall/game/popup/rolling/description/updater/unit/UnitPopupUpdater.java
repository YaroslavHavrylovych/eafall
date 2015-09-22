package com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.unit;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.UnitBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.BasePopupUpdater;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

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
    public void updateDescription(Shape drawArea, Object objectId, String allianceName, String playerName) {
        //description
        mDescriptionAreaUpdater.updateDescription(drawArea, objectId, allianceName, playerName);
    }

    @Override
    public void updateAdditionInfo(Shape drawArea, Object objectId, String allianceName, String playerName) {
    }
}
