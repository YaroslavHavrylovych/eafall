package com.yaroslavlancelot.eafall.game.visual.buttons.sound;

import com.yaroslavlancelot.eafall.game.audio.GeneralSoundKeys;
import com.yaroslavlancelot.eafall.game.audio.SoundFactory;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Sound on a click
 *
 * @author Yaroslav Havrylovych
 */
public class ButtonSpriteSelectButton extends ButtonSprite {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public ButtonSpriteSelectButton(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pNormalTextureRegion, pVertexBufferObjectManager);
    }

    public ButtonSpriteSelectButton(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnClickListener pOnClickListener) {
        super(pX, pY, pNormalTextureRegion, pVertexBufferObjectManager, pOnClickListener);
    }

    public ButtonSpriteSelectButton(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pNormalTextureRegion, pPressedTextureRegion, pVertexBufferObjectManager);
    }

    public ButtonSpriteSelectButton(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnClickListener pOnClickListener) {
        super(pX, pY, pNormalTextureRegion, pPressedTextureRegion, pVertexBufferObjectManager, pOnClickListener);
    }

    public ButtonSpriteSelectButton(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final ITextureRegion pDisabledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pNormalTextureRegion, pPressedTextureRegion, pDisabledTextureRegion, pVertexBufferObjectManager);
    }

    public ButtonSpriteSelectButton(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final ITextureRegion pDisabledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnClickListener pOnClickListener) {
        super(pX, pY, pNormalTextureRegion, pPressedTextureRegion, pDisabledTextureRegion, pVertexBufferObjectManager, pOnClickListener);
    }

    public ButtonSpriteSelectButton(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public ButtonSpriteSelectButton(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnClickListener pOnClickListener) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, pOnClickListener);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        if (pSceneTouchEvent.isActionUp() && this.mState == State.PRESSED) {
            SoundFactory.getInstance().playSound(GeneralSoundKeys.SELECT);
        }
        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
    }


    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
