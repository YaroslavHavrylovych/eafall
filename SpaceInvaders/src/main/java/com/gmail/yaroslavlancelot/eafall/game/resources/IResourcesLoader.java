package com.gmail.yaroslavlancelot.eafall.game.resources;

import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Fonts and images/textures resources loading/unloading logic
 */
public interface IResourcesLoader {
    /**
     * add images which has to be loaded in addition.
     * <br/>
     * Note:
     * <br/>
     * - images with the any side bigger than 1024 loads in separate
     * <br/>
     * - other images loads in the single buildable bitmap (size 2048 * 2048)
     */
    void addImage(String path, int width, int height);

    void loadProfilingFonts(TextureManager textureManager,
                            FontManager fontManager);

    void loadSplashImages(TextureManager textureManager,
                          VertexBufferObjectManager vertexBufferObjectManager);

    void loadImages(TextureManager textureManager,
                    VertexBufferObjectManager vertexBufferObjectManager);

    void loadFonts(TextureManager textureManager,
                   FontManager fontManager);

    void unloadFonts(FontManager fontManager);

    void unloadImages(TextureManager textureManager);

    void unloadSplashImages();
}
