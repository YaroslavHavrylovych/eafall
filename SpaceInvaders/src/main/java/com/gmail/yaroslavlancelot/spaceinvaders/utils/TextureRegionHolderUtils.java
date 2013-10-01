package com.gmail.yaroslavlancelot.spaceinvaders.utils;

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
}
