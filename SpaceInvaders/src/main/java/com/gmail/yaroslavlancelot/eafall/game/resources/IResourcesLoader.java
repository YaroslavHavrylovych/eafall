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
}
