package com.yaroslavlancelot.eafall.game.scene.hud;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Custom HUD used in the game (to unify the work).
 * Custom setAlpha which  passed to children.
 *
 * @author Yaroslav Havrylovych
 */
public class BaseGameHud extends HUD {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public BaseGameHud() {
        setTouchAreaBindingOnActionDownEnabled(true);
        setOnAreaTouchTraversalFrontToBack();
        setAlpha(EaFallApplication.getConfig().getHudAlpha());
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void setAlpha(final float pAlpha) {
        super.setAlpha(pAlpha);
        if (mChildren != null) {
            for (IEntity child : mChildren) {
                child.setAlpha(pAlpha);
            }
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================
    public void initHudElements(Camera camera, VertexBufferObjectManager vertexManager,
                                MissionConfig missionConfig) {
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
