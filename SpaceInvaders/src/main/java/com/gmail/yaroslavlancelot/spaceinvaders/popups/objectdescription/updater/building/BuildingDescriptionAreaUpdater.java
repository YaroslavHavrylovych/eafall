package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dummies.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.UnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.DescriptionText;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.BaseDescriptionAreaUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.AllianceHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.IAlliance;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.text.Link;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * writes building description on given area (p.s. only description popup). Knows about other
 * buttons (e.g. build button) on the description popup so place text with knowing of this.
 */
public class BuildingDescriptionAreaUpdater extends BaseDescriptionAreaUpdater {
    /* values changed with each #updateDescription call */
    private DescriptionText mCostValue;
    private DescriptionText mUnitCreationTimeValue;
    private DescriptionText mUpgradeText;
    private Link mProducedUnitLink;
    private Link mUpgradeLink;

    public BuildingDescriptionAreaUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        // cost
        Text text = createDescriptionText(0, R.string.description_cost, vertexBufferObjectManager);
        mCostValue = createDescriptionText(text.getWidth() + mSpace, 0, vertexBufferObjectManager);
        // produce
        text = createDescriptionText(1, R.string.description_produce, vertexBufferObjectManager);
        mProducedUnitLink = createLink(text.getWidth(), text.getY(), vertexBufferObjectManager);
        // unit creation time
        text = createDescriptionText(2, R.string.description_unit_producing_time, vertexBufferObjectManager);
        mUnitCreationTimeValue = createDescriptionText(text.getWidth() + mSpace, text.getY(), "8", vertexBufferObjectManager);
        // upgrade
        text = mUpgradeText = createDescriptionText(3, R.string.description_upgrade, vertexBufferObjectManager);
        mUpgradeLink = createLink(text.getWidth(), text.getY(), vertexBufferObjectManager);

        // touch
        scene.registerTouchArea(mProducedUnitLink);
        scene.registerTouchArea(mUpgradeLink);
    }

    /** update description values (e.g. new building appear) */
    @Override
    public void updateDescription(RectangularShape drawArea, Object objectId,
                                  final String raceName, final String teamName) {
        final BuildingId buildingId = (BuildingId) objectId;
        attach(drawArea);
        IAlliance race = AllianceHolder.getInstance().getElement(raceName);
        CreepBuildingDummy dummy = race.getBuildingDummy(buildingId);
        // cost
        mCostValue.setText(Integer.toString(dummy.getCost(buildingId.getUpgrade())));
        // produced unit
        final int unitId = dummy.getUnitId(buildingId.getUpgrade());
        UnitDummy unitDummy = race.getUnitDummy(unitId);
        mProducedUnitLink.setText(unitDummy.getName());
        mProducedUnitLink.setOnClickListener(new TouchUtils.OnClickListener() {
            @Override
            public void onClick() {
                EventBus.getDefault().post(new UnitByBuildingDescriptionShowEvent(buildingId, teamName));
            }
        });
        //upgrade
        if (race.isUpgradeAvailable(buildingId)) {
            updateUpgradeCost(buildingId, teamName);
            mUpgradeLink.setOnClickListener(new TouchUtils.OnClickListener() {
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

    @Override
    protected void iniDescriptionTextList() {
        mDescriptionTextList = new ArrayList<Text>(7);
    }
}
