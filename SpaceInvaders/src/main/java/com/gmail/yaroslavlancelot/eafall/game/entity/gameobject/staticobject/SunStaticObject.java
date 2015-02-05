package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringsAndPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class SunStaticObject extends StaticObject {
    public SunStaticObject(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
        setWidth(Sizes.SUN_DIAMETER);
        setHeight(Sizes.SUN_DIAMETER);
        mIncomeIncreasingValue = 0;
    }

    public static void loadImages(Context context, TextureManager textureManager){
        BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(textureManager,
                256, 256, TextureOptions.BILINEAR);
        TextureRegionHolder.getInstance().addElement(
                StringsAndPath.KEY_SUN, BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        bitmapTextureAtlas, context, StringsAndPath.FILE_SUN, 0, 0)
        );
        bitmapTextureAtlas.load();
    }
}
