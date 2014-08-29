package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater;

import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.AmountDrawer;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.RacesHolder;

import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** updates buildings */
public class BuildingDescriptionUpdater extends BaseDescriptionUpdater {
    /** basically used for display buildings amount on building image */
    private AmountDrawer mAmountDrawer;

    public BuildingDescriptionUpdater(VertexBufferObjectManager vertexBufferObjectManager) {
        super(vertexBufferObjectManager);
        mAmountDrawer = new AmountDrawer(vertexBufferObjectManager);
    }

    @Override
    public void updateImage(RectangularShape drawArea, int objectId, String raceName, String teamName) {
        super.updateImage(drawArea, objectId, raceName, teamName);
        updateBuildingsAmount(0);
    }

    @Override
    protected ITextureRegion getDescriptionImage(int objectId, String raceName) {
        IRace race = RacesHolder.getInstance().getElement(raceName);
        return race.getBuildingDummy(objectId).getTextureRegion();
    }

    private void updateBuildingsAmount(int buildingsAmount) {
        mAmountDrawer.setText(Integer.toString(buildingsAmount));
        mAmountDrawer.draw(mObjectImage);
    }

    @Override
    public void updateDescription(RectangularShape drawArea, int objectId, String raceName, String teamName) {

    }

    @Override
    public void updateAdditionInfo(RectangularShape drawArea, int objectId, String raceName, String teamName) {

    }

    @Override
    public void clear() {
        mAmountDrawer.detach();
        if (mObjectImage != null)
            mObjectImage.detachSelf();
    }
}
