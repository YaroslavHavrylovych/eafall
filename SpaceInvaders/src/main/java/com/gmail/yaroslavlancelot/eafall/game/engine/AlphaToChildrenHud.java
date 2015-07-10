package com.gmail.yaroslavlancelot.eafall.game.engine;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;

/**
 * Has alpha for children.
 *
 * @author Yaroslav Havrylovych
 */
public class AlphaToChildrenHud extends HUD {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void setAlpha(final float pAlpha) {
        super.setAlpha(pAlpha);
        if(mChildren != null) {
            for (IEntity child : mChildren) {
                child.setAlpha(pAlpha);
            }
        }
    }

    @Override
    public void attachChild(final IEntity pEntity) throws IllegalStateException {
        super.attachChild(pEntity);
        pEntity.setAlpha(getAlpha());
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
