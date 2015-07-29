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
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.CampaignListLoader;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.PositionLoader;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.mission.MissionDataLoader;
import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.TextButton;

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
    private CampaignListLoader mCampaignListLoader;
    private LimitedSoundWrapper mSelectSound;
    private Sprite mSelectImage;
    private TextButton mStartButton;


    @Override
    public EngineOptions onCreateEngineOptions() {
        mCampaignFileName = getIntent().getExtras().getString(CampaignIntent.CAMPAIGN_FILE_NAME);
        return super.onCreateEngineOptions();
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) {
        super.onPopulateScene(scene, onPopulateSceneCallback);
    }

    @Override
    protected void loadResources() {
        mCampaignListLoader = loadObjects(mCampaignFileName, CampaignListLoader.class);
        //adding resources
        mResourcesLoader.addImage(mCampaignListLoader.background,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT);
        for (CampaignDataLoader dataLoader : mCampaignListLoader.getList()) {
            mResourcesLoader.addImage(dataLoader.picture, dataLoader.width, dataLoader.height);
        }
        mResourcesLoader.addImage(SELECTED_IMAGE,
                SizeConstants.SELECTOR_IMAGE_SIZE, SizeConstants.SELECTOR_IMAGE_SIZE);
        //loading resources
        mResourcesLoader.loadImages(getTextureManager(), getVertexBufferObjectManager());
        mResourcesLoader.loadFonts(getTextureManager(), getFontManager());
        //sound
        if (Config.getConfig().isSoundsEnabled()) {
            mSelectSound = SoundFactory.getInstance().loadSound(mCampaignListLoader.sound_select);
        }
        //music
        if (Config.getConfig().isMusicEnabled()) {
            mBackgroundMusic = new BackgroundMusic(
                    StringConstants.getMusicPath() + mCampaignListLoader.music,
                    getMusicManager(), CampaignActivity.this);
            mBackgroundMusic.initBackgroundMusic();
            mBackgroundMusic.playBackgroundMusic();
        }
        onResourcesLoaded();
    }

    @Override
    protected void unloadResources() {
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();
        hideSplash();
    }

    @Override
    protected void initWorkingScene() {
        mSceneManager.initWorkingScene(mCamera, mCampaignListLoader.parallax_background);
        onPopulateWorkingScene(mSceneManager.getWorkingScene());
    }

    @Override
    protected void onPopulateWorkingScene(final EaFallScene scene) {
        VertexBufferObjectManager objectManager = getVertexBufferObjectManager();
        //background
        scene.setBackground(mCampaignListLoader.background, objectManager);
        //sprites
        List<Sprite> elementsList = new ArrayList<Sprite>(mCampaignListLoader.getList().size());
        for (CampaignDataLoader dataLoader : mCampaignListLoader.getList()) {
            PositionLoader position = dataLoader.position;
            Sprite sprite = new Sprite(position.x, position.y,
                    TextureRegionHolder.getRegion(dataLoader.picture), objectManager);
            sprite.setTag(dataLoader.id);
            elementsList.add(sprite);
            scene.attachChild(sprite);
            sprite.setTouchCallback(new ElementTouchCallback(sprite));
            scene.registerTouchArea(sprite);
        }
        //selected
        mSelectImage = new Sprite(0, 0,
                TextureRegionHolder.getRegion(SELECTED_IMAGE), objectManager);
        mSelectImage.setVisible(false);
        scene.attachChild(mSelectImage);
        /* HUD */
        //start button
        initStartButton();
        //select
        select(elementsList.get(0));
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
        finish();
        StartableIntent campaignIntent = new MissionIntent(missionData);
        campaignIntent.start(CampaignActivity.this);
    }

    private void updateCampaignActivity(final CampaignDataLoader campaignDataLoader) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                unloadResources();
                mResourcesLoader.loadSplashImages(getTextureManager(), getVertexBufferObjectManager());
                mSceneManager.initSplashScene();
                mSceneManager.showSplash();
                mCampaignFileName = CampaignIntent.getPathToCampaign(campaignDataLoader.name);
                loadResources();
            }
        }).start();
    }

    /**
     * search for campaign with given id
     *
     * @param id given campaign id
     * @return {@link CampaignDataLoader} instance or null if no such campaign loaded
     */
    private CampaignDataLoader getCampaign(int id) {
        CampaignDataLoader campaignDataLoader = null;
        for (CampaignDataLoader data : mCampaignListLoader.getList()) {
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
