package com.gmail.yaroslavlancelot.spaceinvaders.popups;

import android.graphics.Rect;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.List;

/** Flow popup which contains images and their descriptions */
public class ImageDescriptionPopup {
    /** form parent activity (set in constructor) */
    private VertexBufferObjectManager mVertexBufferObjectManager;
    /** for attaching/detaching {@link org.andengine.entity.sprite.Sprite} */
    private EntityOperations mEntityOperations;
    /** form parent activity (set in constructor) */
    private Rect mAreaForPopup;
    /**
     * {@link com.gmail.yaroslavlancelot.spaceinvaders.popups.ImageDescriptionPopup.PopupItem}
     * that should be displayed. Passed with attachMenuItems(items)
     */
    private List<PopupItem> mPopupItems;
    /** showed elements */
    private ArrayList<Sprite> mPopupShowedElements;

    public ImageDescriptionPopup(VertexBufferObjectManager vertexBufferObjectManager, EntityOperations entityOperations, Rect rect) {
        mVertexBufferObjectManager = vertexBufferObjectManager;
        mAreaForPopup = rect;
        mEntityOperations = entityOperations;
    }

    /**
     * attach items to display in popup
     *
     * @param itemsList list of items to display
     */
    public void attachMenuItems(final List<PopupItem> itemsList) {
        mPopupItems = itemsList;
        mPopupShowedElements = new ArrayList<Sprite>(itemsList.size() * 2);
    }

    /** show popup */
    public void showPopup() {
        for (PopupItem item : mPopupItems)
            showItem(item);
    }

    /** show one popup item */
    private void showItem(PopupItem popupItem) {
        // picture
        IMenuItem imageMenuItem = new SpriteMenuItem(popupItem.mId, popupItem.mItemTextureRegion, mVertexBufferObjectManager);
        imageMenuItem.setX(mAreaForPopup.left);
        imageMenuItem.setY(mAreaForPopup.top + (PopupItem.ITEM_HEIGHT + PopupItem.ITEM_SEPARATOR_LENGTH) * popupItem.mId);
        imageMenuItem.setWidth(PopupItem.ITEM_IMAGE_WIDTH);
        imageMenuItem.setHeight(PopupItem.ITEM_HEIGHT);
        // text
        float textX = imageMenuItem.getX() + imageMenuItem.getWidth() + PopupItem.ITEM_SEPARATOR_LENGTH,
                textY = imageMenuItem.getY() + imageMenuItem.getHeight() -
                        FontHolderUtils.getInstance().getElement(GameStringConstants.KEY_FONT_MONEY).getLineHeight();
        Text imageDescriptionText = new Text(textX, textY,
                FontHolderUtils.getInstance().getElement(GameStringConstants.KEY_FONT_MONEY),
                popupItem.mItemName, mVertexBufferObjectManager);
        // attach
        mEntityOperations.attachEntity(imageMenuItem);
        mEntityOperations.attachEntity(imageDescriptionText);
    }

    /** hide popup */
    public void hidePopup() {
        mPopupShowedElements = null;
    }

    /**
     * {@link com.gmail.yaroslavlancelot.spaceinvaders.popups.ImageDescriptionPopup} building block.
     * Store popup item information.
     */
    public static class PopupItem {
        /** popup image height */
        private static final int ITEM_HEIGHT = 20;
        /** popup image height */
        private static final int ITEM_IMAGE_WIDTH = 20;
        /** separator between elements/items */
        private static final int ITEM_SEPARATOR_LENGTH = 5;
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