package com.gmail.yaroslavlancelot.eafall.game.campaign;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.EaFallActivity;
import com.gmail.yaroslavlancelot.eafall.game.audio.BackgroundMusic;
import com.gmail.yaroslavlancelot.eafall.game.audio.LimitedSoundWrapper;
import com.gmail.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.gmail.yaroslavlancelot.eafall.game.campaign.intents.CampaignIntent;
import com.gmail.yaroslavlancelot.eafall.game.campaign.intents.MissionIntent;
import com.gmail.yaroslavlancelot.eafall.game.campaign.intents.StartableIntent;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.CampaignDataLoader;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.CampaignFileLoader;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.ObjectDataLoader;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.PositionLoader;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.mission.MissionDataLoader;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.engine.InstantRotationModifier;
import com.gmail.yaroslavlancelot.eafall.game.engine.MoveByCircleModifier;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.TextButton;
import com.gmail.yaroslavlancelot.eafall.general.SelfCleanable;

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
        if (EaFallApplication.getConfig().isSoundsEnabled()) {
            mSelectSound = SoundFactory.getInstance().loadSound("audio/sound/select.ogg");
        }
        //music
        if (EaFallApplication.getConfig().isMusicEnabled()) {
            mBackgroundMusic = new BackgroundMusic(
                    StringConstants.getMusicPath() + "background_1.ogg",
                    getMusicManager(), CampaignActivity.this);
            mBackgroundMusic.initBackgroundMusic();
        }
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) {
        super.onPopulateScene(scene, onPopulateSceneCallback);
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
        onResourcesLoaded();
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

    private List<Sprite> populateObjects(EaFallScene scene, VertexBufferObjectManager vertexManager) {
        int size = mCampaignFileLoader.getObjectsList().size();
        List<Sprite> elementsList = new ArrayList<>(size);
        LoggerHelper.printVerboseMessage(TAG, "campaign loading objects amount = " + size);
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
            LoggerHelper.printVerboseMessage(TAG, "campaign loading element=" + dataLoader.name);
            PositionLoader position = dataLoader.position;
            Sprite sprite = new Sprite(position.x, position.y,
                    TextureRegionHolder.getRegion(dataLoader.picture), vertexManager);
            sprite.setTag(dataLoader.id);
            if (dataLoader.rotation != null) {
                LoggerHelper.printVerboseMessage(TAG, "campaign element=" + dataLoader.name
                        + ", rotation = " + dataLoader.rotation);
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
        mStartButton = new TextButton(getVertexBufferObjectManager(),
                SizeConstants.CAMPAIGN_START_BUTTON_WIDTH, SizeConstants.CAMPAIGN_START_BUTTON_HEIGHT,
                SizeConstants.CAMPAIGN_START_BUTTON_X, SizeConstants.CAMPAIGN_START_BUTTON_Y);
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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mResourcesLoader.loadSplashImages(getTextureManager(), getVertexBufferObjectManager());
                mSceneManager.initSplashScene();
                mSceneManager.showSplash();
                mCampaignFileName = CampaignIntent.getPathToCampaign(campaignDataLoader.name);
                mResourcesLoader.unloadImages(getTextureManager());
                TextureRegionHolder.getInstance().clear();
                loadResources();
            }
        });
        thread.setDaemon(true);
        thread.start();
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
            LoggerHelper.printErrorMessage(TAG, "File read exception = " + e.getMessage());
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

    private class ElementTouchCallback extends StaticHelper.CustomTouchListener {
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
