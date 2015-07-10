package com.gmail.yaroslavlancelot.eafall.game.entity;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.general.Holder;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;

/** Holds all {@link org.andengine.opengl.texture.region.ITextureRegion} used in game */
public class TextureRegionHolder extends Holder<ITextureRegion> {
    /** current class instance (singleton realization) */
    private final static TextureRegionHolder sTextureRegionHolderUtils = new TextureRegionHolder();

    private TextureRegionHolder() {
    }

    /** return {@link TextureRegionHolder} object */
    public static TextureRegionHolder getInstance() {
        return sTextureRegionHolderUtils;
    }

    /**
     * add element to texture atlas and add it to {@link TextureRegionHolder}
     * <br/>
     * invokes {@link TextureRegionHolder#addElementFromAssets(String, String, BitmapTextureAtlas, Context, int, int)}
     *
     * @param key          path to file. Used like element key for {@link TextureRegionHolder}.
     * @param textureAtlas texture atlas to loadGeneralGameTextures texture
     * @param context      app context
     * @param x            x position to which texture should be loadGeneralGameTextures
     * @param y            y position to which texture should be loadGeneralGameTextures
     */
    public static ITextureRegion addElementFromAssets(String key, BitmapTextureAtlas textureAtlas, Context context, int x, int y) {
        return addElementFromAssets(key, key, textureAtlas, context, x, y);
    }

    /**
     * add element to texture atlas and add it to {@link TextureRegionHolder}
     * <br/>
     * invokes {@link TextureRegionHolder#addElementFromAssets(String, String, BitmapTextureAtlas, Context, int, int)}
     *
     * @param key          path to file. Used like element key for {@link TextureRegionHolder}.
     * @param textureAtlas texture atlas to loadGeneralGameTextures texture
     * @param context      app context
     */
    public static ITextureRegion addElementFromAssets(String key, BuildableBitmapTextureAtlas textureAtlas, Context context) {
        return addElementFromAssets(key, key, textureAtlas, context);
    }

    /**
     * add element to texture atlas and add it to {@link TextureRegionHolder}
     *
     * @param key          Used like element key for {@link TextureRegionHolder}.
     * @param path         path to file.
     * @param textureAtlas texture atlas to loadGeneralGameTextures texture
     * @param context      app context
     */
    public static ITextureRegion addElementFromAssets(String key, String path,
                                                      BuildableBitmapTextureAtlas textureAtlas,
                                                      Context context) {
        if (!sTextureRegionHolderUtils.isElementExist(key)) {
            TextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                    textureAtlas, context, path);
            return sTextureRegionHolderUtils.addElement(key, textureRegion);
        }
        return sTextureRegionHolderUtils.getElement(key);
    }

    /**
     * add element to texture atlas and add it to {@link TextureRegionHolder}
     *
     * @param key          Used like element key for {@link TextureRegionHolder}.
     * @param path         path to file.
     * @param textureAtlas texture atlas to loadGeneralGameTextures texture
     * @param context      app context
     * @param x            x position to which texture should be loadGeneralGameTextures
     * @param y            y position to which texture should be loadGeneralGameTextures
     */
    public static ITextureRegion addElementFromAssets(String key, String path, BitmapTextureAtlas
            textureAtlas, Context context, int x, int y) {
        if (!sTextureRegionHolderUtils.isElementExist(key)) {
            TextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                    textureAtlas, context, path, x, y);
            return sTextureRegionHolderUtils.addElement(key, textureRegion);
        }
        return sTextureRegionHolderUtils.getElement(key);
    }

    /**
     * add element from the source to to texture atlas and add it to {@link TextureRegionHolder}
     *
     * @param key          Used like element key for {@link TextureRegionHolder}.
     * @param textureAtlas texture atlas to loadGeneralGameTextures texture
     * @param x            x position to which texture should be loadGeneralGameTextures
     * @param y            y position to which texture should be loadGeneralGameTextures
     */
    public static ITextureRegion addElementFromSource(String key, BitmapTextureAtlas
            textureAtlas, IBitmapTextureAtlasSource source, int x, int y) {
        if (!sTextureRegionHolderUtils.isElementExist(key)) {
            TextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource
                    (textureAtlas, source, x, y);
            return sTextureRegionHolderUtils.addElement(key, textureRegion);
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
        if (!sTextureRegionHolderUtils.isElementExist(key)) {
            sTextureRegionHolderUtils.addElement(key,
                    BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureAtlas,
                            context, key, x, y, columns, rows));
        }
    }

    public static ITextureRegion getRegion(String key) {
        return getInstance().getElement(key);
    }
}
