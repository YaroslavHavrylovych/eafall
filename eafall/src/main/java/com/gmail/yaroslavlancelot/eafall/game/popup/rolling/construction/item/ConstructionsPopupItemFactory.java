package com.gmail.yaroslavlancelot.eafall.game.popup.rolling.construction.item;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.touch.TouchHelper;

import org.andengine.entity.IEntity;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class ConstructionsPopupItemFactory {
    private ConstructionsPopupItemFactory() {
    }

    public static BuildingPopupItem createConstructionPopupItem(float x, float y, VertexBufferObjectManager objectManager) {
        ConstructionPopupItem constructionPopupItem = new ConstructionPopupItem(x, y, objectManager);
        return constructionPopupItem;
    }

    public interface BuildingPopupItem {
        BuildingId getBuildingId();

        IEntity getItemEntity();

        void setOnClickListener(TouchHelper.OnClickListener clickListener);

        void setBuildingId(BuildingId buildingId, String allianceName);
    }
}
