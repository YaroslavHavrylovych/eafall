package com.gmail.yaroslavlancelot.spaceinvaders.popups.buildings;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** trigger buildings popup (already initialized) */
public class ShowBuildingsPopupButtonSprite extends ButtonSprite {
    public ShowBuildingsPopupButtonSprite(VertexBufferObjectManager vertexBufferObjectManager) {
        super(SizeConstants.GAME_FIELD_WIDTH / 2 - SizeConstants.BUILDING_POPUP_INVOCATION_BUTTON_SIZE / 2,
                SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.BUILDING_POPUP_INVOCATION_BUTTON_SIZE,
                (ITiledTextureRegion) TextureRegionHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.FILE_BUILDINGS_POPUP_UP_BUTTON),
                vertexBufferObjectManager);
        int size = SizeConstants.BUILDING_POPUP_INVOCATION_BUTTON_SIZE;
        setWidth(size);
        setHeight(size);
        setAlpha(.6f);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        int size = SizeConstants.BUILDING_POPUP_INVOCATION_BUTTON_SIZE;
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 2 * size, size, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addTiledElementFromAssets(GameStringsConstantsAndUtils.FILE_BUILDINGS_POPUP_UP_BUTTON,
                TextureRegionHolderUtils.getInstance(), smallObjectTexture, context, 0, 0, 2, 1);
        smallObjectTexture.load();
    }
}
