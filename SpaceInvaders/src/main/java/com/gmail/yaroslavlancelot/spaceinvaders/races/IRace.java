package com.gmail.yaroslavlancelot.spaceinvaders.races;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

/** abstract race interface */
public interface IRace {
    String getRaceName();

    int getBuildingsAmount();

    StaticObject getBuildingById(int id, VertexBufferObjectManager objectManager, Color teamColor);

    Unit getUnitForBuilding(int buildingId, VertexBufferObjectManager objectManager, Color teamColor);

    void loadResources(TextureManager textureManager, Context context);

    /**
     * returns buildings top left coordinates on the planet
     * @param buildingId building id to find coordinates
     * @return float array which contains two value. First - x position and Second - y position.
     */
    float[] getBuildingPositionOnThePlanet(int buildingId);
}
