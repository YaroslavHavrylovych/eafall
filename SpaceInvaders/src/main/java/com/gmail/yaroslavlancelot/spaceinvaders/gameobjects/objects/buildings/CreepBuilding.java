package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;

public class CreepBuilding extends StaticObject {
    public CreepBuilding(BuildingBuilder buildingBuilder) {
        super(buildingBuilder.getX(), buildingBuilder.getY(),
                buildingBuilder.getTextureRegion(), buildingBuilder.getObjectManager());
        mIncomeIncreasingValue = (int) ((mCost = buildingBuilder.getCost()) * 0.03);
        setWidth(buildingBuilder.getWidth());
        setHeight(buildingBuilder.getHeight());
        setObjectStringId(buildingBuilder.getObjectStringId());
    }

    @Override
    public int getObjectIncomeIncreasingValue() {
        return mIncomeIncreasingValue;
    }
}
