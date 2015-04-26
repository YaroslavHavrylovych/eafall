package com.gmail.yaroslavlancelot.eafall.game.popup.construction.item;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;

import org.andengine.entity.IEntity;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class PopupItemFactory {
    private PopupItemFactory() {
    }

    public static BuildingPopupItem createBuildingPopupItem(float x, float y, VertexBufferObjectManager objectManager) {
        BuildingsPopupItem buildingsPopupItem = new BuildingsPopupItem(x, y, objectManager);
        return buildingsPopupItem;
    }

    public static interface BuildingPopupItem {
        void setBuildingId(BuildingId buildingId, String allianceName);

        BuildingId getBuildingId();

        void setOnClickListener(StaticHelper.OnClickListener clickListener);

        IEntity getItemEntity();
    }
}
