package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** cross which used as close button in {@code DescriptionPopup} */
public class CloseButtonSprite extends ButtonSprite {
    public CloseButtonSprite(VertexBufferObjectManager vertexBufferObjectManager, float size) {
        super(0f, 0f,
                (ITiledTextureRegion) TextureRegionHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.FILE_DESCRIPTION_POPUP_CROSS),
                vertexBufferObjectManager);
        setWidth(size);
        setHeight(size);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 50, 25, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addTiledElementFromAssets(GameStringsConstantsAndUtils.FILE_DESCRIPTION_POPUP_CROSS,
                TextureRegionHolderUtils.getInstance(), smallObjectTexture, context, 0, 0, 2, 1);
        smallObjectTexture.load();
    }
}