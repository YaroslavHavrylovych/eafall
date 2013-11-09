package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.CreepBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** first building of imperials */
public class TrainingCenter extends CreepBuilding {
    public static final String KEY_IMPERIALS_FIFTH_BUILDING = GameStringsConstantsAndUtils.getPathToBuildings(Imperials.RACE_NAME) + "training_center.png";
    private static final int mPosX = SizeConstants.PLANET_DIAMETER / 2,
            mPosY = mPosX - SizeConstants.BUILDING_DIAMETER;

    public TrainingCenter(VertexBufferObjectManager vertexBufferObjectManager) {
        super(mPosX, mPosY, TextureRegionHolderUtils.getInstance().getElement(KEY_IMPERIALS_FIFTH_BUILDING), vertexBufferObjectManager);
        mCost = 340;
        setWidth(SizeConstants.BUILDING_DIAMETER);
        setHeight(SizeConstants.BUILDING_DIAMETER);
    }

    public static void loadResources(final Context context, final BitmapTextureAtlas textureAtlas) {
        loadResource(KEY_IMPERIALS_FIFTH_BUILDING, context, textureAtlas, 16, 16);
    }

    @Override
    public void setBackgroundArea() {
        setBackgroundArea(new Area(13, 4, 2, 8));
    }
}
