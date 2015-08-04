package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.defence;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.DescriptionText;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.BaseDescriptionAreaUpdater;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.Link;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
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
        mCostValue = createDescriptionText(text.getWidth() + mSpace, text.getY(), vertexBufferObjectManager);
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
    public void updateDescription(Shape drawArea, Object objectId,
                                  final String allianceName, final String playerName) {
        super.updateDescription(drawArea, objectId, allianceName, playerName);
        final BuildingId buildingId = (BuildingId) objectId;
        IAlliance alliance = AllianceHolder.getInstance().getElement(allianceName);
        DefenceBuildingDummy dummy = (DefenceBuildingDummy) alliance.getBuildingDummy(buildingId);
        //cost
        mCostValue.setText(Integer.toString(dummy.getCost(buildingId.getUpgrade())));
        //produced unit
        final int unitId = dummy.getOrbitalStationUnitId();
        UnitDummy unitDummy = alliance.getUnitDummy(unitId);
        mProducedUnitLink.setText(EaFallApplication.getContext().getResources().getString(
                unitDummy.getUnitStringId()));
        mProducedUnitLink.setOnClickListener(new StaticHelper.OnClickListener() {
            @Override
            public void onClick() {
                EventBus.getDefault().post(new UnitByBuildingDescriptionShowEvent(buildingId, playerName));
            }
        });
        //building time
        mUnitCreationTimeValue.setText(Integer.toString(dummy.getOrbitalStationCreationTime()));
    }
}
