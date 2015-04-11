package com.gmail.yaroslavlancelot.eafall.game.popup.description;

import android.content.Context;
import android.graphics.Typeface;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.IPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.Shape;
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
import org.andengine.util.adt.color.Color;

/**
 * Background sprite for description popup.
 * Contains all operation to draw this popup.
 * Used by handler {@code DescriptionPopup} to step-by-step display popup on screen.
 * Generally this sprite with inner elements IS popup but handler contains logic
 * to redraw it and display correctly.
 */
public class DescriptionPopupBackground extends Sprite {
    private static final String TAG = DescriptionPopupBackground.class.getCanonicalName();
    /** font key */
    private static final String sDescriptionFontKey = "key_objects_description_object_name_text_font";
    /** described object name */
    protected Text mObjectNameText;

    // next three guys/field are just split popup on display areas
    /** descript object image */
    private Shape mImageShape;
    /** descript object description area */
    private Shape mDescriptionShape;
    /** descript object addition information field */
    private Shape mAdditionalInformationShape;

    //
    DescriptionPopupBackground(float width, float height, VertexBufferObjectManager vertexBufferObjectManager) {
        super(width / 2, height / 2, width, height,
                TextureRegionHolder.getInstance().getElement(StringConstants.FILE_DESCRIPTION_POPUP_BACKGROUND),
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
        mImageShape = new Rectangle(padding + size / 2, padding + size / 2, size, size, getVertexBufferObjectManager());
        mImageShape.setColor(Color.TRANSPARENT);
        attachChild(mImageShape);
        // addition information area
        int paddingX = SizeConstants.DESCRIPTION_POPUP_WIDTH
                - SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_AREA_WIDTH / 2
                - SizeConstants.DESCRIPTION_POPUP_PADDING;
        int paddingY = SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_AREA_HEIGHT / 2 + SizeConstants.DESCRIPTION_POPUP_PADDING;
        mAdditionalInformationShape = new Rectangle(paddingX, paddingY,
                SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_AREA_WIDTH,
                SizeConstants.DESCRIPTION_POPUP_ADDITIONAL_AREA_HEIGHT, getVertexBufferObjectManager());
        mAdditionalInformationShape.setColor(Color.TRANSPARENT);
        attachChild(mAdditionalInformationShape);
        // init described object name text
        initObjectNameText(padding);
        // object description area
        int height = SizeConstants.DESCRIPTION_POPUP_DESCRIPTION_AREA_HEIGHT;
        float width = SizeConstants.DESCRIPTION_POPUP_WIDTH - mAdditionalInformationShape.getWidth()
                - mImageShape.getWidth() - 4 * padding;
        mDescriptionShape = new Rectangle(mImageShape.getWidth() + 2 * padding + width / 2,
                padding + height / 2,
                width, height, getVertexBufferObjectManager());
        mDescriptionShape.setColor(Color.TRANSPARENT);
        attachChild(mDescriptionShape);
    }

    private void initObjectNameText(int padding) {
        IFont font = FontHolder.getInstance().getElement(sDescriptionFontKey);
        mObjectNameText = new Text(0, SizeConstants.DESCRIPTION_POPUP_HEIGHT - padding,
                font, "", 20, getVertexBufferObjectManager());
        attachChild(mObjectNameText);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        //background
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 1920, 540, TextureOptions.BILINEAR);
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_DESCRIPTION_POPUP_BACKGROUND, smallObjectTexture, context, 0, 0);
        smallObjectTexture.load();
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        //described object name font
        IFont font = FontFactory.create(fontManager, textureManager, 512, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
                SizeConstants.DESCRIPTION_POPUP_TITLE_SIZE, Color.BLUE.getABGRPackedInt());
        font.load();
        FontHolder.getInstance().addElement(sDescriptionFontKey, font);
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
        mObjectNameText.setX(mDescriptionShape.getX());
    }
}
