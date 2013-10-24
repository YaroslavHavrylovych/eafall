package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import android.content.Context;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

/** Holds all {@link org.andengine.opengl.texture.region.ITextureRegion} used in game */
public class TextureRegionHolderUtils extends HolderUtils<ITextureRegion> {
    /** current class instance (singleton realization) */
    private final static TextureRegionHolderUtils S_TEXTURE_REGION_HOLDER_UTILS = new TextureRegionHolderUtils();

    private TextureRegionHolderUtils() {
    }

    /** return {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils} object */
    public static TextureRegionHolderUtils getInstance() {
        return S_TEXTURE_REGION_HOLDER_UTILS;
    }

    /**
     * add element to texture atlas and add it to {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils}
     *
     * @param key path to file. Used like element key for {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils}.
     * @param utils instance of {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils}
     * @param textureAtlas texture atlas to load texture
     * @param context app context
     * @param x x position to which texture should be load
     * @param y y position to which texture should be load
     */
    public static void addElementFromAssets(String key, TextureRegionHolderUtils utils,
                                            BitmapTextureAtlas textureAtlas, Context context, int x, int y) {
        if (!utils.isElementExist(key))
            utils.addElement(key, BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas, context, key, x, y));
    }
}
