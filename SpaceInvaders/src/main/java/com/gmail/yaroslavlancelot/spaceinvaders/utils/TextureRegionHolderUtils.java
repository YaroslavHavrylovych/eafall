package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import android.content.Context;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

/** Holds all {@link org.andengine.opengl.texture.region.ITextureRegion} used in game */
public class TextureRegionHolderUtils extends HolderUtils<ITextureRegion> {
    /** current class instance (singleton realization) */
    private final static TextureRegionHolderUtils sTextureRegionHolderUtils = new TextureRegionHolderUtils();

    private TextureRegionHolderUtils() {
    }

    /**
     * add element to texture atlas and add it to {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils}
     *
     * @param key          path to file. Used like element key for {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils}.
     * @param textureAtlas texture atlas to loadGeneralGameTextures texture
     * @param context      app context
     * @param x            x position to which texture should be loadGeneralGameTextures
     * @param y            y position to which texture should be loadGeneralGameTextures
     */
    public static void addElementFromAssets(String key, BitmapTextureAtlas textureAtlas, Context context, int x, int y) {
        if (!sTextureRegionHolderUtils.isElementExist(key))
            sTextureRegionHolderUtils.addElement(key, BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas, context, key, x, y));
    }

    /**
     * add element to texture atlas and add it to {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils}
     *
     * @param key          path to file. Used like element key for {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils}.
     * @param textureAtlas texture atlas to loadGeneralGameTextures texture
     * @param context      app context
     * @param x            x position to which texture should be loadGeneralGameTextures
     * @param y            y position to which texture should be loadGeneralGameTextures
     * @param columns      columns for tiled images
     * @param rows         rows for tiled images
     */
    public static void addTiledElementFromAssets(String key, BitmapTextureAtlas textureAtlas, Context context, int x, int y, int columns, int rows) {
        if (!sTextureRegionHolderUtils.isElementExist(key))
            sTextureRegionHolderUtils.addElement(key, BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureAtlas, context, key, x, y, columns, rows));
    }

    /** return {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils} object */
    public static TextureRegionHolderUtils getInstance() {
        return sTextureRegionHolderUtils;
    }
}
