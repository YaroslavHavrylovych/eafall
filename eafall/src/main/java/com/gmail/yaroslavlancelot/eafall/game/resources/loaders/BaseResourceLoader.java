package com.gmail.yaroslavlancelot.eafall.game.resources.loaders;

import android.content.Context;
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
import org.andengine.opengl.texture.atlas.TextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Common methods similar for loading
 *
 * @author Yaroslav Havrylovych
 */
abstract class BaseResourceLoader implements IResourcesLoader {
    private static final String sProfiling = "profiling";
    protected Set<String> mBigImages = new HashSet<String>(5);
    protected Set<String> mImagesList = new HashSet<String>(5);

    @Override
    public void addImage(String path, int width, int height) {
        if (width > 1024 || height > 1024) {
            mBigImages.add(path);
        } else {
            mImagesList.add(path);
        }
    }

    @Override
    public void loadProfilingFonts(TextureManager textureManager, FontManager fontManager) {
        IFont font = FontFactory.create(fontManager, textureManager, 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.BOLD), SizeConstants.MONEY_FONT_SIZE, Color.WHITE.hashCode());
        font.load();
        FontHolder.getInstance().addElement(sProfiling, font);
    }

    @Override
    public void loadSplashImages(TextureManager textureManager,
                                              VertexBufferObjectManager vertexBufferObjectManager) {
        SplashScene.loadResources(EaFallApplication.getContext(), textureManager);
    }

    @Override
    public void unloadSplashImages() {
        SplashScene.unloadResources();
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
    protected List<TextureAtlas> loadBigImages(TextureManager textureManager) {
        Context context = EaFallApplication.getContext();
        List<TextureAtlas> textures = new ArrayList<TextureAtlas>(mBigImages.size());
        for (String path : mBigImages) {
            BitmapTextureAtlas atlas = new BitmapTextureAtlas(textureManager,
                    SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT, TextureOptions.BILINEAR);
            TextureRegionHolder.addElementFromAssets(path, atlas, context, 0, 0);
            atlas.load();
        }
        return textures;
    }

    protected TextureAtlas loadSmallImages(TextureManager textureManager) {
        Context context = EaFallApplication.getContext();
        BuildableBitmapTextureAtlas textureAtlas
                = new BuildableBitmapTextureAtlas(textureManager, 2048, 2048);

        for (String path : mImagesList) {
            TextureRegionHolder.addElementFromAssets(path, textureAtlas, context);
        }

        BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas> builder
                = new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(1, 1, 1);
        try {
            textureAtlas.build(builder);
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            throw new IllegalStateException("can't build small images atlas");
        }

        textureAtlas.load();
        return textureAtlas.getTextureAtlas();
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