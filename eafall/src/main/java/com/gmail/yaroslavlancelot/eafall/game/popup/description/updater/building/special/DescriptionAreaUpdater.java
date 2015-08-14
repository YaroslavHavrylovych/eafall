package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.special;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.SpecialBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.DescriptionText;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.BaseDescriptionAreaUpdater;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
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
        mCost = createDescriptionText(text.getWidth() + mSpace, text.getY(), "350", vertexBufferObjectManager);
        // produce
        text = createDescriptionText(1, R.string.description_description, vertexBufferObjectManager);
        mDescription = createDescriptionText(text.getWidth() + mSpace, text.getY(),
                EaFallApplication.getContext().getString(R.string.reptile_city_description), vertexBufferObjectManager);
    }

    @Override
    protected void iniDescriptionTextList() {
        mDescriptionTextList = new ArrayList<Text>(4);
    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId, String allianceName, String playerName) {
        super.updateDescription(drawArea, objectId, allianceName, playerName);
        final BuildingId buildingId = (BuildingId) objectId;
        IAlliance alliance = AllianceHolder.getInstance().getElement(allianceName);
        SpecialBuildingDummy dummy = (SpecialBuildingDummy) alliance.getBuildingDummy(buildingId);
        //cost
        mCost.setText(Integer.toString(dummy.getCost(buildingId.getUpgrade())));
        //description
        mDescription.setText(EaFallApplication.getContext().getString(dummy.getDescriptionStringId()));
    }
}