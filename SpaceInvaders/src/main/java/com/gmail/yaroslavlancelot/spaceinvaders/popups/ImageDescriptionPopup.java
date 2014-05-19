package com.gmail.yaroslavlancelot.spaceinvaders.popups;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.IItemPickListener;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;

import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.text.Text;

import java.util.List;

/** Flow popup which contains images and their descriptions */
public class ImageDescriptionPopup {
    /** for attaching/detaching {@link org.andengine.entity.sprite.Sprite} */
    private EntityOperations mEntityOperations;
    /** form parent activity (set in constructor) */
    private Area mAreaForPopup;
    /**
     * {@link com.gmail.yaroslavlancelot.spaceinvaders.popups.ImageDescriptionPopup.PopupItem}
     * that should be displayed. Passed with attachMenuItems(items)
     */
    private List<PopupItem> mPopupItems;
    /** represent boolean value which true if popup is showing now and false in other way */
    private boolean mIsPopupShowing;

    public ImageDescriptionPopup(EntityOperations entityOperations, Area areaForPopup) {
        mAreaForPopup = areaForPopup;
        mEntityOperations = entityOperations;
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
        if (mIsPopupShowing)
            return;
        for (PopupItem item : mPopupItems)
            showItem(item);
        mIsPopupShowing = true;
    }

    /** show one popup item */
    private void showItem(PopupItem popupItem) {
        if (!popupItem.isItemAttached())
            attachItems(popupItem);
        // show element on screen
        mEntityOperations.attachEntityWithTouchToHud(popupItem.mBackground);
    }

    private void attachItems(final PopupItem popupItem) {
        // background
        final PopupItemBackgroundSprite background = popupItem.mBackground;
        background.setX(mAreaForPopup.left);
        background.setY(mAreaForPopup.top + PopupItem.ITEM_HEIGHT * popupItem.mId);
        background.setWidth(SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_WIDTH);
        background.setHeight(SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT);
        // picture
        popupItem.mItemGameObject.setPosition(5f, 10f);
        popupItem.mItemGameObject.setWidth(PopupItem.ITEM_IMAGE_WIDTH);
        popupItem.mItemGameObject.setHeight(PopupItem.ITEM_IMAGE_WIDTH);
        background.setOnTouchListener(new TouchUtils.CustomTouchListener(new Area(background.getX(), background.getY(), background.getWidth(), background.getHeight())) {
            @Override
            public void click() { popupItem.mItemPickListener.itemPicked(popupItem.mId); }

            @Override
            public void unPress() { background.unpress(); }

            @Override
            public void press() { background.press(); }
        });
        background.attachChild(popupItem.mItemGameObject);
        // text
        float textX = popupItem.mItemGameObject.getWidth() + PopupItem.ITEM_SEPARATOR_LENGTH,
                textY = popupItem.mItemGameObject.getHeight() -
                        FontHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.KEY_FONT_MONEY).getLineHeight();
        Text imageDescriptionText = new Text(textX, textY,
                FontHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.KEY_FONT_MONEY),
                popupItem.mItemName, mEntityOperations.getObjectManager());
        background.attachChild(imageDescriptionText);
        // attaching
        popupItem.mText = imageDescriptionText;
        popupItem.mBackground = background;
    }

    /** hide popup */
    public void hidePopup() {
        if (!mIsPopupShowing)
            return;
        for (PopupItem popupItem : mPopupItems) {
            mEntityOperations.detachEntityFromHud(popupItem.mBackground);
        }
        mIsPopupShowing = false;
    }

    public boolean isShowing() {
        return mIsPopupShowing;
    }

    /**
     * {@link com.gmail.yaroslavlancelot.spaceinvaders.popups.ImageDescriptionPopup} building block.
     * Store popup item information.
     */
    public static class PopupItem {
        /** popup image height */
        private static final int ITEM_HEIGHT = SizeConstants.BUILDING_POPUP_ELEMENT_HEIGHT;
        /** popup image height */
        private static final int ITEM_IMAGE_WIDTH = ITEM_HEIGHT;
        /** separator between elements/items */
        private static final int ITEM_SEPARATOR_LENGTH = 5;
        /** current item touched */
        private final IItemPickListener mItemPickListener;
        /** description for image of current element */
        private String mItemName;
        /**
         * {@link org.andengine.opengl.texture.region.ITextureRegion} which should be
         * displayed with current popup item
         */
        private GameObject mItemGameObject;
        private int mId;
        /** already initiated text */
        private IAreaShape mText;
        /** background image */
        private PopupItemBackgroundSprite mBackground;

        public PopupItem(int id, GameObject itemGameObject, String itemName, IItemPickListener itemPickListener,
                         PopupItemBackgroundSprite background) {
            mId = id;
            mItemName = itemName;
            mItemGameObject = itemGameObject;
            mItemPickListener = itemPickListener;
            mBackground = background;
        }

        private boolean isItemAttached() {
            return mText != null;
        }
    }
}