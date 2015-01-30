package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.unit;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.SpaceInvadersApplication;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.AllianceHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.IAlliance;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dummies.BuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dummies.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dummies.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.UnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.dynamic.MovableUnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.DescriptionText;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.BaseDescriptionAreaUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.text.Link;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
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
    public void updateDescription(RectangularShape drawArea, Object objectId, String raceName, final String teamName) {
        final BuildingId buildingId = (BuildingId) objectId;
        IAlliance race = AllianceHolder.getInstance().getElement(raceName);
        BuildingDummy buildingDummy = race.getBuildingDummy(buildingId);
        int unitId;
        if (buildingDummy instanceof CreepBuildingDummy) {
            unitId = ((CreepBuildingDummy) buildingDummy).getMovableUnitId(buildingId.getUpgrade());
        } else if (buildingDummy instanceof DefenceBuildingDummy) {
            unitId = ((DefenceBuildingDummy) buildingDummy).getOrbitalStationUnitId();
        } else {
            return;
        }

        attach(drawArea);
        UnitDummy unitDummy = race.getUnitDummy(unitId);

        // building name
        mUnitBuildingNameLink.setText(LocaleImpl.getInstance().getStringById(buildingDummy.getStringId()));
        mUnitBuildingNameLink.setOnClickListener(new TouchUtils.OnClickListener() {
            @Override
            public void onClick() {
                EventBus.getDefault().post(new BuildingDescriptionShowEvent(buildingId, teamName));
            }
        });
        // static text
        mUnitHealth.setText("" + unitDummy.getHealth());
        String unitSpeed;
        if (unitDummy instanceof MovableUnitDummy) {
            unitSpeed = "" + ((MovableUnitDummy) unitDummy).getSpeed();
        } else {
            unitSpeed = SpaceInvadersApplication.getContext().getString(R.string.unmovable);
        }
        mUnitSpeed.setText(unitSpeed);
        mUnitReloadTime.setText("" + unitDummy.getReloadTime());
        mUnique.setText("cool unit");
    }
}
