package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.CreepBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** first building of imperials */
public class Barracks extends CreepBuilding {
    public static final String KEY_IMPERIALS_THIRD_BUILDING = GameStringsConstantsAndUtils.getPathToBuildings(Imperials.RACE_NAME) + "barracks.png";
    private static final int mPosX = SizeConstants.PLANET_DIAMETER / 2 + SizeConstants.BUILDING_DIAMETER,
            mPosY = SizeConstants.PLANET_DIAMETER / 2 - SizeConstants.BUILDING_DIAMETER;

    public Barracks(VertexBufferObjectManager vertexBufferObjectManager) {
        super(mPosX, mPosY, TextureRegionHolderUtils.getInstance().getElement(KEY_IMPERIALS_THIRD_BUILDING), vertexBufferObjectManager);
        mCost = 220;
        setWidth(SizeConstants.BUILDING_DIAMETER);
        setHeight(SizeConstants.BUILDING_DIAMETER);
    }

    public static void loadResources(final Context context, final BitmapTextureAtlas textureAtlas) {
        loadResource(KEY_IMPERIALS_THIRD_BUILDING, context, textureAtlas, 32, 0);
    }

    @Override
    public void setBackgroundArea() {
        setBackgroundArea(new Area(0, 0, getWidth(), getHeight()));
    }
}
