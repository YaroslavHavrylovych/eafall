package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.unit;

import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.DescriptionText;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.DescriptionPopupUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.text.Link;

import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** Update unit description (and only description area) on given area. */
public class UnitDescriptionObject implements DescriptionPopupUpdater.DescriptionAreaUpdater {
    /* constants for */
    private final int mBetweenDescriptionLinesSpace = 5;
    private final int mSpace = 6;

    /* static text */
    private DescriptionText mUnitBuildingNameText;
    private DescriptionText mUnitSpeedText;
    private DescriptionText mUnitHealthText;
    private DescriptionText mReloadTimeText;

    /* values changed with each #updateDescription call */
    private Link mUnitBuildingName;
    private DescriptionText mUnitSpeed;
    private DescriptionText mUnitHealth;
    private DescriptionText mReloadTime;


    public UnitDescriptionObject(VertexBufferObjectManager vertexBufferObjectManager) {

    }

    @Override
    public void updateDescription(RectangularShape drawArea, int objectId, String raceName, String teamName) {

    }

    @Override
    public void clearDescription() {

    }
}
