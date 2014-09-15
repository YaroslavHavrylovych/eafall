package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.UnitDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.BaseDescriptionUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.RacesHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.buttons.GeneralButton;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.sprite.TouchableSprite;

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
    private GeneralButton mBuildButton;
    /** image for addition information */
    private TouchableSprite mAdditionDescriptionObjectImage;

    public BuildingDescriptionUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mAmountDrawer = new AmountDrawer(vertexBufferObjectManager);
        initBuildButton(vertexBufferObjectManager);
        EventBus.getDefault().register(this);
    }

    private void initBuildButton(VertexBufferObjectManager vertexBufferObjectManager) {
        mBuildButton = new GeneralButton(vertexBufferObjectManager, 200, 70, 10, 10);
        mBuildButton.setText(LocaleImpl.getInstance().getStringById(R.string.build));
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        //amount font
        AmountDrawer.loadFonts(fontManager, textureManager);
        GeneralButton.loadFonts(fontManager, textureManager);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        GeneralButton.loadResources(context, textureManager);
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final BuildingsAmountChangedEvent buildingsAmountChangedEvent) {
        if (!mTeamName.equals(buildingsAmountChangedEvent.getTeamName())
                || mBuildingId != buildingsAmountChangedEvent.getKey()) {
            return;
        }
        mAmountDrawer.setText(buildingsAmountChangedEvent.getNewBuildingsAmount());
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
        mScene.registerTouchArea(mBuildButton);
        IRace race = RacesHolder.getInstance().getElement(raceName);
        return race.getBuildingDummy(objectId).getTextureRegion();
    }

    @Override
    public void clear() {
        super.clear();
        mAmountDrawer.detach();
        mBuildButton.detachSelf(mScene);
        if (mAdditionDescriptionObjectImage != null) {
            mAdditionDescriptionObjectImage.detachSelf(mScene);
            mAdditionDescriptionObjectImage = null;
        }
        mBuildingId = sNoValue;
        mTeamName = "";
    }

    private void updateBuildingsAmount(int buildingsAmount) {
        mAmountDrawer.setText(buildingsAmount);
        mAmountDrawer.draw(mObjectImage);
    }

    @Override
    public void updateDescription(RectangularShape drawArea, int objectId, String raceName, String teamName) {
        drawArea.attachChild(mBuildButton);
        mBuildButton.setOnTouchListener(
                new TouchUtils.CustomTouchListener(mBuildButton.getTouchArea()) {
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
    }

    @Override
    public void updateAdditionInfo(RectangularShape drawArea, final int objectId, String raceName, final String teamName) {
        if (mAdditionDescriptionObjectImage != null) {
            mAdditionDescriptionObjectImage.detachSelf();
        }
        mAdditionDescriptionObjectImage = new TouchableSprite(0, 0, drawArea.getWidth(), drawArea.getHeight(),
                getAdditionalInformationImage(objectId, raceName), mVertexBufferObjectManager);
        drawArea.attachChild(mAdditionDescriptionObjectImage);
        mAdditionDescriptionObjectImage.setOnTouchListener(
                new TouchUtils.CustomTouchListener(mAdditionDescriptionObjectImage.getTouchArea()) {
                    @Override
                    public void click() {
                        super.click();
                        EventBus.getDefault().post(new UnitDescriptionShowEvent(objectId, teamName));
                    }
                });
        mScene.registerTouchArea(mAdditionDescriptionObjectImage);
    }

    protected ITextureRegion getAdditionalInformationImage(int objectId, String raceName) {
        IRace race = RacesHolder.getInstance().getElement(raceName);
        return race.getUnitDummy(objectId).getTextureRegion();
    }
}
