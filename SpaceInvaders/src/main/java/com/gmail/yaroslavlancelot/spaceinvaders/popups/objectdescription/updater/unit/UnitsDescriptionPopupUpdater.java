package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.unit;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.BaseDescriptionPopupUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.RacesHolder;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** present particular unit in description popup */
public class UnitsDescriptionPopupUpdater extends BaseDescriptionPopupUpdater {
    /** unit description object (update description area which u pass to it) */
    private DescriptionAreaUpdater mDescriptionAreaUpdater;
    /** updates unit addition information area */
    private DescriptionAreaUpdater mAdditionInformationAreaUpdater;

    public UnitsDescriptionPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mDescriptionAreaUpdater = new UnitDescriptionAreaUpdater(vertexBufferObjectManager, scene);
        mAdditionInformationAreaUpdater = new UnitAdditionalAreaUpdater(vertexBufferObjectManager, scene);
    }

    @Override
    protected String getDescribedObjectName(Object objectId, String raceName) {
        BuildingId buildingId = (BuildingId) objectId;
        IRace race = RacesHolder.getInstance().getElement(raceName);
        int unitId = race.getBuildingDummy(buildingId).getUnitId(buildingId.getUpgrade());
        return race.getUnitDummy(unitId).getName();
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
        IRace race = RacesHolder.getInstance().getElement(raceName);
        int unitId = race.getBuildingDummy(buildingId).getUnitId(buildingId.getUpgrade());
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
