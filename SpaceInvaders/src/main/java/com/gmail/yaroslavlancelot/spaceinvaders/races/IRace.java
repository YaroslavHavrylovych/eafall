package com.gmail.yaroslavlancelot.spaceinvaders.races;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import org.andengine.opengl.texture.TextureManager;

/** abstract race interface */
public interface IRace {
    String getRaceName();

    int getBuildingsAmount();

    String[] getBuildingsNames();

    StaticObject getBuildingById(int buildingId);

    int getBuildingCostById(int buildingId);

    Unit getUnitForBuilding(int buildingId);

    void loadResources(TextureManager textureManager, Context context);
}
