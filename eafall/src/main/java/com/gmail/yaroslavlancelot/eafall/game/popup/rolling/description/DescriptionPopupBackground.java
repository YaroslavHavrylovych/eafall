package com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.IPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.touch.TouchHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.ArrowButtonSprite;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

/**
 * Background sprite for description popup.
 * Contains all operation to draw this popup.
 * Used by handler {@code DescriptionPopup} to step-by-step display popup on screen.
 * Generally this sprite with inner elements IS popup but handler contains logic
 * to redraw it and display correctly.
 */
public class DescriptionPopupBackground extends Sprite {
    /** font key */
    private static final String sDescriptionFontKey = "key_objects_description_object_name_text_font";
    /** described object name */
    protected Text mObjectNameText;

    // next three fields are just split popup on display areas
    /** descript object image */
    private Shape mImageShape;
    /** descript object description area */
    private Shape mDescriptionShape;
    /** descript object addition information field */
    private Shape mAdditionalInformationShape;
    /** additional information image */
    private Sprite mLeftBlockImage;
    /** left popup arrow (near header) */
    private ButtonSprite mLeftArrow;
    /** right popup arrow (near header) */
    private ButtonSprite mRightArrow;

    DescriptionPopupBackground(float x, float y, float width, float height,
                               VertexBufferObjectManager vboManager) {
        super(x, y, width, height,
                TextureRegionHolder.getInstance()
                        .getElement(StringConstants.FILE_DESCRIPTION_POPUP_BACKGROUND),
                vboManager);
        initAreas();
        initObjectNameText();
        initLeftBlock();
        initArrows();
    }

    public void setLeftBlockVisibility(boolean visibility) {
        mLeftBlockImage.setVisible(visibility);
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float touchAreaLocalX, float touchAreaLocalY) {
        return isVisible() && super.onAreaTouched(pSceneTouchEvent, touchAreaLocalX, touchAreaLocalY);
    }

    /** instantiate and init positions for buttons */
    private void initArrows() {
        ITiledTextureRegion textureRegion =
                (TiledTextureRegion) TextureRegionHolder.getRegion(StringConstants.FILE_DESCRIPTION_ARROWS);
        int y = SizeConstants.DESCRIPTION_POPUP_ARROW_Y;
        mLeftArrow = new ArrowButtonSprite(
                SizeConstants.DESCRIPTION_POPUP_LEFT_ARROW_X, y,
                textureRegion, getVertexBufferObjectManager());
        mRightArrow = new ArrowButtonSprite(
                SizeConstants.DESCRIPTION_POPUP_RIGHT_ARROW_X, y,
                textureRegion, getVertexBufferObjectManager());
        mRightArrow.setFlippedVertical(true);
        mRightArrow.setRotation(180);
        attachChild(mLeftArrow);
        attachChild(mRightArrow);
    }

    /**
     * init popup areas (basically 3 : image, description and addition information).
     * This areas will be used to pass to different {@code Drawer}'s so they know where
     * to draw existing stuff.
     */
    private void initAreas() {
        //image
        mImageShape = new Rectangle(SizeConstants.DESCRIPTION_POPUP_IMAGE_X,
                SizeConstants.DESCRIPTION_POPUP_IMAGE_Y,
                SizeConstants.DESCRIPTION_POPUP_IMAGE_WIDTH,
                SizeConstants.DESCRIPTION_POPUP_IMAGE_HEIGHT, getVertexBufferObjectManager());
        mImageShape.setColor(Color.TRANSPARENT);
        mImageShape.setTouchCallback(new TouchHelper.EntityTouchToChildren(mImageShape));
        attachChild(mImageShape);
        //addition information (right)
        mAdditionalInformationShape = new Rectangle(
                SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_AREA_X,
                SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_AREA_Y,
                SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_AREA_WIDTH,
                SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_AREA_HEIGHT,
                getVertexBufferObjectManager());
        mAdditionalInformationShape.setColor(Color.TRANSPARENT);
        mAdditionalInformationShape.setTouchCallback(
                new TouchHelper.EntityTouchToChildren(mAdditionalInformationShape));
        attachChild(mAdditionalInformationShape);
        // object description area
        mDescriptionShape = new Rectangle(SizeConstants.DESCRIPTION_POPUP_DES_AREA_X,
                SizeConstants.DESCRIPTION_POPUP_DES_AREA_Y,
                SizeConstants.DESCRIPTION_POPUP_DES_AREA_WIDTH,
                SizeConstants.DESCRIPTION_POPUP_DES_AREA_HEIGHT, getVertexBufferObjectManager());
        mDescriptionShape.setColor(Color.TRANSPARENT);
        mDescriptionShape.setTouchCallback(
                new TouchHelper.EntityTouchToChildren(mDescriptionShape));
        attachChild(mDescriptionShape);
    }

