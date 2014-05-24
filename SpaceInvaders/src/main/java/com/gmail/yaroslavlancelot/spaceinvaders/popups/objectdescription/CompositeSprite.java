package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** contains elements which together create elements description (e.g. it's image, characteristics etc) */
public class CompositeSprite extends Sprite implements Redraw {
    private static volatile CompositeSprite sCompositeSprite;
    private GameObject mGameObject;

    public CompositeSprite(VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0, 0,
                TextureRegionHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.FILE_DESCRIPTION_POPUP_BACKGROUND),
                pVertexBufferObjectManager);
        recreateArea(new Area(0, SizeConstants.GAME_FIELD_HEIGHT / 2,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT / 2));
        hide();
    }

    public static CompositeSprite getInstance() {
        return sCompositeSprite;
    }

    public static CompositeSprite init(VertexBufferObjectManager pVertexBufferObjectManager) {
        return sCompositeSprite = new CompositeSprite(pVertexBufferObjectManager);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 1920, 540, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addElementFromAssets(
                GameStringsConstantsAndUtils.FILE_DESCRIPTION_POPUP_BACKGROUND,
                TextureRegionHolderUtils.getInstance(), smallObjectTexture, context, 0, 0);
        smallObjectTexture.load();
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
}
