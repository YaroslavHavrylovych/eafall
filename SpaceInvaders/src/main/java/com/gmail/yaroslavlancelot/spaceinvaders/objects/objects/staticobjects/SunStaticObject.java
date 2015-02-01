package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.staticobjects;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class SunStaticObject extends StaticObject {
    public SunStaticObject(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
        setWidth(SizeConstants.SUN_DIAMETER);
        setHeight(SizeConstants.SUN_DIAMETER);
        mIncomeIncreasingValue = 0;
    }

    public static void loadImages(Context context, TextureManager textureManager){
        BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(textureManager,
                256, 256, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.getInstance().addElement(
                StringsAndPathUtils.KEY_SUN, BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        bitmapTextureAtlas, context, StringsAndPathUtils.FILE_SUN, 0, 0)
        );
        bitmapTextureAtlas.load();
    }
}
