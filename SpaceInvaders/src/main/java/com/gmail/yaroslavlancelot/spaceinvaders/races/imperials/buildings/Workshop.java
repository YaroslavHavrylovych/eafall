package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.buildings;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.CreepBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** first building of imperials */
public class Workshop extends CreepBuilding {
    public static final String KEY_IMPERIALS_SIX_BUILDING = GameStringsConstantsAndUtils.getPathToBuildings(Imperials.RACE_NAME) + "workshop.png";
    private static final int mPosX = SizeConstants.PLANET_DIAMETER / 2 - SizeConstants.BUILDING_DIAMETER,
            mPosY = SizeConstants.PLANET_DIAMETER / 2 + SizeConstants.BUILDING_DIAMETER;

    public Workshop(VertexBufferObjectManager vertexBufferObjectManager) {
        super(mPosX, mPosY, TextureRegionHolderUtils.getInstance().getElement(KEY_IMPERIALS_SIX_BUILDING), vertexBufferObjectManager);
        mCost = 360;
        setWidth(SizeConstants.BUILDING_DIAMETER);
        setHeight(SizeConstants.BUILDING_DIAMETER);
        setObjectStringId(R.string.workshop);
    }

    @Override
    public void setWidth(final float pWidth) {
        super.setWidth(pWidth);
    }

    @Override
    public void setHeight(final float pHeight) {
        super.setHeight(pHeight);
    }

    @Override
    public void setBackgroundArea() {
        setBackgroundArea(new Area(3, 6, 10, 3));
    }

    public static void loadResources(final Context context, final BitmapTextureAtlas textureAtlas) {
        loadResource(KEY_IMPERIALS_SIX_BUILDING, context, textureAtlas, 32, 16);
    }
}
