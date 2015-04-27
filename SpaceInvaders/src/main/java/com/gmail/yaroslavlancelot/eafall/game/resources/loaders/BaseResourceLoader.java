package com.gmail.yaroslavlancelot.eafall.game.resources.loaders;

import android.graphics.Typeface;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.resources.IResourcesLoader;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.SplashScene;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;

import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

/**
 * Contains common resources loading logic
 *
 * @author Yaroslav Havrylovych
 */
abstract class BaseResourceLoader implements IResourcesLoader {
    private static final String sProfiling = "profiling";

    @Override
    public synchronized void loadProfilingFonts(TextureManager textureManager, FontManager fontManager) {
        IFont font = FontFactory.create(fontManager, textureManager, 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD), SizeConstants.MONEY_FONT_SIZE, Color.WHITE.hashCode());
        font.load();
        FontHolder.getInstance().addElement(sProfiling, font);
    }

    @Override
    public synchronized void loadSplashImages(TextureManager textureManager,
                                              VertexBufferObjectManager vertexBufferObjectManager) {
        SplashScene.loadResources(EaFallApplication.getContext(), textureManager);
    }

    @Override
    public synchronized void unloadSplashImages() {
        SplashScene.unloadResources();
    }

    @Override
    public synchronized void unloadProfilingFonts() {
        IFont font = FontHolder.getInstance().removeElement(sProfiling);
        font.unload();
    }
}
