package com.yaroslavlancelot.eafall.game.resources.loaders.game;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.popup.GameOverPopup;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.BuildingLessDescriptionPopup;
import com.yaroslavlancelot.eafall.game.scene.hud.SandboxGameHud;

import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Loads resources for the find path games (player specific units)
 *
 * @author Yaroslav Havrylovych
 */
public class FindPathResourcesLoader extends BaseGameObjectsLoader {
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
        //sprites
        loadEndPoint(textureManager);
        loadVisibleArea(textureManager);
    }

    @Override
    public void loadFonts(final TextureManager textureManager, final FontManager fontManager) {
        super.loadFonts(textureManager, fontManager);
        //hud
        SandboxGameHud.loadFonts(fontManager, textureManager);
        BuildingLessDescriptionPopup.loadFonts(fontManager, textureManager);
        GameOverPopup.loadFonts(fontManager, textureManager);
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
    private void loadEndPoint(TextureManager textureManager) {
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager, 750, 250);
        TextureRegionHolder.addTiledElementFromAssets(
                StringConstants.KEY_ENDPOINT,
                textureAtlas, EaFallApplication.getContext(),
                0, 0, 3, 1);
        textureAtlas.load();
    }

    private void loadVisibleArea(TextureManager textureManager) {
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager, 300, 450);
        TextureRegionHolder.addElementFromAssets(
                StringConstants.KEY_VISIBLE_AREA,
                textureAtlas, EaFallApplication.getContext(), 0, 0);
        TextureRegionHolder.addElementFromAssets(
                StringConstants.KEY_BIGGER_VISIBLE_AREA,
                textureAtlas, EaFallApplication.getContext(), 0, 150);
        textureAtlas.load();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
