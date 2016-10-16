package com.yaroslavlancelot.eafall.game.visual.buttons;

import android.content.Context;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.visual.buttons.sound.ButtonSpriteClickButton;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Ease to use and load back button
 *
 * @author Yaroslav Havrylovych
 */
public class BackButton extends ButtonSpriteClickButton {
    public BackButton(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, (ITiledTextureRegion)
                        TextureRegionHolder.getRegion(StringConstants.FILE_CAMPAIGN_BACK_BUTTON),
                pVertexBufferObjectManager);
    }

    public static void loadImages(TextureManager textureManager) {
        Context context = EaFallApplication.getContext();
        //background
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager,
                2 * SizeConstants.CAMPAIGN_BACK_BUTTON_SIZE,
                SizeConstants.CAMPAIGN_BACK_BUTTON_SIZE,
                TextureOptions.BILINEAR);
        TextureRegionHolder.addTiledElementFromAssets(StringConstants.FILE_CAMPAIGN_BACK_BUTTON, textureAtlas,
                context, 0, 0, 2, 1);
        textureAtlas.load();
    }
}
