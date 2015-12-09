package com.gmail.yaroslavlancelot.eafall.game.scene.hud;

import com.gmail.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Custom HUD used in the game (to unify the work)
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

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

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
