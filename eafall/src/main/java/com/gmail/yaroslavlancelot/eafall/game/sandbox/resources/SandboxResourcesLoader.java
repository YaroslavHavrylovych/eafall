package com.gmail.yaroslavlancelot.eafall.game.sandbox.resources;

import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.resources.loaders.game.BaseGameObjectsLoader;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Loads resources for the sandbox (player specific units)
 *
 * @author Yaroslav Havrylovych
 */
public class SandboxResourcesLoader extends BaseGameObjectsLoader {
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
    protected void loadAllianceSpecificResources(TextureManager textureManager) {
        for (IAlliance alliance : AllianceHolder.getInstance().getElements()) {
            alliance.loadUnits(textureManager);
        }
    }

    @Override
    protected void loadPlayerSpecificResources(TextureManager textureManager,
                                               VertexBufferObjectManager vboManager) {
        for (IPlayer player : PlayersHolder.getInstance().getElements()) {
            IAlliance alliance = player.getAlliance();
            loadPlayerSpecificUnits(alliance, textureManager, vboManager, player);
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
