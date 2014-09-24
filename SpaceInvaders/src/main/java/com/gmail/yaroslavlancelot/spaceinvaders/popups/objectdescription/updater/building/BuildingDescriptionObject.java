package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.UnitDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.UnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.DescriptionText;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.RacesHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.Locale;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.text.Link;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/**
 * writes building description on given area (p.s. only description popup). Knows about other
 * buttons (e.g. build button) on the description popup so place text with knowing of this.
 */
public class BuildingDescriptionObject {
    private final int mBetweenDescriptionLinesSpace = 5;
    private final int mSpace = 6;
    private DescriptionText mCostText;
    private DescriptionText mCostValue;
    private DescriptionText mProducedUnitText;
    private DescriptionText mUnitCreationTimeText;
    private DescriptionText mUnitCreationTimeValue;
    private DescriptionText mUpgradeText;
    private Link mProducedUnitLink;

    public BuildingDescriptionObject(VertexBufferObjectManager vertexBufferObjectManager) {
        Locale locale = LocaleImpl.getInstance();
        // cost
        mCostText = new DescriptionText(0, 0,
                locale.getStringById(R.string.description_cost), vertexBufferObjectManager);
        mCostValue = new DescriptionText(mCostText.getWidth() + mSpace, 0, vertexBufferObjectManager);
        // produce
        mProducedUnitText = new DescriptionText(0, DescriptionText.sFontSize + mBetweenDescriptionLinesSpace,
                locale.getStringById(R.string.description_produce), vertexBufferObjectManager);
        mProducedUnitLink = new Link(mProducedUnitText.getWidth() + mSpace, mProducedUnitText.getY(), vertexBufferObjectManager);
        // unit creation time
        mUnitCreationTimeText = new DescriptionText(0, 2 * (DescriptionText.sFontSize + mBetweenDescriptionLinesSpace),
                locale.getStringById(R.string.description_unit_producing_time), vertexBufferObjectManager);
        mUnitCreationTimeValue = new DescriptionText(mUnitCreationTimeText.getWidth() + mSpace, mUnitCreationTimeText.getY(),
                "8", vertexBufferObjectManager);
        // upgrade
        mUpgradeText = new DescriptionText(0, 3 * (DescriptionText.sFontSize + mBetweenDescriptionLinesSpace),
                locale.getStringById(R.string.description_upgrade), vertexBufferObjectManager);
    }

    /** update description values (e.g. new building appear) */
    public void updateDescription(RectangularShape drawArea, final int objectId,
                                  final String raceName, final String teamName) {
        attach(drawArea);
        IRace race = RacesHolder.getInstance().getElement(raceName);
        CreepBuildingDummy dummy = race.getBuildingDummy(objectId);
        // cost
        mCostValue.setText(Integer.toString(dummy.getCost()));
        // produced unit
        UnitDummy unitDummy = race.getUnitDummy(objectId);
        mProducedUnitLink.setText(unitDummy.getName());
        mProducedUnitLink.setOnClickListener(new TouchUtils.OnClickListener() {
            @Override
            public void onClick() {
                EventBus.getDefault().post(new UnitDescriptionShowEvent(objectId, teamName));
            }
        });
    }

    /** attaches object to the area */
    private void attach(RectangularShape drawArea) {
        drawArea.attachChild(mCostText);
        drawArea.attachChild(mCostValue);
        drawArea.attachChild(mProducedUnitText);
        drawArea.attachChild(mProducedUnitLink);
        drawArea.attachChild(mUnitCreationTimeText);
        drawArea.attachChild(mUnitCreationTimeValue);
        drawArea.attachChild(mUpgradeText);
    }

    public void initTouches(Scene scene) {
        scene.registerTouchArea(mProducedUnitLink);
    }

    /** detaches objects */
    public void clearDescription() {
        mCostText.detachSelf();
        mCostValue.detachSelf();
        mProducedUnitText.detachSelf();
        mProducedUnitLink.detachSelf();
        mUnitCreationTimeText.detachSelf();
        mUnitCreationTimeValue.detachSelf();
        mUpgradeText.detachSelf();
    }
}
