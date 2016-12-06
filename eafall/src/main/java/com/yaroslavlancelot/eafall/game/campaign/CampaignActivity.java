package com.yaroslavlancelot.eafall.game.campaign;

import android.content.Context;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.StartableIntent;
import com.yaroslavlancelot.eafall.game.EaFallActivity;
import com.yaroslavlancelot.eafall.game.ai.IBot;
import com.yaroslavlancelot.eafall.game.campaign.intents.CampaignIntent;
import com.yaroslavlancelot.eafall.game.campaign.loader.CampaignDataLoader;
import com.yaroslavlancelot.eafall.game.campaign.loader.CampaignFileLoader;
import com.yaroslavlancelot.eafall.game.campaign.loader.ObjectDataLoader;
import com.yaroslavlancelot.eafall.game.campaign.loader.PositionLoader;
import com.yaroslavlancelot.eafall.game.campaign.pass.CampaignPassage;
import com.yaroslavlancelot.eafall.game.campaign.pass.CampaignPassageFactory;
import com.yaroslavlancelot.eafall.game.campaign.plot.PlotPresenter;
import com.yaroslavlancelot.eafall.game.campaign.visual.CampaignTitleText;
import com.yaroslavlancelot.eafall.game.campaign.visual.NextButton;
import com.yaroslavlancelot.eafall.game.client.thick.single.SinglePlayerGameActivity;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.engine.BackwardInstantRotationModifier;
import com.yaroslavlancelot.eafall.game.engine.InstantRotationModifier;
import com.yaroslavlancelot.eafall.game.engine.MoveByCircleModifier;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.setlectable.selector.SelectorFactory;
import com.yaroslavlancelot.eafall.game.mission.CampaignMissionIntent;
import com.yaroslavlancelot.eafall.game.mission.MissionDetailsLoader;
import com.yaroslavlancelot.eafall.game.scene.hud.BaseGameHud;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;
import com.yaroslavlancelot.eafall.game.visual.buttons.BackButton;
import com.yaroslavlancelot.eafall.game.visual.buttons.TextButton;
import com.yaroslavlancelot.eafall.general.SelfCleanable;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.simpleframework.xml.core.Persister;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Used to display missions list and/or campaigns list.
 * <br/>
 * The main thing is to parse xml with campaign data and correctly display it on the screen.
 *
 * @author Yaroslav Havrylovych
 */
public class CampaignActivity extends EaFallActivity {
    private String mCampaignFileName;
    private CampaignFileLoader mCampaignFileLoader;
    private TextButton mStartButton;
    private BackButton mBackButton;
    private Text mTitleText;
    private int mScreenId;
    private CampaignPassage mCampaignPassage;
    private Sprite mMissionLogo;
    private NextButton mNextScreenButton;
    private NextButton mPreviousScreenButton;

    @Override
    public EngineOptions onCreateEngineOptions() {
        mCampaignFileName = getIntent().getExtras().getString(CampaignIntent.CAMPAIGN_FILE_NAME_KEY);
        initCampaignData();
        EngineOptions engineOptions = super.onCreateEngineOptions();
        mCamera.setBoundsEnabled(false);
        return engineOptions;
    }

    @Override
    public void onCreateResources(final OnCreateResourcesCallback onCreateResourcesCallback) {
        super.onCreateResources(onCreateResourcesCallback);
        // we loading sounds and music here and
        // wouldn't reload this sounds when reloading the campaign screen
        //sound
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
        mCampaignFileLoader.init(mCampaignFileName);
        //load background
        mResourcesLoader.addImage(mCampaignFileLoader.background,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT);
        //load campaign objects (planets and additional elements)
        for (CampaignDataLoader dataLoader : mCampaignFileLoader.getCampaignsList()) {
            mResourcesLoader.addImage(dataLoader.picture, dataLoader.width, dataLoader.height);
        }
        for (ObjectDataLoader dataLoader : mCampaignFileLoader.getObjectsList()) {
            mResourcesLoader.addImage(dataLoader.picture, dataLoader.width, dataLoader.height);
        }
        mResourcesLoader.addImage(StringConstants.FILE_LOGO, 170, 170);
        BackButton.loadImages(getTextureManager());
        NextButton.loadImages(getTextureManager());
        //loading resources
        mResourcesLoader.loadImages(getTextureManager(), getVertexBufferObjectManager());
        mResourcesLoader.loadFonts(getTextureManager(), getFontManager());
        SelectorFactory.getSelector().block(); //we  don't need selector in the campaign
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
        initHud();
        mScreenId = mCampaignPassage.getPassedCampaignsAmount();
        updateScreen();
    }

