package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.UnitDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.BaseDescriptionPopupUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.RacesHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.buttons.TextButton;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import de.greenrobot.event.EventBus;

/** updates buildings description popup */
public class BuildingDescriptionPopupUpdater extends BaseDescriptionPopupUpdater {
    private static final int sNoValue = Integer.MIN_VALUE;
    private volatile int mBuildingId = sNoValue;
    private volatile String mTeamName = "";
    /** basically used for display buildings amount on building image */
    private AmountDrawer mAmountDrawer;
    /** press to create a building */
    private TextButton mBuildButton;
    /** image for addition information */
    private Sprite mAdditionDescriptionImage;
    /** building description object (update description area which u pass to it) */
    private DescriptionAreaUpdater mDescriptionAreaUpdater;
    /** used only for intercept touch event on addition information area */
    private Rectangle mAdditionInfoRectangle;

    public BuildingDescriptionPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mAmountDrawer = new AmountDrawer(vertexBufferObjectManager);
        mDescriptionAreaUpdater = new BuildingDescriptionAreaUpdater(vertexBufferObjectManager, scene);
        initBuildButton(vertexBufferObjectManager);
        initAdditionInformationArea();
        EventBus.getDefault().register(this);
    }

    private void initBuildButton(VertexBufferObjectManager vertexBufferObjectManager) {
        mBuildButton = new TextButton(vertexBufferObjectManager, 200, 70);
        mBuildButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_build));
        mScene.registerTouchArea(mBuildButton);
    }

    private void initAdditionInformationArea() {
        mAdditionInfoRectangle = new Rectangle(0, 0, 10, 10, mVertexBufferObjectManager);
        mAdditionInfoRectangle.setColor(Color.TRANSPARENT);
        mScene.registerTouchArea(mAdditionInfoRectangle);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        //amount font
        AmountDrawer.loadFonts(fontManager, textureManager);
        TextButton.loadFonts(fontManager, textureManager);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        TextButton.loadResources(context, textureManager);
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
    protected String getDescribedObjectName(int objectId, String raceName) {
        return LocaleImpl.getInstance().getStringById
                (RacesHolder.getInstance().getElement(raceName).getBuildingDummy(objectId).getNameId());
    }

    @Override
    public void clear() {
        super.clear();
        mAmountDrawer.detach();
        mBuildButton.detachSelf();
        if (mAdditionDescriptionImage != null) {
            mAdditionDescriptionImage.detachSelf(mScene);
            mAdditionDescriptionImage = null;
            mAdditionInfoRectangle.detachSelf();
        }
        mDescriptionAreaUpdater.clearDescription();
        mBuildingId = sNoValue;
        mTeamName = "";
    }

    @Override
    protected ITextureRegion getDescriptionImage(int objectId, String raceName) {
        IRace race = RacesHolder.getInstance().getElement(raceName);
        return race.getBuildingDummy(objectId).getTextureRegion();
    }

    private void updateBuildingsAmount(int buildingsAmount) {
        mAmountDrawer.setText(buildingsAmount);
        mAmountDrawer.draw(mObjectImage);
    }

    @Override
    public void updateDescription(RectangularShape drawArea, int objectId, String raceName, String teamName) {
        //description
        mDescriptionAreaUpdater.updateDescription(drawArea, objectId, raceName, teamName);
        //buttons
        mBuildButton.setPosition(0, drawArea.getHeight() - mBuildButton.getHeight());
        drawArea.attachChild(mBuildButton);
        mBuildButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                EventBus.getDefault().post(new CreateBuildingEvent(mTeamName, mBuildingId));
            }
        });
    }

    @Override
    public void updateAdditionInfo(RectangularShape drawArea, final int objectId, String raceName, final String teamName) {
        if (mAdditionDescriptionImage != null) {
            mAdditionDescriptionImage.detachSelf();
            mAdditionInfoRectangle.detachSelf();
        }
        mAdditionDescriptionImage = new Sprite(0, 0, drawArea.getWidth(), drawArea.getHeight(),
                getAdditionalInformationImage(objectId, raceName), mVertexBufferObjectManager);

        mAdditionInfoRectangle.setWidth(drawArea.getWidth());
        mAdditionInfoRectangle.setHeight(drawArea.getHeight());
        mAdditionInfoRectangle.setTouchCallback(
                new TouchUtils.CustomTouchListener(mAdditionDescriptionImage) {
                    @Override
                    public void click() {
                        super.click();
                        EventBus.getDefault().post(new UnitDescriptionShowEvent(objectId, teamName));
                    }
                });
        mAdditionInfoRectangle.attachChild(mAdditionDescriptionImage);
        drawArea.attachChild(mAdditionInfoRectangle);
    }

    protected ITextureRegion getAdditionalInformationImage(int objectId, String raceName) {
        IRace race = RacesHolder.getInstance().getElement(raceName);
        return race.getUnitDummy(objectId).getTextureRegion();
    }
}
