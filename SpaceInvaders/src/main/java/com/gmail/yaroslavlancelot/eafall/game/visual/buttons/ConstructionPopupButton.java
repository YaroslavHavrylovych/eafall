package com.gmail.yaroslavlancelot.eafall.game.visual.buttons;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** trigger buildings popup (already initialized) */
public class ConstructionPopupButton extends ButtonSprite {
    public ConstructionPopupButton(VertexBufferObjectManager vertexBufferObjectManager) {
        super(SizeConstants.GAME_FIELD_WIDTH / 2,
                SizeConstants.CONSTRUCTIONS_POPUP_INVOCATION_BUTTON_HEIGHT / 2,
                (ITiledTextureRegion) TextureRegionHolder.getInstance()
                        .getElement(StringConstants.FILE_BUILDINGS_POPUP_UP_BUTTON),
                vertexBufferObjectManager);
        setWidth(SizeConstants.CONSTRUCTIONS_POPUP_INVOCATION_BUTTON_WIDTH);
        setHeight(SizeConstants.CONSTRUCTIONS_POPUP_INVOCATION_BUTTON_HEIGHT);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager,
                2 * SizeConstants.CONSTRUCTIONS_POPUP_INVOCATION_BUTTON_WIDTH,
                SizeConstants.CONSTRUCTIONS_POPUP_INVOCATION_BUTTON_HEIGHT,
                TextureOptions.BILINEAR);
        TextureRegionHolder.addTiledElementFromAssets(
                StringConstants.FILE_BUILDINGS_POPUP_UP_BUTTON, smallObjectTexture, context, 0, 0, 2, 1);
        smallObjectTexture.load();
    }
}