    private void initLeftBlock() {
        mLeftBlockImage = new Sprite(
                mAdditionalInformationShape.getWidth() / 2,
                mAdditionalInformationShape.getHeight() / 2,
                TextureRegionHolder.getRegion(StringConstants.FILE_DESCRIPTION_LEFT_BLOCK),
                getVertexBufferObjectManager());
        mLeftBlockImage.setWidth(SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_BACKGROUND_WIDTH);
        mLeftBlockImage.setHeight(SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_BACKGROUND_HEIGHT);
        mAdditionalInformationShape.attachChild(mLeftBlockImage);
    }

    private void initObjectNameText() {
        IFont font = FontHolder.getInstance().getElement(sDescriptionFontKey);
        mObjectNameText = new Text(SizeConstants.DESCRIPTION_POPUP_HEADER_TEXT_X,
                SizeConstants.DESCRIPTION_POPUP_HEADER_SINGLE_ROW_Y,
                font, "", 20, getVertexBufferObjectManager());
        mObjectNameText.setHorizontalAlign(HorizontalAlign.CENTER);
        attachChild(mObjectNameText);
    }

    public void updateDescription(IPopupUpdater updater, Object objectId, String allianceName, String playerName) {
        updater.updateImage(mImageShape, objectId, allianceName, playerName);
        updater.updateDescription(mDescriptionShape, objectId, allianceName, playerName);
        updater.updateAdditionInfo(mAdditionalInformationShape, objectId, allianceName, playerName);
        updater.updateHeaderButtons(mLeftArrow, mRightArrow, objectId, playerName);
        updateObjectNameText(updater, objectId, allianceName);
    }

    private void updateObjectNameText(IPopupUpdater updater, Object objectId, String allianceName) {
        updater.updateObjectNameText(mObjectNameText, objectId, allianceName);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        int padding = SizeConstants.BETWEEN_TEXTURES_PADDING;
        //background
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager,
                SizeConstants.DESCRIPTION_POPUP_WIDTH,
                SizeConstants.DESCRIPTION_POPUP_HEIGHT
                        + padding
                        + SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_AREA_FILE_HEIGHT,
                TextureOptions.BILINEAR);
        TextureRegionHolder.addElementFromAssets(
                StringConstants.FILE_DESCRIPTION_POPUP_BACKGROUND, textureAtlas, context, 0, 0);
        TextureRegionHolder.addElementFromAssets(
                StringConstants.FILE_DESCRIPTION_LEFT_BLOCK, textureAtlas, context,
                0, SizeConstants.DESCRIPTION_POPUP_HEIGHT + padding);
        TextureRegionHolder.addTiledElementFromAssets(
                StringConstants.FILE_DESCRIPTION_ARROWS, textureAtlas, context,
                SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_AREA_FILE_WIDTH + padding,
                SizeConstants.DESCRIPTION_POPUP_HEIGHT + padding, 2, 1);
        textureAtlas.load();
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        //described object name font
        final ITexture fontTexture = new BitmapTextureAtlas(textureManager, 512, 256);
        IFont font = FontFactory.createFromAsset(fontManager, fontTexture,
                EaFallApplication.getContext().getAssets(), "fonts/MyriadPro-Regular.ttf",
                SizeConstants.DESCRIPTION_POPUP_HEADER_FONT_SIZE, true,
                android.graphics.Color.argb(255, 196, 248, 255));
        font.load();
        FontHolder.getInstance().addElement(sDescriptionFontKey, font);
    }
}
