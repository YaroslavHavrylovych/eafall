package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.HashMap;
import java.util.Map;

public class TextureRegionHolderUtils {

    private final static TextureRegionHolderUtils S_TEXTURE_REGION_HOLDER_UTILS = new TextureRegionHolderUtils();
    private Map<String, ITextureRegion> textureRegionMap = new HashMap<String, ITextureRegion>(15);

    private TextureRegionHolderUtils() {
    }

    public static TextureRegionHolderUtils getInstance() {
        return S_TEXTURE_REGION_HOLDER_UTILS;
    }

    public ITextureRegion getRegion(String textureRegionId) {
        return textureRegionMap.get(textureRegionId);
    }

    public void addTextureRegion(String textureRegionId, ITextureRegion textureRegion) {
        textureRegionMap.put(textureRegionId, textureRegion);
    }
}
