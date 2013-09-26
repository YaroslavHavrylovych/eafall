package com.gmail.yaroslavlancelot.spaceinvaders.popups;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.List;

public class CreateBuildingPopup extends MenuScene {
    public void attachMenuItems(final List<PopupItem> itemsList, VertexBufferObjectManager vertexBufferObjectManager) {
        for(int i = 0; i < itemsList.size(); i++)
            addMenuItem(i, itemsList.get(i), vertexBufferObjectManager);
    }

    private void addMenuItem(int id, final PopupItem item, VertexBufferObjectManager vertexBufferObjectManager) {
        IMenuItem menuItem = new SpriteMenuItem(id, item.mElementTextureRegion, vertexBufferObjectManager);
        addMenuItem(menuItem);
    }

    public static class PopupItem {
        private static final int ITEM_HEIGHT = 20;
        private static final int ITEM_WIDTH = 20;
        private String mElementName;
        private ITextureRegion mElementTextureRegion;

        public PopupItem(ITextureRegion elementTextureRegion, String elementName) {
            mElementName = elementName;
            mElementTextureRegion = elementTextureRegion;
        }
    }
}