    private void initCampaignData() {
        mCampaignPassage = CampaignPassageFactory.getInstance()
                .getCampaignPassage(mCampaignFileName, getApplicationContext());
        mScreenId = 0;
        if (getIntent().getExtras().getBoolean(CampaignIntent.GAME_RESULT_SUCCESS_KEY, false)) {
            getIntent().getExtras().remove(CampaignIntent.GAME_RESULT_SUCCESS_KEY);
            int missionId = getIntent().getExtras().getInt(CampaignIntent.CAMPAIGN_MISSION_ID_KEY);
            if (missionId == mCampaignPassage.getPassedCampaignsAmount()) {
                mCampaignPassage.markNewCampaignPassed();
            }
        }
    }

    @Override
    protected void onPopulateWorkingScene(final EaFallScene scene) {
        VertexBufferObjectManager vertexManager = getVertexBufferObjectManager();
        //background
        scene.setBackground(mCampaignFileLoader.background, vertexManager);
        //populate
        populateCampaigns(scene, vertexManager);
        populateObjects(scene, vertexManager);
    }

    private void initHud() {
        mHud.attachChild(new Sprite(SizeConstants.HALF_FIELD_WIDTH, SizeConstants.HALF_FIELD_HEIGHT,
                TextureRegionHolder.getRegion(StringConstants.FILE_CAMPAIGN_HUD_FOREGROUND), getVertexBufferObjectManager()));
        mTitleText = new CampaignTitleText(320, 1006, getVertexBufferObjectManager());
        mHud.attachChild(mTitleText);
        mStartButton = new TextButton(
                SizeConstants.CAMPAIGN_START_BUTTON_X, SizeConstants.CAMPAIGN_START_BUTTON_Y,
                SizeConstants.CAMPAIGN_START_BUTTON_WIDTH, SizeConstants.CAMPAIGN_START_BUTTON_HEIGHT,
                getVertexBufferObjectManager());
        mStartButton.setFixedSize(true);
        mBackButton = new BackButton(SizeConstants.CAMPAIGN_BACK_BUTTON_X, SizeConstants.CAMPAIGN_BACK_BUTTON_Y,
                getVertexBufferObjectManager());
        mHud.attachChild(mBackButton);
        mHud.registerTouchArea(mBackButton);
        mMissionLogo = new Sprite(153, 945, TextureRegionHolder.getRegion(StringConstants.FILE_LOGO), getVertexBufferObjectManager());
        mHud.attachChild(mMissionLogo);
        mNextScreenButton = new NextButton(SizeConstants.CAMPAIGN_NEXT_RIGHT_BUTTON_X, SizeConstants.CAMPAIGN_NEXT_BUTTON_Y,
                getVertexBufferObjectManager(), true);
        mPreviousScreenButton = new NextButton(SizeConstants.CAMPAIGN_NEXT_LEFT_BUTTON_X, SizeConstants.CAMPAIGN_NEXT_BUTTON_Y,
                getVertexBufferObjectManager(), false);
        mHud.attachChild(mNextScreenButton);
        mHud.attachChild(mPreviousScreenButton);
        mHud.registerTouchArea(mNextScreenButton);
        mHud.registerTouchArea(mPreviousScreenButton);
        initNextPreviousButtonsListeners();
        mBackButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                CampaignActivity.this.finish();
            }
        });
        initStartButton();
        updateHudValues();
    }

    private void initNextPreviousButtonsListeners() {
        mNextScreenButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                mScreenId += 1;
                updateScreen();
            }
        });
        mPreviousScreenButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                mScreenId -= 1;
                updateScreen();
            }
        });
    }

    private void updateScreen() {
        int amountOfMissions = mCampaignFileLoader.getCampaignsList().size();
        mScreenId = mScreenId < amountOfMissions ? mScreenId : amountOfMissions - 1;
        mCamera.setCenter(SizeConstants.HALF_FIELD_WIDTH + mScreenId * SizeConstants.GAME_FIELD_WIDTH,
                SizeConstants.HALF_FIELD_HEIGHT);
        mPreviousScreenButton.setEnabled(mScreenId > 0);
        mNextScreenButton.setEnabled(mScreenId < mCampaignFileLoader.getCampaignsList().size() - 1);
        mStartButton.setVisible(mScreenId <= mCampaignPassage.getPassedCampaignsAmount());
        String title = getString(getResources().getIdentifier(
                mCampaignFileLoader.getCampaignsList().get(mScreenId).name, "string",
                getApplicationInfo().packageName));
        Timber.v("Campaign title is [%s]", title);
        mTitleText.setText(title);
    }

    private void updateHudValues() {
        mStartButton.setText(R.string.campaign_guide_start);
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
            Sprite sprite = new Sprite(
                    position.x + dataLoader.screen * SizeConstants.GAME_FIELD_WIDTH, position.y,
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
            Sprite sprite = new Sprite(
                    position.x + dataLoader.screen * SizeConstants.GAME_FIELD_WIDTH, position.y,
                    TextureRegionHolder.getRegion(dataLoader.picture), vertexManager);
            sprite.setTag(dataLoader.screen);
            if (dataLoader.rotation != null) {
                sprite.registerEntityModifier(new BackwardInstantRotationModifier(dataLoader.rotation));
            }
            elementsList.add(sprite);
            scene.attachChild(sprite);
            scene.registerTouchArea(sprite);
        }
        return elementsList;
    }

    private void initStartButton() {
        mHud.attachChild(mStartButton);
        mStartButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
                                float pTouchAreaLocalY) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int plot = getResources().getIdentifier(
                                "campaign_demo_plot" + (mScreenId + 1), "string",
                                getApplicationInfo().packageName);
                        PlotPresenter plotPresenter = new PlotPresenter(plot, CampaignActivity.this,
                                new PlotPresenter.PlotPresentedCallback() {
                                    @Override
                                    public void onPresentationDone() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                startMission(getCampaign(mScreenId).mission);
                                                finish();
                                            }
                                        });
                                    }
                                });
                        plotPresenter.startPresentation();
                    }
                });
            }
        });
        mHud.registerTouchArea(mStartButton);
    }

    private void startMission(final MissionDetailsLoader missionData) {
        mResourcesLoader.unloadImages(getTextureManager());
        SelfCleanable.clearMemory();
        StartableIntent campaignIntent = new CampaignMissionIntent(getMissionActivity(missionData),
                missionData, mCampaignFileName, mScreenId);
        campaignIntent.start(this);
    }

    private Class<? extends SinglePlayerGameActivity> getMissionActivity(MissionDetailsLoader missionData) {
        try {
            return (Class<? extends SinglePlayerGameActivity>) Class.forName(missionData.game_handler.trim());
        } catch (ClassNotFoundException e) {
            Timber.e(e, "Can't find class [%s]", missionData.game_handler);
        } catch (ClassCastException e) {
            Timber.e(e, "Can't case class [%s]", missionData.game_handler);
        }
        return SinglePlayerGameActivity.class;
    }

    /**
     * search for campaign with given screen
     *
     * @param id given campaign screen
     * @return {@link CampaignDataLoader} instance or null if no such campaign loaded
     */
    private CampaignDataLoader getCampaign(int id) {
        CampaignDataLoader campaignDataLoader = null;
        for (CampaignDataLoader data : mCampaignFileLoader.getCampaignsList()) {
            if (data.screen == id) {
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
            Timber.e(e, "can't load objects in campaign");
        }
        return ret;
    }
}
