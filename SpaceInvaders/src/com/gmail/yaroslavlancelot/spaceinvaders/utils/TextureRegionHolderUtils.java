package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import org.andengine.opengl.texture.region.ITextureRegion;

public class TextureRegionHolderUtils extends HolderUtils<ITextureRegion> {

    private final static TextureRegionHolderUtils S_TEXTURE_REGION_HOLDER_UTILS = new TextureRegionHolderUtils();

    private TextureRegionHolderUtils() {
    }

    public static TextureRegionHolderUtils getInstance() {
        return S_TEXTURE_REGION_HOLDER_UTILS;
    }
}
