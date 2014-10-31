package com.gmail.yaroslavlancelot.spaceinvaders.visualelements.buttons;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** used to mark the unit possible path */
public class CirclePointButton extends TiledSprite {
    public CirclePointButton(float x, float y, ITiledTextureRegion tiledTextureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, tiledTextureRegion, vertexBufferObjectManager);
        setCurrentTileIndex(0);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 128, 64, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addTiledElementFromAssets(GameStringsConstantsAndUtils.FILE_CIRCLE_POINT,
                TextureRegionHolderUtils.getInstance(), smallObjectTexture, context, 0, 0, 2, 1);
        smallObjectTexture.load();
    }

    public void setActive() {
        setCurrentTileIndex(1);
    }

    public void setDeactivated() {
        setCurrentTileIndex(0);
    }

    public boolean isActive() {
        return getCurrentTileIndex() == 1;
    }
}
