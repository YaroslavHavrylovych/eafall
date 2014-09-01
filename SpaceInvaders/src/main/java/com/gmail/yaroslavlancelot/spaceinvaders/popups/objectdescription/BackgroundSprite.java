package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ITouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.DescriptionUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
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
public class BackgroundSprite extends Sprite implements ITouchListener {
    private static final String TAG = BackgroundSprite.class.getCanonicalName();
    /** will hide popup from the screen */
    private CloseButtonTiledSprite mCloseSprite;

    // next three guys/field are just split popup on display areas

    /** descript object image */
    private RectangularShape mImageShape;
    /** descript object description area */
    private RectangularShape mDescriptionShape;
    /** descript object addition information field */
    private RectangularShape mAdditionalInformationShape;

    //

    BackgroundSprite(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(0, 0,
                TextureRegionHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.FILE_DESCRIPTION_POPUP_BACKGROUND),
                vertexBufferObjectManager);
        recreateArea(new Area(0, SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.DESCRIPTION_POPUP_HEIGHT,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.DESCRIPTION_POPUP_HEIGHT));
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
        mCloseSprite = new CloseButtonTiledSprite(getVertexBufferObjectManager(),
                SizeConstants.DESCRIPTION_POPUP_CROSS_SIZE);
        mCloseSprite.setPosition(SizeConstants.GAME_FIELD_WIDTH - SizeConstants.DESCRIPTION_POPUP_CROSS_SIZE
                        - SizeConstants.DESCRIPTION_POPUP_CROSS_PADDING,
                SizeConstants.DESCRIPTION_POPUP_CROSS_PADDING);
        mCloseSprite.setOnTouchListener(new TouchUtils.CustomTouchListener(new Area(getX() + mCloseSprite.getX(),
                getY() + mCloseSprite.getY(), mCloseSprite.getWidth(), mCloseSprite.getHeight())) {
            @Override
            public void press() {
                mCloseSprite.press();
            }

            @Override
            public void click() {
                unPress();
                hide();
            }

            @Override
            public void unPress() {
                mCloseSprite.unpress();
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
        int padding = SizeConstants.DESCRIPTION_POPUP_OBJECT_IMAGE_PADDING;
        int size = SizeConstants.DESCRIPTION_POPUP_HEIGHT - 2 * padding;
        mImageShape = new Rectangle(padding, padding, size, size, getVertexBufferObjectManager());
        mImageShape.setAlpha(0);
        attachChild(mImageShape);
        // object description area
        padding = SizeConstants.DESCRIPTION_POPUP_OBJECT_IMAGE_PADDING;
        int height = SizeConstants.DESCRIPTION_POPUP_HEIGHT - 2 * padding;
        int width = 400;
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

        CloseButtonTiledSprite.loadResources(context, textureManager);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
    }

    /** show sprite/popup */
    void show() {
        setVisible(true);
        setIgnoreUpdate(false);
    }

    @Override
    public boolean onTouch(TouchEvent pSceneTouchEvent) {
        return isVisible() && contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
    }

    public void initDescription(DescriptionUpdater updater) {
        updater.initDescriptionArea(getX() + mDescriptionShape.getX(), getY() + mDescriptionShape.getY());
    }

    public void updateDescription(DescriptionUpdater updater, int objectId, String raceName, String teamName) {
        updater.updateImage(mImageShape, objectId, raceName, teamName);
        updater.updateDescription(mDescriptionShape, objectId, raceName, teamName);
        updater.updateAdditionInfo(mAdditionalInformationShape, objectId, raceName, teamName);
    }
}
