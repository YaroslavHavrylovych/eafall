package com.gmail.yaroslavlancelot.spaceinvaders.popups;

import android.graphics.Rect;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.List;

/** Flow popup which contains images and their descriptions */
public class ImageDescriptionPopup {
    private VertexBufferObjectManager mVertexBufferObjectManager;
    private Scene mScene;
    private Rect mAreaForPopup;
    private List<PopupItem> mPopupItems;

    public ImageDescriptionPopup(VertexBufferObjectManager vertexBufferObjectManager, Scene scene, Rect rect) {
        mVertexBufferObjectManager = vertexBufferObjectManager;
        mScene = scene;
        mAreaForPopup = rect;
    }

    /**
     * attach items to display in popup
     *
     * @param itemsList list of items to display
     */
    public void attachMenuItems(final List<PopupItem> itemsList) {
        mPopupItems = itemsList;
    }

    /** show popup */
    public void showPopup() {
        for (PopupItem item : mPopupItems)
            showItem(item);
    }

    /** show one popup item */
    private void showItem(PopupItem popupItem) {
        // picture
        IMenuItem menuItem = new SpriteMenuItem(popupItem.mId, popupItem.mItemTextureRegion, mVertexBufferObjectManager);
        menuItem.setX(mAreaForPopup.left);
        menuItem.setY(mAreaForPopup.top + PopupItem.ITEM_HEIGHT * popupItem.mId);
        // text
        mScene.attachChild(menuItem);
    }

    /** hide popup */
    public void hidePopup() {
    }

    /**
     * {@link com.gmail.yaroslavlancelot.spaceinvaders.popups.ImageDescriptionPopup} building block.
     * Store popup item information.
     */
    public static class PopupItem {
        private static final int ITEM_HEIGHT = 20;
        private static final int ITEM_WIDTH = 20;
        private String mItemName;
        private ITextureRegion mItemTextureRegion;
        private int mId;

        public PopupItem(int id, ITextureRegion itemTextureRegion, String itemName) {
            mId = id;
            mItemName = itemName;
            mItemTextureRegion = itemTextureRegion;
        }
    }
}