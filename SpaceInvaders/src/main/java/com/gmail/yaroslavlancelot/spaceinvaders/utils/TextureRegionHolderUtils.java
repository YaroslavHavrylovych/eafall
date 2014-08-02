package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

/** Holds all {@link org.andengine.opengl.texture.region.ITextureRegion} used in game */
public class TextureRegionHolderUtils extends HolderUtils<ITextureRegion> {
    /** current class instance (singleton realization) */
    private final static TextureRegionHolderUtils S_TEXTURE_REGION_HOLDER_UTILS = new TextureRegionHolderUtils();

    private TextureRegionHolderUtils() {
    }

    /**
     * add element to texture atlas and add it to {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils}
     *
     * @param key          path to file. Used like element key for {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils}.
     * @param utils        instance of {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils}
     * @param textureAtlas texture atlas to loadGeneralGameTextures texture
     * @param context      app context
     * @param x            x position to which texture should be loadGeneralGameTextures
     * @param y            y position to which texture should be loadGeneralGameTextures
     */
    public static void addElementFromAssets(String key, TextureRegionHolderUtils utils,
                                            BitmapTextureAtlas textureAtlas, Context context, int x, int y) {
        if (!utils.isElementExist(key))
            utils.addElement(key, BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas, context, key, x, y));
    }

    /**
     * add element to texture atlas and add it to {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils}
     *
     * @param key          path to file. Used like element key for {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils}.
     * @param utils        instance of {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils}
     * @param textureAtlas texture atlas to loadGeneralGameTextures texture
     * @param context      app context
     * @param x            x position to which texture should be loadGeneralGameTextures
     * @param y            y position to which texture should be loadGeneralGameTextures
     * @param columns      columns for tiled images
     * @param rows         rows for tiled images
     */
    public static void addTiledElementFromAssets(String key, TextureRegionHolderUtils utils,
                                                 BitmapTextureAtlas textureAtlas, Context context, int x, int y, int columns, int rows) {
        if (!utils.isElementExist(key))
            utils.addElement(key, BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureAtlas, context, key, x, y, columns, rows));
    }

    public static void loadGeneralGameTextures(Context context, TextureManager textureManager) {
        BitmapTextureAtlas biggerObjectsTexture = new BitmapTextureAtlas(textureManager,
                512, 512, TextureOptions.BILINEAR);
        TextureRegionHolderUtils utils = getInstance();
        utils.addElement(GameStringsConstantsAndUtils.KEY_SUN,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(biggerObjectsTexture, context, GameStringsConstantsAndUtils.FILE_SUN, 0, 0));
        utils.addElement(GameStringsConstantsAndUtils.KEY_RED_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(biggerObjectsTexture, context, GameStringsConstantsAndUtils.FILE_RED_PLANET,
                        0, SizeConstants.FILE_SUN_DIAMETER)
        );
        utils.addElement(GameStringsConstantsAndUtils.KEY_BLUE_PLANET,
                BitmapTextureAtlasTextureRegionFactory.createFromAsset(biggerObjectsTexture, context, GameStringsConstantsAndUtils.FILE_BLUE_PLANET,
                        SizeConstants.PLANET_DIAMETER, SizeConstants.FILE_SUN_DIAMETER)
        );
        biggerObjectsTexture.load();
    }

    /** return {@link com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils} object */
    public static TextureRegionHolderUtils getInstance() {
        return S_TEXTURE_REGION_HOLDER_UTILS;
    }
}
