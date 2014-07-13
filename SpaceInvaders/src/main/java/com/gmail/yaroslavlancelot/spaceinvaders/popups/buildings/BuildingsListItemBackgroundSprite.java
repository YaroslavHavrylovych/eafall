package com.gmail.yaroslavlancelot.spaceinvaders.popups.buildings;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.buttons.ButtonTiledSprite;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** represent popup item background */
public class BuildingsListItemBackgroundSprite extends ButtonTiledSprite {
    public BuildingsListItemBackgroundSprite(VertexBufferObjectManager vertexBufferObjectManager) {
        super(0f, 0f, (ITiledTextureRegion) TextureRegionHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.FILE_POPUP_BACKGROUND_ITEM),
                vertexBufferObjectManager);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 1200, 100, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addTiledElementFromAssets(GameStringsConstantsAndUtils.FILE_POPUP_BACKGROUND_ITEM,
                TextureRegionHolderUtils.getInstance(), smallObjectTexture, context, 0, 0, 2, 1);
        smallObjectTexture.load();
    }
}
