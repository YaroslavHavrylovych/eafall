package com.yaroslavlancelot.eafall.game.campaign;

import android.content.Context;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.StartableIntent;
import com.yaroslavlancelot.eafall.game.EaFallActivity;
import com.yaroslavlancelot.eafall.game.audio.GeneralSoundKeys;
import com.yaroslavlancelot.eafall.game.audio.LimitedSoundWrapper;
import com.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.yaroslavlancelot.eafall.game.campaign.intents.CampaignIntent;
import com.yaroslavlancelot.eafall.game.campaign.loader.CampaignDataLoader;
import com.yaroslavlancelot.eafall.game.campaign.loader.CampaignFileLoader;
import com.yaroslavlancelot.eafall.game.campaign.loader.ObjectDataLoader;
import com.yaroslavlancelot.eafall.game.campaign.loader.PositionLoader;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.engine.InstantRotationModifier;
import com.yaroslavlancelot.eafall.game.engine.MoveByCircleModifier;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.mission.MissionDataLoader;
import com.yaroslavlancelot.eafall.game.mission.MissionIntent;
import com.yaroslavlancelot.eafall.game.scene.hud.BaseGameHud;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;
import com.yaroslavlancelot.eafall.game.touch.TouchHelper;
import com.yaroslavlancelot.eafall.game.visual.buttons.TextButton;
import com.yaroslavlancelot.eafall.general.SelfCleanable;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.simpleframework.xml.core.Persister;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to display missions list and/or campaigns list.
 * <br/>
 * The main thing is to parse xml with campaign data and correctly display it on the screen.
 *
 * @author Yaroslav Havrylovych
 */
public class CampaignActivity extends EaFallActivity {
    private final static String SELECTED_IMAGE = "graphics/icons/selected.png";
    private final static int NOT_SELECTED = -1;
    private String mCampaignFileName;
    private CampaignFileLoader mCampaignFileLoader;
    private LimitedSoundWrapper mSelectSound;
    private Sprite mSelectImage;
    private TextButton mStartButton;

    @Override
    public EngineOptions onCreateEngineOptions() {
        mCampaignFileName = getIntent().getExtras().getString(CampaignIntent.CAMPAIGN_FILE_NAME);
        return super.onCreateEngineOptions();
    }

    @Override
    public void onCreateResources(final OnCreateResourcesCallback onCreateResourcesCallback) {
        super.onCreateResources(onCreateResourcesCallback);
        // we loading sounds and music here and
        // wouldn't reload this sounds when reloading the campaign screen
        //sound
        mSelectSound = SoundFactory.getInstance().loadSound(GeneralSoundKeys.SELECT);
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) {
        super.onPopulateScene(scene, onPopulateSceneCallback);
    }

    @Override
    protected BaseGameHud createHud() {
        return new BaseGameHud();
    }

    @Override
    protected String createMusicPath() {
        return StringConstants.getMusicPath() + "background_1.ogg";
    }

    @Override
    protected void preResourcesLoading() {
    }

    @Override
    protected void loadResources() {
        mCampaignFileLoader = loadObjects(mCampaignFileName, CampaignFileLoader.class);
        //adding resources
        mResourcesLoader.addImage(mCampaignFileLoader.background,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT);
        for (CampaignDataLoader dataLoader : mCampaignFileLoader.getCampaignsList()) {
            mResourcesLoader.addImage(dataLoader.picture, dataLoader.width, dataLoader.height);
        }
        mResourcesLoader.addImage(SELECTED_IMAGE,
                SizeConstants.SELECTOR_IMAGE_SIZE, SizeConstants.SELECTOR_IMAGE_SIZE);
        for (ObjectDataLoader dataLoader : mCampaignFileLoader.getObjectsList()) {
            mResourcesLoader.addImage(dataLoader.picture, dataLoader.width, dataLoader.height);
        }
        //loading resources
        mResourcesLoader.loadImages(getTextureManager(), getVertexBufferObjectManager());
        mResourcesLoader.loadFonts(getTextureManager(), getFontManager());
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();
        hideSplash();
    }

    @Override
    protected void initWorkingScene() {
        mSceneManager.initWorkingScene(mCamera, mCampaignFileLoader.parallax_background);
        onPopulateWorkingScene(mSceneManager.getWorkingScene());
    }

    @Override
    protected void onPopulateWorkingScene(final EaFallScene scene) {
        VertexBufferObjectManager vertexManager = getVertexBufferObjectManager();
        //background
        scene.setBackground(mCampaignFileLoader.background, vertexManager);
        //populate
        List<Sprite> campaigns = populateCampaigns(scene, vertexManager);
        populateObjects(scene, vertexManager);
        //selected
        mSelectImage = new Sprite(0, 0, TextureRegionHolder.getRegion(SELECTED_IMAGE), vertexManager);
        scene.attachChild(mSelectImage);
        /* HUD */
        //start button
        initStartButton();
        //select
        select(campaigns.get(0));
    }

    @Override
    protected void onShowWorkingScene() {
    }

    @Override
    public String toString() {
        return "campaign activity";
    }

