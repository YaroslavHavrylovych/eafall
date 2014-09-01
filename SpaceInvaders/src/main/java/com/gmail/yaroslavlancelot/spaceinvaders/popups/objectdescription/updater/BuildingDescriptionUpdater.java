package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.RacesHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.buttons.GameButton;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/** updates buildings */
public class BuildingDescriptionUpdater extends BaseDescriptionUpdater {
    private static final int sNoValue = Integer.MIN_VALUE;
    private volatile int mBuildingId = sNoValue;
    private volatile String mTeamName = "";
    /** basically used for display buildings amount on building image */
    private AmountDrawer mAmountDrawer;
    private GameButton mBuildButton;
    private Scene mScene;

    public BuildingDescriptionUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager);
        mScene = scene;
        mAmountDrawer = new AmountDrawer(vertexBufferObjectManager);
        initBuildButton(vertexBufferObjectManager);
    }

    private void initBuildButton(VertexBufferObjectManager vertexBufferObjectManager) {
        mBuildButton = new GameButton(vertexBufferObjectManager, 200, 70, 10, 10);
        mBuildButton.setText(LocaleImpl.getInstance().getStringById(R.string.build));
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        //amount font
        AmountDrawer.loadFonts(fontManager, textureManager);
        GameButton.loadFonts(fontManager, textureManager);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        GameButton.loadResources(context, textureManager);
    }

    @Override
    public void updateImage(RectangularShape drawArea, int objectId, String raceName, String teamName) {
        super.updateImage(drawArea, objectId, raceName, teamName);
        ITeam team = TeamsHolder.getInstance().getElement(teamName);
        updateBuildingsAmount(team.getTeamPlanet().getBuildingAmount(objectId));
        mBuildingId = objectId;
        mTeamName = teamName;
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
        drawArea.attachChild(mBuildButton);
    }

    @Override
    public void updateAdditionInfo(RectangularShape drawArea, int objectId, String raceName, String teamName) {

    }

    @Override
    public void clear() {
        mAmountDrawer.detach();
        mBuildButton.detachSelf();
        if (mObjectImage != null) {
            mObjectImage.detachSelf();
        }
        mBuildingId = sNoValue;
        mTeamName = "";
    }

    @Override
    public void initDescriptionArea(float offsetX, float offsetY) {
        mBuildButton.setOnTouchListener(
                new TouchUtils.CustomTouchListener(mBuildButton.getTouchArea(offsetX, offsetY)) {
                    @Override
                    public void press() {
                        mBuildButton.press();
                    }

                    @Override
                    public void click() {
                        unPress();
                        EventBus.getDefault().post(new CreateBuildingEvent(mTeamName, mBuildingId));
                    }

                    @Override
                    public void unPress() {
                        mBuildButton.unpress();
                    }
                });
        mScene.registerTouchArea(mBuildButton);
        mScene = null;
    }
}
