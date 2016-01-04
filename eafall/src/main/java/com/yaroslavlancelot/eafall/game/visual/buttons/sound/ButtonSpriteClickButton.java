package com.yaroslavlancelot.eafall.game.visual.buttons.sound;

import com.yaroslavlancelot.eafall.game.audio.GeneralSoundKeys;
import com.yaroslavlancelot.eafall.game.audio.SoundFactory;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Sound on a click
 *
 * @author Yaroslav Havrylovych
 */
public class ButtonSpriteClickButton extends ButtonSprite {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    protected Boolean mSound = true;

    // ===========================================================
    // Constructors
    // ===========================================================
    public ButtonSpriteClickButton(float pX, float pY,
                                   ITiledTextureRegion pTiledTextureRegion,
                                   VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    /** play click sound if true and false in other case */
    public void setSound(final Boolean sound) {
        mSound = sound;
    }


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        if (mSound && pSceneTouchEvent.isActionUp() && this.mState == State.PRESSED) {
            SoundFactory.getInstance().playSound(GeneralSoundKeys.BUTTON_CLICK);
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
