package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.unit;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.MovableUnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.DescriptionText;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.BaseDescriptionAreaUpdater;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.Link;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/** Update unit description (and only description area) on given area. */
public class DescriptionAreaUpdater extends BaseDescriptionAreaUpdater {
    /* values changed with each #updateDescription call */
    private Link mUnitBuildingNameLink;
    private DescriptionText mUnitSpeed;
    private DescriptionText mUnitHealth;
    private DescriptionText mUnitReloadTime;
    private DescriptionText mUnique;


    public DescriptionAreaUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        // unit building
        Text text = createDescriptionText(0, R.string.description_building, vertexBufferObjectManager);
        mUnitBuildingNameLink = createLink(text.getWidth(), text.getY(), vertexBufferObjectManager);
        // unit health
        text = createDescriptionText(1, R.string.description_health, vertexBufferObjectManager);
        mUnitHealth = createDescriptionText(text.getWidth() + mSpace, text.getY(), vertexBufferObjectManager);
        // unit speed
        text = createDescriptionText(2, R.string.description_speed, vertexBufferObjectManager);
        mUnitSpeed = createDescriptionText(text.getWidth() + mSpace, text.getY(), vertexBufferObjectManager);
        // unit reload time
        text = createDescriptionText(3, R.string.description_reload_time, vertexBufferObjectManager);
        mUnitReloadTime = createDescriptionText(text.getWidth() + mSpace, text.getY(), vertexBufferObjectManager);
        // unique
        text = createDescriptionText(4, R.string.description_unique, vertexBufferObjectManager);
        mUnique = createDescriptionText(text.getWidth() + mSpace, text.getY(), vertexBufferObjectManager);

        // touch
        scene.registerTouchArea(mUnitBuildingNameLink);
    }

    @Override
    protected void iniDescriptionTextList() {
        mDescriptionTextList = new ArrayList<Text>(10);
    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId, String allianceName, final String playerName) {
        final BuildingId buildingId = (BuildingId) objectId;
        IAlliance alliance = AllianceHolder.getInstance().getElement(allianceName);
        BuildingDummy buildingDummy = alliance.getBuildingDummy(buildingId);
        int unitId;
        if (buildingDummy instanceof CreepBuildingDummy) {
            unitId = ((CreepBuildingDummy) buildingDummy).getMovableUnitId(buildingId.getUpgrade());
        } else if (buildingDummy instanceof DefenceBuildingDummy) {
            unitId = ((DefenceBuildingDummy) buildingDummy).getOrbitalStationUnitId();
        } else {
            return;
        }

        attach(drawArea);
        UnitDummy unitDummy = alliance.getUnitDummy(unitId);

        // building name
        mUnitBuildingNameLink.setText(LocaleImpl.getInstance().getStringById(buildingDummy.getStringId()));
        mUnitBuildingNameLink.setOnClickListener(new StaticHelper.OnClickListener() {
            @Override
            public void onClick() {
                EventBus.getDefault().post(new BuildingDescriptionShowEvent(buildingId, playerName));
            }
        });
        // static text
        mUnitHealth.setText("" + unitDummy.getHealth());
        String unitSpeed;
        if (unitDummy instanceof MovableUnitDummy) {
            unitSpeed = "" + ((MovableUnitDummy) unitDummy).getSpeed();
        } else {
            unitSpeed = EaFallApplication.getContext().getString(R.string.unmovable);
        }
        mUnitSpeed.setText(unitSpeed);
        mUnitReloadTime.setText("" + unitDummy.getReloadTime());
        mUnique.setText("cool unit");
    }
}
