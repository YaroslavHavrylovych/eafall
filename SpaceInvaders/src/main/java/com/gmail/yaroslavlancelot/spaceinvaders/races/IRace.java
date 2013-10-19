package com.gmail.yaroslavlancelot.spaceinvaders.races;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

/** abstract race interface */
public interface IRace {
    int BUILDINGS_AMOUNT = 14;

    String getRaceName();

    StaticObject getBuildingById(int id, VertexBufferObjectManager objectManager);

    Unit getUnitForBuilding(int buildingId, VertexBufferObjectManager objectManager, Color teamColor);

    void loadResources(TextureManager textureManager, Context context);
}
