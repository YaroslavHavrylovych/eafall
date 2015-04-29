package com.gmail.yaroslavlancelot.eafall.game.resources;

import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Handle loading logic for whole game resources
 * (e.g. different texture atlases for sprite groups etc).
 */
public interface IResourcesLoader {
    void loadProfilingFonts(TextureManager textureManager,
                            FontManager fontManager);

    void loadSplashImages(TextureManager textureManager,
                          VertexBufferObjectManager vertexBufferObjectManager);

    void loadImages(TextureManager textureManager,
                    VertexBufferObjectManager vertexBufferObjectManager);

    void loadFonts(TextureManager textureManager,
                   FontManager fontManager);

    void unloadFonts(TextureManager textureManager,
                     FontManager fontManager);

    void unloadImages();

    void unloadSplashImages();


    void unloadProfilingFonts();

    /**
     * add images which has to be loaded in addition.
     * <br/>
     * Note:
     * <br/>
     * - image which have the size of the screen will be loaded in separate as a background.
     * <br/>
     * - images which are not background (we don't check but sure that they're less)
     * will be grouped into atlases by 4
     * (or maybe another static value amount).
     */
    void addImage(String path, int width, int height);
}
