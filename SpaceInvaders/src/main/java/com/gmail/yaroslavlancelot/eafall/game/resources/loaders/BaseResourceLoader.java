package com.gmail.yaroslavlancelot.eafall.game.resources.loaders;

import android.graphics.Bitmap;
import android.graphics.Typeface;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.resources.IResourcesLoader;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.SplashScene;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;

import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains common resources loading logic
 *
 * @author Yaroslav Havrylovych
 */
abstract class BaseResourceLoader implements IResourcesLoader {
    private static final String sProfiling = "profiling";
    protected String mBackgroundImagePath;
    protected List<String> mImagesList = new ArrayList<String>(5);

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
    public void clear() {
        mImagesList.clear();
    }

    @Override
    public synchronized void unloadProfilingFonts() {
        IFont font = FontHolder.getInstance().removeElement(sProfiling);
        font.unload();
    }

    @Override
    public void addImage(String path, int width, int height) {
        if (width == SizeConstants.GAME_FIELD_WIDTH && height == SizeConstants.GAME_FIELD_HEIGHT) {
            mBackgroundImagePath = path;
        } else {
            if (!mImagesList.contains(path)) {
                mImagesList.add(path);
            }
        }
    }

    /** sets #mBackgroundImagePath and triggers #loadBackgroundImage(TextureManager) */
    protected void loadBackgroundImage(String path, TextureManager textureManager) {
        mBackgroundImagePath = path;
        loadBackgroundImage(textureManager);
    }

    /**
     * Create texture atlas source from the bitmap filled with the particular color
     *
     * @param color  bitmap color
     * @param width  region width
     * @param height region height
     * @return newly created texture region
     */
    protected IBitmapTextureAtlasSource createColoredTextureAtlasSource(Color color,
                                                                        int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        bitmap.eraseColor(color.getARGBPackedInt());
        return new BitmapTextureAtlasSource(bitmap);
    }

    /** loads background image using path #mBackgroundImagePath */
    protected void loadBackgroundImage(TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT, TextureOptions.BILINEAR);
        TextureRegionHolder.addElementFromAssets(mBackgroundImagePath,
                smallObjectTexture, EaFallApplication.getContext(), 0, 0);
        smallObjectTexture.load();
    }

    /** create texture atlas source out of bitmap */
    private class BitmapTextureAtlasSource extends EmptyBitmapTextureAtlasSource {
        private Bitmap mBitmap;

        public BitmapTextureAtlasSource(Bitmap bitmap) {
            super(bitmap.getWidth(), bitmap.getHeight());
            mBitmap = bitmap;
        }

        @Override
        public Bitmap onLoadBitmap(Bitmap.Config pBitmapConfig, boolean pMutable) {
            return mBitmap;
        }
    }
}
