package com.gmail.yaroslavlancelot.eafall.game.resources.loaders.campaign;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.resources.BaseResourceLoader;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.TextButton;

import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.TextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to load resources for guide campaign (the campaign home page)
 *
 * @author Yaroslav Havrylovych
 */
public class CampaignResourceLoader extends BaseResourceLoader {
    private List<TextureAtlas> mAtlases = new ArrayList<>(5);

    @Override
    public void loadImages(TextureManager textureManager,
                           VertexBufferObjectManager vertexBufferObjectManager) {
        //background
        mAtlases.addAll(loadBigImages(textureManager));
        //images
        mAtlases.add(loadSmallImages(textureManager));
        //button
        mAtlases.add(TextButton.loadResources(EaFallApplication.getContext(), textureManager));
    }

    @Override
    public void loadFonts(TextureManager textureManager, FontManager fontManager) {
        //button
        TextButton.loadFonts(fontManager, textureManager);
    }

    @Override
    public void unloadFonts(final FontManager fontManager) {
        throw new UnsupportedOperationException("fonts unloading not supported in campaign");
    }

    @Override
    public void unloadImages(final TextureManager textureManager) {
        for (TextureAtlas textureAtlas : mAtlases) {
            textureAtlas.unload();
        }
    }
}
