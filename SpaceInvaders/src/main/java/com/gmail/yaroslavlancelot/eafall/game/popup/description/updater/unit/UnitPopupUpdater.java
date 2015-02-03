package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.unit;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.BasePopupUpdater;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** present particular unit in description popup */
public class UnitPopupUpdater extends BasePopupUpdater {
    /** unit description object (update description area which u pass to it) */
    private IDescriptionAreaUpdater mDescriptionAreaUpdater;
    /** updates unit addition information area */
    private IDescriptionAreaUpdater mAdditionInformationAreaUpdater;

    public UnitPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mDescriptionAreaUpdater = new com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.unit.DescriptionAreaUpdater(vertexBufferObjectManager, scene);
        mAdditionInformationAreaUpdater = new AdditionalInfoAreaUpdater(vertexBufferObjectManager, scene);
    }

    @Override
    protected String getDescribedObjectName(Object objectId, String raceName) {
        BuildingId buildingId = (BuildingId) objectId;
        IAlliance race = AllianceHolder.getInstance().getElement(raceName);
        BuildingDummy buildingDummy = race.getBuildingDummy(buildingId);
        int unitId;
        if (buildingDummy instanceof CreepBuildingDummy) {
            unitId = ((CreepBuildingDummy) buildingDummy).getMovableUnitId(buildingId.getUpgrade());
        } else if (buildingDummy instanceof DefenceBuildingDummy) {
            unitId = ((DefenceBuildingDummy) buildingDummy).getOrbitalStationUnitId();
        } else {
            return null;
        }
        return EaFallApplication.getContext().getResources()
                .getString(race.getUnitDummy(unitId).getUnitStringId());
    }

    @Override
    public void clear() {
        super.clear();
        mDescriptionAreaUpdater.clearDescription();
        mAdditionInformationAreaUpdater.clearDescription();
    }

    @Override
    protected ITextureRegion getDescriptionImage(Object objectId, String raceName) {
        BuildingId buildingId = (BuildingId) objectId;
        IAlliance race = AllianceHolder.getInstance().getElement(raceName);
        BuildingDummy buildingDummy = race.getBuildingDummy(buildingId);
        int unitId;
        if (buildingDummy instanceof CreepBuildingDummy) {
            unitId = ((CreepBuildingDummy) buildingDummy).getMovableUnitId(buildingId.getUpgrade());
        } else if (buildingDummy instanceof DefenceBuildingDummy) {
            unitId = ((DefenceBuildingDummy) buildingDummy).getOrbitalStationUnitId();
        } else {
            return null;
        }
        return race.getUnitDummy(unitId).getTextureRegion();
    }

    @Override
    public void updateDescription(RectangularShape drawArea, Object objectId, String raceName, String teamName) {
        //description
        mDescriptionAreaUpdater.updateDescription(drawArea, objectId, raceName, teamName);
    }

    @Override
    public void updateAdditionInfo(RectangularShape drawArea, Object objectId, String raceName, String teamName) {
        mAdditionInformationAreaUpdater.updateDescription(drawArea, objectId, raceName, teamName);
    }
}
