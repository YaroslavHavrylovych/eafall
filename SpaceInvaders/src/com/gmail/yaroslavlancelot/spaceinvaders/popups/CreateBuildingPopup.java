package com.gmail.yaroslavlancelot.spaceinvaders.popups;

import android.graphics.Rect;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.List;

public class CreateBuildingPopup {
    private VertexBufferObjectManager mVertexBufferObjectManager;
    private Scene mScene;
    private Rect mAreaForPopup;
    private List<PopupItem> mPopupItems;

    public CreateBuildingPopup(VertexBufferObjectManager vertexBufferObjectManager, Scene scene, Rect rect) {
        mVertexBufferObjectManager = vertexBufferObjectManager;
        mScene = scene;
        mAreaForPopup = rect;
    }

    public void attachMenuItems(final List<PopupItem> itemsList) {
        mPopupItems = itemsList;
    }

    public void showPopup() {
        for (PopupItem item : mPopupItems)
            showItem(item);
    }

    private void showItem(PopupItem popupItem) {
        // picture
        IMenuItem menuItem = new SpriteMenuItem(popupItem.mId, popupItem.mElementTextureRegion, mVertexBufferObjectManager);
        menuItem.setX(mAreaForPopup.left);
        menuItem.setY(mAreaForPopup.top + PopupItem.ITEM_HEIGHT * popupItem.mId);
        // text
        mScene.attachChild(menuItem);
    }

    public void hidePopup() {
    }

    public static class PopupItem {
        private static final int ITEM_HEIGHT = 20;
        private static final int ITEM_WIDTH = 20;
        private String mElementName;
        private ITextureRegion mElementTextureRegion;
        private int mId;

        public PopupItem(int id, ITextureRegion elementTextureRegion, String elementName) {
            mId = id;
            mElementName = elementName;
            mElementTextureRegion = elementTextureRegion;
        }
    }
}