    private List<Sprite> populateObjects(EaFallScene scene, VertexBufferObjectManager vertexManager) {
        int size = mCampaignFileLoader.getObjectsList().size();
        List<Sprite> elementsList = new ArrayList<>(size);
        for (ObjectDataLoader dataLoader : mCampaignFileLoader.getObjectsList()) {
            PositionLoader position = dataLoader.position;
            Sprite sprite = new Sprite(position.x, position.y,
                    TextureRegionHolder.getRegion(dataLoader.picture), vertexManager);
            if (dataLoader.rotation != null) {
                sprite.registerEntityModifier(new InstantRotationModifier(dataLoader.rotation));
            }
            if (dataLoader.radius != null) {
                sprite.registerEntityModifier(
                        new MoveByCircleModifier(dataLoader.duration, dataLoader.radius,
                                position.x.intValue(), position.y.intValue()));
            }
            elementsList.add(sprite);
            scene.attachChild(sprite);
        }
        return elementsList;
    }

    private List<Sprite> populateCampaigns(EaFallScene scene, VertexBufferObjectManager vertexManager) {
        List<Sprite> elementsList = new ArrayList<>(mCampaignFileLoader.getCampaignsList().size());
        for (CampaignDataLoader dataLoader : mCampaignFileLoader.getCampaignsList()) {
            PositionLoader position = dataLoader.position;
            Sprite sprite = new Sprite(position.x, position.y,
                    TextureRegionHolder.getRegion(dataLoader.picture), vertexManager);
            sprite.setTag(dataLoader.id);
            if (dataLoader.rotation != null) {
                sprite.registerEntityModifier(new InstantRotationModifier(dataLoader.rotation));
            }
            elementsList.add(sprite);
            scene.attachChild(sprite);
            sprite.setTouchCallback(new ElementTouchCallback(sprite));
            scene.registerTouchArea(sprite);
        }
        return elementsList;
    }

    private void initStartButton() {
        mStartButton = new TextButton(
                SizeConstants.CAMPAIGN_START_BUTTON_X, SizeConstants.CAMPAIGN_START_BUTTON_Y,
                SizeConstants.CAMPAIGN_START_BUTTON_WIDTH, SizeConstants.CAMPAIGN_START_BUTTON_HEIGHT,
                getVertexBufferObjectManager());
        mStartButton.setVisible(false);
        mHud.attachChild(mStartButton);
        mStartButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(final ButtonSprite pButtonSprite, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                CampaignDataLoader campaignDataLoader = getCampaign(mSelectImage.getTag());
                if (campaignDataLoader.isMission()) {
                    startMission(campaignDataLoader.mission);
                } else {
                    updateCampaignActivity(campaignDataLoader);
                }
            }
        });
    }

    private void startMission(final MissionDataLoader missionData) {
        mResourcesLoader.unloadImages(getTextureManager());
        SelfCleanable.clearMemory();
        finish();
        StartableIntent campaignIntent = new MissionIntent(missionData);
        campaignIntent.start(CampaignActivity.this);
    }

    private void updateCampaignActivity(final CampaignDataLoader campaignDataLoader) {
        startAsyncResourceLoading(new Runnable() {
            @Override
            public void run() {
                mResourcesLoader.loadSplashImages(getTextureManager(), getVertexBufferObjectManager());
                mSceneManager.initSplashScene();
                mSceneManager.showSplash();
                mCampaignFileName = CampaignIntent.getPathToCampaign(campaignDataLoader.name);
                mResourcesLoader.unloadImages(getTextureManager());
                TextureRegionHolder.getInstance().clear();
            }
        });
    }

    /**
     * search for campaign with given id
     *
     * @param id given campaign id
     * @return {@link CampaignDataLoader} instance or null if no such campaign loaded
     */
    private CampaignDataLoader getCampaign(int id) {
        CampaignDataLoader campaignDataLoader = null;
        for (CampaignDataLoader data : mCampaignFileLoader.getCampaignsList()) {
            if (data.id == id) {
                campaignDataLoader = data;
                break;
            }
        }
        return campaignDataLoader;
    }

    private <T> T loadObjects(String path, Class<T> cls) {
        T ret = null;
        Context context = EaFallApplication.getContext();
        try {
            ret = new Persister().read(cls, context.getAssets().open(path));
        } catch (Exception e) {
            //TODO logger was here
        }
        return ret;
    }

    private void click(IEntity entity) {
        SoundFactory.getInstance().playSound(mSelectSound);
        select(entity);
    }

    private void select(IEntity entity) {
        mSelectImage.setPosition(entity);
        mSelectImage.setVisible(true);
        mSelectImage.setTag(entity.getTag());
        //start button
        mStartButton.setVisible(true);
        CampaignDataLoader data = getCampaign(mSelectImage.getTag());
        String text;
        if (!data.isMission()) {
            text = getString(R.string.campaign_guide_enter);
        } else {
            text = getString(R.string.campaign_guide_start);
        }
        mStartButton.setText(text);
        mHud.registerTouchArea(mStartButton);
    }

    private void unselect(IEntity entity) {
        mSelectImage.setPosition(entity);
        mSelectImage.setTag(NOT_SELECTED);
        mSelectImage.setVisible(false);
        mStartButton.setVisible(false);
        mHud.unregisterTouchArea(mStartButton);
    }

    private class ElementTouchCallback extends TouchHelper.EntityCustomTouch {
        public ElementTouchCallback(IEntity object) {
            super(object);
        }

        @Override
        public void click() {
            super.click();
            CampaignActivity.this.click(mObject);
        }
    }
}
