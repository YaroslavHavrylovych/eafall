package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building.special;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.SpaceInvadersApplication;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.AllianceHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.IAlliance;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dummies.SpecialBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dummies.WealthBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.DescriptionText;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.BaseDescriptionAreaUpdater;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/** wealth building description area */
public class DescriptionAreaUpdater extends BaseDescriptionAreaUpdater {
    /** wealth building cost value */
    private DescriptionText mCost;
    /** wealth building description */
    private DescriptionText mDescription;

    public DescriptionAreaUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        // cost
        Text text = createDescriptionText(0, R.string.description_cost, vertexBufferObjectManager);
        mCost = createDescriptionText(text.getWidth() + mSpace, 0, "350", vertexBufferObjectManager);
        // produce
        text = createDescriptionText(1, R.string.description_description, vertexBufferObjectManager);
        mDescription = createDescriptionText(text.getWidth() + mSpace, text.getY(),
                SpaceInvadersApplication.getContext().getString(R.string.reptile_city_description), vertexBufferObjectManager);
    }

    @Override
    protected void iniDescriptionTextList() {
        mDescriptionTextList = new ArrayList<Text>(4);
    }

    @Override
    public void updateDescription(RectangularShape drawArea, Object objectId, String raceName, String teamName) {
        super.updateDescription(drawArea, objectId, raceName, teamName);
        final BuildingId buildingId = (BuildingId) objectId;
        IAlliance race = AllianceHolder.getInstance().getElement(raceName);
        SpecialBuildingDummy dummy = (SpecialBuildingDummy) race.getBuildingDummy(buildingId);
        //cost
        mCost.setText(Integer.toString(dummy.getCost(buildingId.getUpgrade())));
        //description
        mDescription.setText(SpaceInvadersApplication.getContext().getString(dummy.getDescriptionStringId()));
    }
}
