package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.IPopupUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Background sprite for description popup.
 * Contains all operation to draw this popup.
 * Used by handler {@code DescriptionPopup} to step-by-step display popup on screen.
 * Generally this sprite with inner elements IS popup but handler contains logic
 * to redraw it and display correctly.
 */
public class DescriptionPopupBackgroundSprite extends Sprite {
    private static final String TAG = DescriptionPopupBackgroundSprite.class.getCanonicalName();
    /** font key */
    private static final String sDescriptionFontKey = "key_objects_description_object_name_text_font";
    /** described object name */
    protected Text mObjectNameText;

    // next three guys/field are just split popup on display areas
    /** descript object image */
    private RectangularShape mImageShape;
    /** descript object description area */
    private RectangularShape mDescriptionShape;
    /** descript object addition information field */
    private RectangularShape mAdditionalInformationShape;

    //

    DescriptionPopupBackgroundSprite(float width, float height, VertexBufferObjectManager vertexBufferObjectManager) {
        super(0, 0, width, height,
                TextureRegionHolderUtils.getInstance().getElement(StringsAndPathUtils.FILE_DESCRIPTION_POPUP_BACKGROUND),
                vertexBufferObjectManager);

        initAreas();
    }

    /**
     * init popup areas (basically 3 : image, description and addition information).
     * This areas will be used to pass to different {@code Drawer}'s so they know where
     * to draw existing stuff.
     */
    private void initAreas() {
        // descript image area
        int padding = SizeConstants.DESCRIPTION_POPUP_PADDING;
        int size = SizeConstants.DESCRIPTION_POPUP_HEIGHT - 2 * padding;
        mImageShape = new Rectangle(padding, padding, size, size, getVertexBufferObjectManager());
        mImageShape.setAlpha(0);
        attachChild(mImageShape);
        // addition information area
        int paddingX = SizeConstants.DESCRIPTION_POPUP_WIDTH
                - SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_AREA_WIDTH
                - SizeConstants.DESCRIPTION_POPUP_PADDING;
        int paddingY = SizeConstants.DESCRIPTION_POPUP_HEIGHT
                - SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_AREA_HEIGHT
                - SizeConstants.DESCRIPTION_POPUP_PADDING;
        mAdditionalInformationShape = new Rectangle(paddingX, paddingY,
                SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_AREA_WIDTH,
                SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_AREA_HEIGHT, getVertexBufferObjectManager());
        mAdditionalInformationShape.setAlpha(0);
        attachChild(mAdditionalInformationShape);
        // init described object name text
        initObjectNameText(padding);
        // object description area
        int objectNameTextHeight = Math.round(mObjectNameText.getHeight() + 1);
        int height = SizeConstants.DESCRIPTION_POPUP_HEIGHT - 2 * padding - objectNameTextHeight;
        float width = SizeConstants.DESCRIPTION_POPUP_WIDTH - mAdditionalInformationShape.getWidth()
                - mImageShape.getWidth() - 4 * padding;
        mDescriptionShape = new Rectangle(mImageShape.getX() + mImageShape.getWidth() + padding,
                padding + objectNameTextHeight,
                width, height, getVertexBufferObjectManager());
        mDescriptionShape.setAlpha(0);
        attachChild(mDescriptionShape);
    }

    private void initObjectNameText(int padding) {
        mObjectNameText = new Text(0, padding, FontHolderUtils.getInstance().getElement(sDescriptionFontKey),
                "", 20, getVertexBufferObjectManager());
        attachChild(mObjectNameText);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        //background
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 1920, 540, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addElementFromAssets(StringsAndPathUtils.FILE_DESCRIPTION_POPUP_BACKGROUND, smallObjectTexture, context, 0, 0);
        smallObjectTexture.load();

        CloseButtonSprite.loadResources(context, textureManager);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        //described object name font
        IFont font = FontFactory.create(fontManager, textureManager, 512, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
                70f, Color.BLUE);
        font.load();
        FontHolderUtils.getInstance().addElement(sDescriptionFontKey, font);
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float touchAreaLocalX, float touchAreaLocalY) {
        return isVisible() && super.onAreaTouched(pSceneTouchEvent, touchAreaLocalX, touchAreaLocalY);
    }

    public void updateDescription(IPopupUpdater updater, Object objectId, String raceName, String teamName) {
        updater.updateImage(mImageShape, objectId, raceName, teamName);
        updater.updateDescription(mDescriptionShape, objectId, raceName, teamName);
        updater.updateAdditionInfo(mAdditionalInformationShape, objectId, raceName, teamName);
        updateObjectNameText(updater, objectId, raceName);
    }

    private void updateObjectNameText(IPopupUpdater updater, Object objectId, String raceName) {
        updater.updateObjectNameText(mObjectNameText, objectId, raceName);
        mObjectNameText.setX(mDescriptionShape.getX() + mDescriptionShape.getWidth() / 2
                - mObjectNameText.getWidth() / 2);
    }
}
