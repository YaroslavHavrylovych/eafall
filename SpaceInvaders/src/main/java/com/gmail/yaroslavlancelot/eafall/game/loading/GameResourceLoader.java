package com.gmail.yaroslavlancelot.eafall.game.loading;

import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Handle loading logic for whole game resources
 * (e.g. different texture atlases for sprite groups etc).
 */
public interface GameResourceLoader {
    void loadSplashImages(TextureManager textureManager,
                          VertexBufferObjectManager vertexBufferObjectManager);

    void loadInGameImages(TextureManager textureManager,
                          VertexBufferObjectManager vertexBufferObjectManager);

    void loadInGameFonts(TextureManager textureManager,
                         FontManager fontManager);

    void unloadInGameFonts(TextureManager textureManager,
                           FontManager fontManager);

    void unloadGameImages();

    void unloadSplashImages();
}
