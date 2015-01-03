package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building.wealth;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.BaseDescriptionAreaUpdater;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/** wealth building description area */
public class DescriptionAreaUpdater extends BaseDescriptionAreaUpdater {

    public DescriptionAreaUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        // cost
        Text text = createDescriptionText(0, R.string.description_cost, vertexBufferObjectManager);
        //TODO change
        createDescriptionText(text.getWidth() + mSpace, 0, "350", vertexBufferObjectManager);
        // produce
        text = createDescriptionText(1, R.string.description_produce, vertexBufferObjectManager);
        createDescriptionText(text.getWidth() + mSpace, 0, "description", vertexBufferObjectManager);
    }

    @Override
    protected void iniDescriptionTextList() {
        mDescriptionTextList = new ArrayList<Text>(4);
    }

    @Override
    public void updateDescription(RectangularShape drawArea, Object objectId, String raceName, String teamName) {
        super.updateDescription(drawArea, objectId, raceName, teamName);
    }
}
