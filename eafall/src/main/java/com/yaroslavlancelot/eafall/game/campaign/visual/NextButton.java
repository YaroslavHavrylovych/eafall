package com.yaroslavlancelot.eafall.game.campaign.visual;

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
 * Ease to use and load next button
 *
 * @author Yaroslav Havrylovych
 */
public class NextButton extends ButtonSpriteClickButton {

    public NextButton(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, boolean toTheRight) {
        super(pX, pY, (ITiledTextureRegion)
                        TextureRegionHolder.getRegion(StringConstants.FILE_CAMPAIGN_NEXT_BUTTON),
                pVertexBufferObjectManager);
        if (toTheRight) {
            setRotation(180);
        } else {
            setRotation(0);
        }
    }

    public static void loadImages(TextureManager textureManager) {
        Context context = EaFallApplication.getContext();
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager,
                3 * SizeConstants.CAMPAIGN_NEXT_BUTTON_WIDTH,
                SizeConstants.CAMPAIGN_NEXT_BUTTON_HEIGHT,
                TextureOptions.BILINEAR);
        TextureRegionHolder.addTiledElementFromAssets(StringConstants.FILE_CAMPAIGN_NEXT_BUTTON, textureAtlas,
                context, 0, 0, 3, 1);
        textureAtlas.load();
    }
}
