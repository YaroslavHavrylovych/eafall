package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.ShowBuildingDescriptionEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch.ITouchListener;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/**
 * Contains elements which together create elements description (e.g. it's image, characteristics etc)
 * You can see it in the bottom of the screen after you tap on unit or building to build.
 * Contains element image and characteristics.
 */
public class DescriptionPopupCompositeSprite extends Sprite implements Redraw, ITouchListener {
    public static final String TAG = DescriptionPopupCompositeSprite.class.getCanonicalName();
    private static volatile DescriptionPopupCompositeSprite sDescriptionPopupCompositeSprite;
    private GameObject mGameObject;
    private CloseButtonTiledSprite mCloseSprite;
    private Scene mScene;

    private DescriptionPopupCompositeSprite(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(0, 0,
                TextureRegionHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.FILE_DESCRIPTION_POPUP_BACKGROUND),
                vertexBufferObjectManager);
        recreateArea(new Area(0, SizeConstants.GAME_FIELD_HEIGHT / 2,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT / 2));
        mScene = scene;

        mScene.attachChild(this);
        mScene.registerTouchArea(this);

        hide();

        EventBus.getDefault().register(this);
    }

    public void recreateArea(Area area) {
        setPosition(area.left, area.top);
        setWidth(area.width);
        setHeight(area.height);
    }

    public void hide() {
        setVisible(false);
        setIgnoreUpdate(true);
    }

    public static synchronized DescriptionPopupCompositeSprite getInstance() {
        return sDescriptionPopupCompositeSprite;
    }

    public static synchronized DescriptionPopupCompositeSprite init(VertexBufferObjectManager objectManager, Scene scene) {
        sDescriptionPopupCompositeSprite =
                new DescriptionPopupCompositeSprite(objectManager, scene);

        sDescriptionPopupCompositeSprite.initCross();
        return sDescriptionPopupCompositeSprite;
    }

    private void initCross() {
        mCloseSprite = new CloseButtonTiledSprite(getVertexBufferObjectManager());
        mCloseSprite.setPosition(SizeConstants.GAME_FIELD_WIDTH - SizeConstants.DESCRIPTION_POPUP_TOP_BORDER_SIZE * 4,
                (SizeConstants.DESCRIPTION_POPUP_TOP_BORDER_SIZE - SizeConstants.DESCRIPTION_POPUP_CROSS_SIZE) / 2);
        mCloseSprite.setWidth(SizeConstants.DESCRIPTION_POPUP_TOP_BORDER_SIZE * 4);
        mCloseSprite.setHeight(SizeConstants.DESCRIPTION_POPUP_TOP_BORDER_SIZE * 4);
        mCloseSprite.setOnTouchListener(new TouchUtils.CustomTouchListener(new Area(getX() + mCloseSprite.getX(),
                getY() + mCloseSprite.getY(), mCloseSprite.getWidth(), mCloseSprite.getHeight())) {
            @Override
            public void click() {
                unPress();
                hide();
            }

            @Override
            public void unPress() {
                mCloseSprite.unpress();
            }

            @Override
            public void press() {
                mCloseSprite.press();
            }
        });

        mScene.registerTouchArea(mCloseSprite);
        attachChild(mCloseSprite);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        //background
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 1920, 540, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addElementFromAssets(GameStringsConstantsAndUtils.FILE_DESCRIPTION_POPUP_BACKGROUND,
                TextureRegionHolderUtils.getInstance(), smallObjectTexture, context, 0, 0);
        smallObjectTexture.load();

        CloseButtonTiledSprite.loadResources(context, textureManager);
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final ShowBuildingDescriptionEvent showBuildingDescriptionEvent) {
        show(null);
    }

    public void show(GameObject gameObject) {
        setVisible(true);
        setIgnoreUpdate(false);
    }

    @Override
    public void redraw() {
    }

    @Override
    public boolean onTouch(TouchEvent pSceneTouchEvent) {
        return isVisible() && contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
    }
}
