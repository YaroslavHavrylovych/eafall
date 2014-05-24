package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** contains elements which together create elements description (e.g. it's image, characteristics etc) */
public class DescriptionPopupCompositeSprite extends Sprite implements Redraw {
    public static final String TAG = DescriptionPopupCompositeSprite.class.getCanonicalName();
    private static volatile DescriptionPopupCompositeSprite sDescriptionPopupCompositeSprite;
    private GameObject mGameObject;
    private CloseButtonTiledSprite mCloseSprite;

    private DescriptionPopupCompositeSprite(VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0, 0,
                TextureRegionHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.FILE_DESCRIPTION_POPUP_BACKGROUND),
                pVertexBufferObjectManager);
        recreateArea(new Area(0, SizeConstants.GAME_FIELD_HEIGHT / 2,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT / 2));
    }

    public static DescriptionPopupCompositeSprite getInstance() {
        return sDescriptionPopupCompositeSprite;
    }

    public static DescriptionPopupCompositeSprite init(EntityOperations entityOperations) {
        sDescriptionPopupCompositeSprite = new DescriptionPopupCompositeSprite(entityOperations.getObjectManager());
        sDescriptionPopupCompositeSprite.initCross(entityOperations);
        sDescriptionPopupCompositeSprite.hide();
        return sDescriptionPopupCompositeSprite;
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        //background
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 1920, 540, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addElementFromAssets(GameStringsConstantsAndUtils.FILE_DESCRIPTION_POPUP_BACKGROUND,
                TextureRegionHolderUtils.getInstance(), smallObjectTexture, context, 0, 0);
        smallObjectTexture.load();

        CloseButtonTiledSprite.loadResources(context, textureManager);
    }

    public void recreateArea(Area area) {
        setPosition(area.left, area.top);
        setWidth(area.width);
        setHeight(area.height);
    }

    @Override
    public void redraw() {
    }

    public void show(GameObject gameObject) {
        setVisible(true);
        setIgnoreUpdate(false);
    }

    public void hide() {
        setVisible(false);
        setIgnoreUpdate(true);
    }

    private void initCross(EntityOperations entityOperations) {
        mCloseSprite = new CloseButtonTiledSprite(entityOperations.getObjectManager());
        mCloseSprite.setPosition(SizeConstants.GAME_FIELD_WIDTH - SizeConstants.DESCRIPTION_POPUP_TOP_BORDER_HEIGHT,
                (SizeConstants.DESCRIPTION_POPUP_TOP_BORDER_HEIGHT - SizeConstants.DESCRIPTION_POPUP_CROSS_SIZE) / 2);
        mCloseSprite.setWidth(SizeConstants.DESCRIPTION_POPUP_TOP_BORDER_HEIGHT);
        mCloseSprite.setHeight(SizeConstants.DESCRIPTION_POPUP_TOP_BORDER_HEIGHT);
        mCloseSprite.setOnTouchListener(new TouchUtils.CustomTouchListener(new Area(mCloseSprite.getX(), mCloseSprite.getY(), mCloseSprite.getWidth(), mCloseSprite.getHeight())) {
            @Override
            public void click() { hide(); }

            @Override
            public void unPress() { mCloseSprite.unpress(); }

            @Override
            public void press() { mCloseSprite.press(); }
        });
        entityOperations.registerHudTouch(mCloseSprite);
        attachChild(mCloseSprite);
    }
}
