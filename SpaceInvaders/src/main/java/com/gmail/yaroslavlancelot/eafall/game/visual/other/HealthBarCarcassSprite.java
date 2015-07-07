package com.gmail.yaroslavlancelot.eafall.game.visual.other;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * @author Yaroslav Havrylovych
 */
public class HealthBarCarcassSprite extends Sprite {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public HealthBarCarcassSprite(final VertexBufferObjectManager vertexBufferObjectManager) {
        super(SizeConstants.GAME_FIELD_WIDTH / 2,
                SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.HEALTH_BAR_CARCASS_HEIGHT / 2,
                TextureRegionHolder.getInstance()
                        .getElement(StringConstants.FILE_HEALTH_BAR_CARCASS),
                vertexBufferObjectManager);
        setWidth(SizeConstants.HEALTH_BAR_CARCASS_WIDTH);
        setHeight(SizeConstants.HEALTH_BAR_CARCASS_HEIGHT);
        setAlpha(.85f);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    public static void loadResources(final TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager,
                SizeConstants.HEALTH_BAR_CARCASS_WIDTH, SizeConstants.HEALTH_BAR_CARCASS_HEIGHT,
                TextureOptions.BILINEAR);
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_HEALTH_BAR_CARCASS,
                smallObjectTexture, EaFallApplication.getContext(), 0, 0);
        smallObjectTexture.load();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
