package com.gmail.yaroslavlancelot.eafall.game.entity;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.general.Holder;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

/** Holds all {@link org.andengine.opengl.texture.region.ITextureRegion} used in game */
public class TextureRegionHolder extends Holder<ITextureRegion> {
    /** current class instance (singleton realization) */
    private final static TextureRegionHolder sTextureRegionHolderUtils = new TextureRegionHolder();

    private TextureRegionHolder() {
    }

    /**
     * add element to texture atlas and add it to {@link TextureRegionHolder}
     *
     * @param key          path to file. Used like element key for {@link TextureRegionHolder}.
     * @param textureAtlas texture atlas to loadGeneralGameTextures texture
     * @param context      app context
     * @param x            x position to which texture should be loadGeneralGameTextures
     * @param y            y position to which texture should be loadGeneralGameTextures
     */
    public static ITextureRegion addElementFromAssets(String key, BitmapTextureAtlas textureAtlas, Context context, int x, int y) {
        if (!sTextureRegionHolderUtils.isElementExist(key)) {
            return 
                    sTextureRegionHolderUtils.addElement(key, 
                            BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                                    textureAtlas, context, key, x, y));
        }
        return sTextureRegionHolderUtils.getElement(key);
    }

    /**
     * add element to texture atlas and add it to {@link TextureRegionHolder}
     *
     * @param key          path to file. Used like element key for {@link TextureRegionHolder}.
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

    public static ITextureRegion getRegion(String key) {
        return getInstance().getElement(key);
    }

    /** return {@link TextureRegionHolder} object */
    public static TextureRegionHolder getInstance() {
        return sTextureRegionHolderUtils;
    }
}
