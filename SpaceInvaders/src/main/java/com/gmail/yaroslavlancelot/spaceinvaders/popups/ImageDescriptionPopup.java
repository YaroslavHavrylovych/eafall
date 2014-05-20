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
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.FontUtils;
import org.andengine.opengl.font.IFont;

import java.util.List;

/** Flow popup which contains images and their descriptions */
public class ImageDescriptionPopup {
    /** for attaching/detaching {@link org.andengine.entity.sprite.Sprite} */
    private EntityOperations mEntityOperations;
    /**
     * {@link com.gmail.yaroslavlancelot.spaceinvaders.popups.ImageDescriptionPopup.PopupItem}
     * that should be displayed. Passed with attachMenuItems(items)
     */
    private List<PopupItem> mPopupItems;
    /** represent boolean value which true if popup is showing now and false in other way */
    private boolean mIsPopupShowing;
    /** popup text font */
    private IFont mFont = FontHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.KEY_FONT_MONEY);
    /** used for settings x offset for text in popup element (same for all items) */
    private float mTextAbscissaPadding = SizeConstants.BUILDING_POPUP_ELEMENT_HEIGHT
            + 2 * SizeConstants.BUILDING_POPUP_IMAGE_PADDING;
    /** used for settings y offset for text in popup element (same for all items) */
    private float mTextOrdinatePadding = SizeConstants.BUILDING_POPUP_ELEMENT_HEIGHT
            + SizeConstants.BUILDING_POPUP_IMAGE_PADDING - mFont.getLineHeight();
    /** popup width which can be changed with call {@code recalculatePopupBoundaries()} */
    private float mPopupWidth;

    public ImageDescriptionPopup(EntityOperations entityOperations) {
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

    public void recalculatePopupBoundaries() {
        if (mPopupItems == null || mPopupItems.isEmpty()) return;

        float lengthWithoutText = SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT
                + SizeConstants.BUILDING_POPUP_AFTER_TEXT_PADDING;

        float maxTextLength = 0f;
        for (PopupItem popupItem : mPopupItems)
            maxTextLength = Math.max(FontUtils.measureText(mFont, popupItem.mItemName), maxTextLength);

        mPopupWidth = lengthWithoutText + maxTextLength;
    }

    /** show popup */
    public void showPopup() {
        if (mIsPopupShowing)
            return;
        for (PopupItem item : mPopupItems)
            showItem(item);
        mIsPopupShowing = true;
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
        background.setPosition(0, SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT * popupItem.mId);
        background.setWidth(mPopupWidth);
        background.setHeight(SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT);
        // picture
        popupItem.mItemGameObject.setPosition(SizeConstants.BUILDING_POPUP_IMAGE_PADDING, SizeConstants.BUILDING_POPUP_IMAGE_PADDING);
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
        Text imageDescriptionText = new Text(mTextAbscissaPadding, mTextOrdinatePadding, mFont,
                popupItem.mItemName, mEntityOperations.getObjectManager());
        background.attachChild(imageDescriptionText);
        // attaching
        popupItem.mText = imageDescriptionText;
        popupItem.mBackground = background;
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