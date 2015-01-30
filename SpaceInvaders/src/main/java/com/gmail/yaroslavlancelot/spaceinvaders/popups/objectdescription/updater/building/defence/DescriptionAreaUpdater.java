package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building.defence;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.SpaceInvadersApplication;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.AllianceHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.IAlliance;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dummies.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.UnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.DescriptionText;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.BaseDescriptionAreaUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.text.Link;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * For CREEP building : <br/>
 * writes building description on given area (p.s. only description popup). Knows about other
 * buttons (e.g. build button) on the description popup so place text with knowing of this.
 */
public class DescriptionAreaUpdater extends BaseDescriptionAreaUpdater {
    /* values changed with each #updateDescription call */
    private DescriptionText mCostValue;
    private DescriptionText mUnitCreationTimeValue;
    private Link mProducedUnitLink;

    public DescriptionAreaUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        // cost
        Text text = createDescriptionText(0, R.string.description_cost, vertexBufferObjectManager);
        mCostValue = createDescriptionText(text.getWidth() + mSpace, 0, vertexBufferObjectManager);
        // produce
        text = createDescriptionText(1, R.string.description_produce, vertexBufferObjectManager);
        mProducedUnitLink = createLink(text.getWidth(), text.getY(), vertexBufferObjectManager);
        // unit creation time
        text = createDescriptionText(2, R.string.description_unit_producing_time, vertexBufferObjectManager);
        mUnitCreationTimeValue = createDescriptionText(text.getWidth() + mSpace, text.getY(), "50", vertexBufferObjectManager);

        // touch
        scene.registerTouchArea(mProducedUnitLink);
    }

    @Override
    protected void iniDescriptionTextList() {
        mDescriptionTextList = new ArrayList<Text>(7);
    }

    @Override
    public void updateDescription(RectangularShape drawArea, Object objectId,
                                  final String raceName, final String teamName) {
        super.updateDescription(drawArea, objectId, raceName, teamName);
        final BuildingId buildingId = (BuildingId) objectId;
        IAlliance race = AllianceHolder.getInstance().getElement(raceName);
        DefenceBuildingDummy dummy = (DefenceBuildingDummy) race.getBuildingDummy(buildingId);
        //cost
        mCostValue.setText(Integer.toString(dummy.getCost(buildingId.getUpgrade())));
        //produced unit
        final int unitId = dummy.getOrbitalStationUnitId();
        UnitDummy unitDummy = race.getUnitDummy(unitId);
        mProducedUnitLink.setText(SpaceInvadersApplication.getContext().getResources().getString(
                unitDummy.getUnitStringId()));
        mProducedUnitLink.setOnClickListener(new TouchUtils.OnClickListener() {
            @Override
            public void onClick() {
                EventBus.getDefault().post(new UnitByBuildingDescriptionShowEvent(buildingId, teamName));
            }
        });
        //building time
        mUnitCreationTimeValue.setText(Integer.toString(dummy.getOrbitalStationCreationTime()));
    }
}
