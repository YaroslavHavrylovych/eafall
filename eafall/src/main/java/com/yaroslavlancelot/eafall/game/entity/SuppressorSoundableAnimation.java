package com.yaroslavlancelot.eafall.game.entity;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Suppressor animation sprite and sound
 *
 * @author Yaroslav Havrylovych
 */
public class SuppressorSoundableAnimation extends AnimatedSprite {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final String IMAGE = "graphics/sprites/suppressor.png";
    public static final String SOUND = "audio/sound/boom/suppressor.ogg";

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public SuppressorSoundableAnimation(float pX, float pY,
                                        VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, (ITiledTextureRegion) TextureRegionHolder.getRegion(IMAGE),
                pVertexBufferObjectManager);
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
    public void startAnimation(boolean rtl) {
        if (rtl) {
            setFlippedHorizontal(true);
        }
        int time = 65, loops = 5, frames = 4;
        float images = frames * loops;
        final float alphaDelta = 0.7f / images;
        final float widthDelta = (SizeConstants.HALF_FIELD_WIDTH
                + SizeConstants.SUN_DIAMETER / 2
                - getWidth() / 2
                - SizeConstants.ADDITION_MARGIN_FOR_PLANET) * 1.f / images;
        final float heightDelta = (SizeConstants.GAME_FIELD_HEIGHT - getHeight()) * 1.f / images;
        setAlpha(1f);
        animate(time, loops, new IAnimationListener() {
            float mAlpha = 1f;

            @Override
            public void onAnimationStarted(final AnimatedSprite pAnimatedSprite, final int pInitialLoopCount) {
                SoundFactory.getInstance().playSound(SOUND);
            }

            @Override
            public void onAnimationFrameChanged(final AnimatedSprite pAnimatedSprite, final int pOldFrameIndex, final int pNewFrameIndex) {
                mAlpha = mAlpha - alphaDelta;
                mAlpha = mAlpha > 0 ? mAlpha : 0;
                setAlpha(mAlpha);
                setWidth(mWidth + widthDelta);
                setHeight(mHeight + heightDelta);
            }

            @Override
            public void onAnimationLoopFinished(final AnimatedSprite pAnimatedSprite, final int pRemainingLoopCount, final int pInitialLoopCount) {
            }

            @Override
            public void onAnimationFinished(final AnimatedSprite pAnimatedSprite) {
                SuppressorSoundableAnimation.this.detachSelf();
            }
        });
    }

    public static void loadResources(TextureManager textureManager) {
        int size = 2 * SizeConstants.PLANET_DIAMETER;
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager,
                size, size, TextureOptions.BILINEAR);
        TextureRegionHolder.addTiledElementFromAssets(IMAGE, textureAtlas,
                EaFallApplication.getContext(), 0, 0, 2, 2);
        textureAtlas.load();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
