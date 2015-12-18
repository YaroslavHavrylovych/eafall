package com.yaroslavlancelot.eafall.game.sandbox.resources;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.BuildingLessDescriptionPopup;
import com.yaroslavlancelot.eafall.game.resources.loaders.game.BaseGameObjectsLoader;
import com.yaroslavlancelot.eafall.game.scene.hud.SandboxGameHud;

import org.andengine.opengl.font.FontManager;
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
    public void loadImages(final TextureManager textureManager, final VertexBufferObjectManager vertexBufferObjectManager) {
        super.loadImages(textureManager, vertexBufferObjectManager);
        //hud
        SandboxGameHud.loadResource(EaFallApplication.getContext(), textureManager);
        BuildingLessDescriptionPopup.loadResources(EaFallApplication.getContext(), textureManager);
    }

    @Override
    public void loadFonts(final TextureManager textureManager, final FontManager fontManager) {
        super.loadFonts(textureManager, fontManager);
        //hud
        SandboxGameHud.loadFonts(fontManager, textureManager);
        BuildingLessDescriptionPopup.loadFonts(fontManager, textureManager);
    }

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
