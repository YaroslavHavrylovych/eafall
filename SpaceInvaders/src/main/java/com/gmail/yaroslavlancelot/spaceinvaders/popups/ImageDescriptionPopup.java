package com.gmail.yaroslavlancelot.spaceinvaders.popups;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.IItemPickListener;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ISpriteTouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

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
        mEntityOperations.attachEntityWithTouchArea(popupItem.mImage);
        mEntityOperations.attachEntityWithTouchArea(popupItem.mText);
    }

    private void attachItems(final PopupItem popupItem) {
        // picture
        popupItem.mItemStaticObject.setOnTouchListener(new ISpriteTouchListener() {
            @Override
            public boolean onTouch(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    popupItem.mItemPickListener.itemPicked(popupItem.mId);
                    return true;
                }
                return false;
            }
        });
        popupItem.mItemStaticObject.setX(mAreaForPopup.left);
        popupItem.mItemStaticObject.setY(
                mAreaForPopup.top + (PopupItem.ITEM_HEIGHT + PopupItem.ITEM_SEPARATOR_LENGTH) * popupItem.mId);
        popupItem.mItemStaticObject.setWidth(PopupItem.ITEM_IMAGE_WIDTH);
        popupItem.mItemStaticObject.setHeight(PopupItem.ITEM_IMAGE_WIDTH);
        // text
        float textX = popupItem.mItemStaticObject.getX() + popupItem.mItemStaticObject.getWidth() + PopupItem.ITEM_SEPARATOR_LENGTH,
                textY = popupItem.mItemStaticObject.getY() + popupItem.mItemStaticObject.getHeight() -
                        FontHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.KEY_FONT_MONEY).getLineHeight();
        Text imageDescriptionText = new

                Text(textX, textY,
                        FontHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.KEY_FONT_MONEY),
                        popupItem.mItemName, mEntityOperations.getObjectManager()) {

                    @Override
                    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX,
                                                 final float pTouchAreaLocalY) {
                        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                            popupItem.mItemPickListener.itemPicked(popupItem.mId);
                            return true;
                        }
                        return false;
                    }
                };
        // attaching
        popupItem.mImage = popupItem.mItemStaticObject;
        popupItem.mText = imageDescriptionText;
    }

    /** hide popup */
    public void hidePopup() {
        if (!mIsPopupShowing)
            return;
        for (PopupItem popupItem : mPopupItems) {
            mEntityOperations.detachEntityWithTouch(popupItem.mImage);
            mEntityOperations.detachEntityWithTouch(popupItem.mText);
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
        private static final int ITEM_HEIGHT = SizeConstants.MONEY_FONT_SIZE;
        /** popup image height */
        private static final int ITEM_IMAGE_WIDTH = 20;
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
        private StaticObject mItemStaticObject;
        private int mId;
        /** already initiated image */
        private IAreaShape mImage;
        /** already initiated text */
        private IAreaShape mText;

        public PopupItem(int id, StaticObject itemStaticObject, String itemName,
                         IItemPickListener itemPickListener) {
            mId = id;
            mItemName = itemName;
            mItemStaticObject = itemStaticObject;
            mItemPickListener = itemPickListener;
        }

        private boolean isItemAttached() {
            return mImage != null && mText != null;
        }
    }
}