package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.BuildingType;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;

/** general buildings functionality */
public class Building implements IBuilding {
    /** current building group type */
    private final BuildingType mBuildingType;
    /** income which give all buildings of the current type (can be in percents) */
    private int mIncome;
    /** amount of buildings of the current building type */
    protected int mBuildingsAmount;
    /** building basement, holder for all building elements */
    private Rectangle mBasement;
    /** current building team name */
    protected final String mTeamName;
    /** current building upgrade */
    protected int mUpgrade;

    public Building(BuildingType buildingType, String teamName) {
        mBuildingType = buildingType;
        mTeamName = teamName;
    }

    @Override
    public BuildingType getBuildingType() {
        return mBuildingType;
    }

    @Override
    public int getIncome() {
        return mIncome;
    }

    @Override
    public int getAmount() {
        return mBuildingsAmount;
    }

    @Override
    public IEntity getEntity() {
        return mBasement;
    }

    @Override
    public int getUpgrade() {
        return mUpgrade;
    }

    /** set entity for the current building (in general it's basement) */
    protected void setEntity(Rectangle basement) {
        mBasement = basement;
    }

    protected void setIncome(int income) {
        mIncome = income;
    }

    protected void setBuildingsAmount(int buildingsAmount) {
        mBuildingsAmount = buildingsAmount;
    }
}
