package com.gmail.yaroslavlancelot.eafall.game.visual.buttons;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.constant.StringsAndPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

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
        TextureRegionHolder.addTiledElementFromAssets(
                StringsAndPath.FILE_CIRCLE_POINT, smallObjectTexture, context, 0, 0, 2, 1);
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
