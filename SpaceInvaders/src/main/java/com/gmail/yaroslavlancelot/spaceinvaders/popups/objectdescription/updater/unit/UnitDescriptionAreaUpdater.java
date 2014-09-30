package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.unit;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.UnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.DescriptionText;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.BaseDescriptionAreaUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.RacesHolder;
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
public class UnitDescriptionAreaUpdater extends BaseDescriptionAreaUpdater {
    /* values changed with each #updateDescription call */
    private Link mUnitBuildingNameLink;
    private DescriptionText mUnitSpeed;
    private DescriptionText mUnitHealth;
    private DescriptionText mUnitReloadTime;
    private DescriptionText mUnique;


    public UnitDescriptionAreaUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
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
    public void updateDescription(RectangularShape drawArea, final int objectId, String raceName, final String teamName) {
        attach(drawArea);
        IRace race = RacesHolder.getInstance().getElement(raceName);
        UnitDummy dummy = race.getUnitDummy(objectId);
        // building name
        CreepBuildingDummy buildingDummy = race.getBuildingDummy(objectId);
        mUnitBuildingNameLink.setText(LocaleImpl.getInstance().getStringById(buildingDummy.getNameId()));
        mUnitBuildingNameLink.setOnClickListener(new TouchUtils.OnClickListener() {
            @Override
            public void onClick() {
                EventBus.getDefault().post(new BuildingDescriptionShowEvent(objectId, teamName));
            }
        });
        // static text
        mUnitHealth.setText("" + dummy.getHealth());
        mUnitSpeed.setText("200");
        mUnitReloadTime.setText("1.5");
        mUnique.setText("cool unit");
    }

    @Override
    protected void iniDescriptionTextList() {
        mDescriptionTextList = new ArrayList<Text>(10);
    }
}
