package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.unit;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.DescriptionText;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.DescriptionPopupUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.Locale;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.text.Link;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** Update unit description (and only description area) on given area. */
public class UnitDescriptionAreaUpdater implements DescriptionPopupUpdater.DescriptionAreaUpdater {
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


    public UnitDescriptionAreaUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        Locale locale = LocaleImpl.getInstance();
        // unit building
        mUnitBuildingNameText = new DescriptionText(0, 0,
                locale.getStringById(R.string.description_building), vertexBufferObjectManager);
        mUnitBuildingName = new Link(mUnitBuildingNameText.getWidth() + mSpace, mUnitBuildingNameText.getY(),
                vertexBufferObjectManager);
        // unit health
        mUnitHealthText = new DescriptionText(0, DescriptionText.sFontSize + mBetweenDescriptionLinesSpace,
                locale.getStringById(R.string.description_produce), vertexBufferObjectManager);
    }

    @Override
    public void updateDescription(RectangularShape drawArea, int objectId, String raceName, String teamName) {

    }

    @Override
    public void clearDescription() {

    }
}
