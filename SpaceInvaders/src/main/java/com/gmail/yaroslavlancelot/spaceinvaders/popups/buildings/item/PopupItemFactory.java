package com.gmail.yaroslavlancelot.spaceinvaders.popups.buildings.item;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;

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
        void setBuildingId(BuildingId buildingId, String raceName);

        BuildingId getBuildingId();

        void setOnClickListener(TouchUtils.OnClickListener clickListener);

        IEntity getItemEntity();
    }
}
