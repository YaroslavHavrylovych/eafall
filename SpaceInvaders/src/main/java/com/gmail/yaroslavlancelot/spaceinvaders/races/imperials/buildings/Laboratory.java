package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.CreepBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** first building of imperials */
public class Laboratory extends CreepBuilding {
    public static final String KEY_IMPERIALS_SEVEN_BUILDING = GameStringsConstantsAndUtils.getPathToBuildings(Imperials.RACE_NAME) + "imperials_seven_building.png";
    private static final int mPosX = SizeConstants.PLANET_DIAMETER / 2 - 2 * SizeConstants.BUILDING_DIAMETER,
            mPosY = SizeConstants.PLANET_DIAMETER / 2;

    public Laboratory(VertexBufferObjectManager vertexBufferObjectManager) {
        super(mPosX, mPosY, TextureRegionHolderUtils.getInstance().getElement(KEY_IMPERIALS_SEVEN_BUILDING), vertexBufferObjectManager);
        mCost = 400;
        setWidth(SizeConstants.BUILDING_DIAMETER);
        setHeight(SizeConstants.BUILDING_DIAMETER);
    }

    public static void loadResources(final Context context, final BitmapTextureAtlas textureAtlas) {
        loadResource(KEY_IMPERIALS_SEVEN_BUILDING, context, textureAtlas, 0, 10);
    }
}
