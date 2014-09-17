package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;

import org.andengine.entity.shape.ITouchCallback;

import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.DescriptionUpdater;
import org.andengine.entity.shape.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.FontManager;
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
    /** will hide popup from the screen */
    private CloseTouchableTiledSprite mCloseSprite;

    // next three guys/field are just split popup on display areas

    /** descript object image */
    private RectangularShape mImageShape;
    /** descript object description area */
    private RectangularShape mDescriptionShape;
    /** descript object addition information field */
    private RectangularShape mAdditionalInformationShape;

    //

    DescriptionPopupBackgroundSprite(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(0, 0,
                TextureRegionHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.FILE_DESCRIPTION_POPUP_BACKGROUND),
                vertexBufferObjectManager);
        recreateArea(new Area(0, SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.DESCRIPTION_POPUP_HEIGHT,
                SizeConstants.DESCRIPTION_POPUP_WIDTH, SizeConstants.DESCRIPTION_POPUP_HEIGHT));
        scene.attachChild(this);
        scene.registerTouchArea(this);

        initCross(scene);
        initAreas();

        hide();
    }

    private void recreateArea(Area area) {
        setPosition(area.left, area.top);
        setWidth(area.width);
        setHeight(area.height);
    }

    /** popup closing button */
    private void initCross(Scene scene) {
        mCloseSprite = new CloseTouchableTiledSprite(getVertexBufferObjectManager(),
                SizeConstants.DESCRIPTION_POPUP_CROSS_SIZE);
        mCloseSprite.setPosition(SizeConstants.DESCRIPTION_POPUP_WIDTH - SizeConstants.DESCRIPTION_POPUP_CROSS_SIZE
                        - SizeConstants.DESCRIPTION_POPUP_CROSS_PADDING,
                SizeConstants.DESCRIPTION_POPUP_CROSS_PADDING);
        mCloseSprite.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                hide();
            }
        });

        scene.registerTouchArea(mCloseSprite);
        attachChild(mCloseSprite);
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
        // object description area
        int height = SizeConstants.DESCRIPTION_POPUP_HEIGHT - 2 * padding;
        float width = SizeConstants.DESCRIPTION_POPUP_WIDTH - mAdditionalInformationShape.getWidth()
                - mImageShape.getWidth() - 4 * padding;
        mDescriptionShape = new Rectangle(mImageShape.getX() + mImageShape.getWidth() + padding, padding,
                width, height, getVertexBufferObjectManager());
        mDescriptionShape.setAlpha(0);
        attachChild(mDescriptionShape);
    }

    /** hide sprite/popup with inner elements */
    void hide() {
        setVisible(false);
        setIgnoreUpdate(true);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        //background
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 1920, 540, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addElementFromAssets(GameStringsConstantsAndUtils.FILE_DESCRIPTION_POPUP_BACKGROUND,
                TextureRegionHolderUtils.getInstance(), smallObjectTexture, context, 0, 0);
        smallObjectTexture.load();

        CloseTouchableTiledSprite.loadResources(context, textureManager);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
    }

    /** show sprite/popup */
    void show() {
        setVisible(true);
        setIgnoreUpdate(false);
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float touchAreaLocalX, float touchAreaLocalY) {
        return isVisible() && super.onAreaTouched(pSceneTouchEvent, touchAreaLocalX, touchAreaLocalY);
    }

    public void updateDescription(DescriptionUpdater updater, int objectId, String raceName, String teamName) {
        updater.updateImage(mImageShape, objectId, raceName, teamName);
        updater.updateDescription(mDescriptionShape, objectId, raceName, teamName);
        updater.updateAdditionInfo(mAdditionalInformationShape, objectId, raceName, teamName);
    }
}
