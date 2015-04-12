package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.creep;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.BaseDescriptionAreaUpdater;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.DescriptionText;
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
    private DescriptionText mUpgradeText;
    private Link mProducedUnitLink;
    private Link mUpgradeLink;

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
        // upgrade
        text = mUpgradeText = createDescriptionText(3, R.string.description_upgrade, vertexBufferObjectManager);
        mUpgradeLink = createLink(text.getWidth(), text.getY(), vertexBufferObjectManager);

        // touch
        scene.registerTouchArea(mUpgradeLink);
        scene.registerTouchArea(mProducedUnitLink);
    }

    @Override
    protected void iniDescriptionTextList() {
        mDescriptionTextList = new ArrayList<Text>(8);
        mDescriptionTextLinesAmount = 4;
    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId,
                                  final String raceName, final String teamName) {
        super.updateDescription(drawArea, objectId, raceName, teamName);
        final BuildingId buildingId = (BuildingId) objectId;
        IAlliance race = AllianceHolder.getInstance().getElement(raceName);
        CreepBuildingDummy dummy = (CreepBuildingDummy) race.getBuildingDummy(buildingId);
        //cost
        mCostValue.setText(Integer.toString(dummy.getCost(buildingId.getUpgrade())));
        //produced unit
        final int unitId = dummy.getMovableUnitId(buildingId.getUpgrade());
        UnitDummy unitDummy = race.getUnitDummy(unitId);
        mProducedUnitLink.setText(EaFallApplication.getContext().getResources().getString(
                unitDummy.getUnitStringId()));
        mProducedUnitLink.setOnClickListener(new StaticHelper.OnClickListener() {
            @Override
            public void onClick() {
                EventBus.getDefault().post(new UnitByBuildingDescriptionShowEvent(buildingId, teamName));
            }
        });
        //building time
        mUnitCreationTimeValue.setText(Integer.toString(dummy.getUnitCreationTime(buildingId.getUpgrade())));
        //upgrade
        if (race.isUpgradeAvailable(buildingId)) {
            updateUpgradeCost(buildingId, teamName);
            mUpgradeLink.setOnClickListener(new StaticHelper.OnClickListener() {
                @Override
                public void onClick() {
                    EventBus.getDefault().post(new BuildingDescriptionShowEvent(buildingId.getNextUpgrade(), teamName));
                }
            });
        } else {
            drawArea.detachChild(mUpgradeText);
            drawArea.detachChild(mUpgradeLink);
        }
    }

    public void updateUpgradeCost(BuildingId buildingId, String teamName) {
        IAlliance race = TeamsHolder.getTeam(teamName).getTeamRace();
        int amount = TeamsHolder.getTeam(teamName).getTeamPlanet().getBuildingsAmount(buildingId.getId());
        amount = amount == 0 ? 1 : amount;
        int upgradeCost = amount * race.getUpgradeCost(buildingId);
        mUpgradeLink.setText(Integer.valueOf(upgradeCost).toString());
    }
}
