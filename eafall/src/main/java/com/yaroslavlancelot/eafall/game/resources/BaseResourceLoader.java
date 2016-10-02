package com.yaroslavlancelot.eafall.game.resources;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.audio.GeneralSoundKeys;
import com.yaroslavlancelot.eafall.game.audio.SoundOperations;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.scene.scenes.SplashScene;
import com.yaroslavlancelot.eafall.game.visual.buttons.TextButton;
import com.yaroslavlancelot.eafall.game.visual.font.FontHolder;

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
 * The base resources loader class. Very common methods.
 *
 * @author Yaroslav Havrylovych
 */
public abstract class BaseResourceLoader implements IResourcesLoader {
    private static final String sProfiling = "profiling";
    private Set<String> mBigImages = new HashSet<>(5);
    private Set<String> mImagesList = new HashSet<>(5);

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
    public void loadFonts(TextureManager textureManager, FontManager fontManager) {
        //button
        TextButton.loadFonts(fontManager, textureManager);
    }

    @Override
    public void loadSounds(SoundOperations soundOperations) {
        soundOperations.loadSound(GeneralSoundKeys.SELECT);
        soundOperations.loadSound(GeneralSoundKeys.BUTTON_CLICK);
        soundOperations.loadSound(GeneralSoundKeys.TICK);
        soundOperations.loadSound(GeneralSoundKeys.DENIED);
        soundOperations.loadSound(GeneralSoundKeys.PRESS);
        soundOperations.loadSound(GeneralSoundKeys.UNPRESS);
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
        return new BitmapTextureAtlasSource(color, width, height);
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
        private Color mColor;

        public BitmapTextureAtlasSource(Color color, int width, int height) {
            super(width, height);
            mColor = color;
        }

        @Override
        public Bitmap onLoadBitmap(Bitmap.Config pBitmapConfig, boolean pMutable) {
            Bitmap bitmap = Bitmap.createBitmap(mTextureWidth, mTextureHeight, Bitmap.Config.ARGB_4444);
            bitmap.eraseColor(mColor.getARGBPackedInt());
            return bitmap;
        }
    }
}
