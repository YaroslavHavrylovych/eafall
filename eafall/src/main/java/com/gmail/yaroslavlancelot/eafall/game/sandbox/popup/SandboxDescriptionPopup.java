package com.gmail.yaroslavlancelot.eafall.game.sandbox.popup;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.BuildingLessDescriptionPopup;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.unit.BaseUnitPopupUpdater;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Custom sand-box description popup. used only for units description.
 *
 * @author Yaroslav Havrylovych
 */
public class SandboxDescriptionPopup extends BuildingLessDescriptionPopup {

    // ===========================================================
    // Constants
    // ===========================================================
    public static String SANDBOX_UNIT_CHANGED_KEY = "SANDBOX_UNIT_CHANGED_KEY";

    // ===========================================================
    // Fields
    // ===========================================================
    private BuildingId mUnitId;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * single instance that's why it's private constructor
     *
     * @param scene                     popup attaches to this scene
     * @param camera                    game camera
     * @param vertexBufferObjectManager object manager to create inner elements
     */
    public SandboxDescriptionPopup(final Scene scene, final Camera camera, final VertexBufferObjectManager vertexBufferObjectManager) {
        super(scene, camera, vertexBufferObjectManager);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected BaseUnitPopupUpdater createUnitPopupUpdater(final VertexBufferObjectManager vboManager) {
        return new SandboxUnitPopupUpdater(vboManager, this);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
