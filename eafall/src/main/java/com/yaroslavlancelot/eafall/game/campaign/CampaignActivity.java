package com.yaroslavlancelot.eafall.game.campaign;

import android.content.Context;
import android.widget.Toast;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.StartableIntent;
import com.yaroslavlancelot.eafall.android.utils.music.Music;
import com.yaroslavlancelot.eafall.game.EaFallActivity;
import com.yaroslavlancelot.eafall.game.camera.EaFallCamera;
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
import com.yaroslavlancelot.eafall.game.mission.CampaignFindPathMissionIntent;
import com.yaroslavlancelot.eafall.game.mission.CampaignMissionIntent;
import com.yaroslavlancelot.eafall.game.mission.MissionDetailsLoader;
import com.yaroslavlancelot.eafall.game.scene.hud.BaseGameHud;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;
import com.yaroslavlancelot.eafall.game.touch.TouchHelper;
import com.yaroslavlancelot.eafall.game.visual.buttons.BackButton;
import com.yaroslavlancelot.eafall.game.visual.buttons.TextButton;
import com.yaroslavlancelot.eafall.general.SelfCleanable;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
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
    public void onPauseGame() {
        super.onPauseGame();
        mBackgroundMusic.stopPlaying();
    }

    @Override
    public void onResumeGame() {
        super.onResumeGame();
        mBackgroundMusic.startPlaying(Music.MusicType.CAMPAIGN);
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
        mScreenId = mCampaignPassage.getLastPlayedMission();
        updateScreen(false);
        mSceneManager.getWorkingScene().setOnSceneTouchListener(
                new TouchHelper.SceneTouchListener());
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
                getVertexBufferObjectManager(), true) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                return onNextAdditionalTouch(mNextScreenButton, pSceneTouchEvent)
                        || super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
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
                updateScreen(true);
            }
        });
        mPreviousScreenButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                mScreenId -= 1;
                updateScreen(true);
            }
        });
    }

    private void updateScreen(boolean smooth) {
        int amountOfMissions = mCampaignFileLoader.getCampaignsList().size();
        mScreenId = mScreenId < amountOfMissions ? mScreenId : amountOfMissions - 1;
        final float x = SizeConstants.HALF_FIELD_WIDTH + mScreenId * SizeConstants.GAME_FIELD_WIDTH;
        float y = SizeConstants.HALF_FIELD_HEIGHT;
        if (smooth) {
            mNextScreenButton.setEnabled(false);
            mPreviousScreenButton.setEnabled(false);
            mStartButton.setEnabled(false);
            mCamera.setCenterChangedCallback(new EaFallCamera.ICameraMoveCallbacks() {
                @Override
                public void cameraMove(float deltaX, float deltaY) {
                    if (mCamera.getCenterX() == x) {
                        updateMissionButtons();
                    }
                }
            });
            mCamera.setCenter(x, y);
        } else {
            mCamera.setCenterDirect(x, y);
            updateMissionButtons();
        }
        String title = getString(getResources().getIdentifier(
                mCampaignFileLoader.getCampaignsList().get(mScreenId).name, "string",
                getApplicationInfo().packageName));
        Timber.v("Campaign title is [%s]", title);
        mTitleText.setText(title);
    }

    private void updateMissionButtons() {
        mPreviousScreenButton.setVisible(mScreenId > 0);
        mPreviousScreenButton.setEnabled(true);
        mNextScreenButton.setVisible(mScreenId < mCampaignFileLoader.getCampaignsList().size() - 1);
        mNextScreenButton.setEnabled(mScreenId < mCampaignFileLoader.getCampaignsList().size() - 1
                && mScreenId < mCampaignPassage.getPassedCampaignsAmount());
        mStartButton.setVisible(mScreenId <= mCampaignPassage.getPassedCampaignsAmount());
        mStartButton.setEnabled(true);
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
                                (int) sprite.getX(), (int) sprite.getY()));
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
        mCampaignPassage.setLastPlayedMission(mScreenId);
        StartableIntent campaignIntent;
        if (mScreenId == 3 || mScreenId == 5) { //those are not usual missions
            campaignIntent = new CampaignFindPathMissionIntent(getMissionActivity(missionData),
                    missionData, mCampaignFileName, mScreenId);
        } else {
            campaignIntent = new CampaignMissionIntent(getMissionActivity(missionData),
                    missionData, mCampaignFileName, mScreenId);
        }
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

    private long mDisabledNextButtonLastTouch;
    private int mDisabledNextButtonClicksAmount;
    private final static int sDisabledButtonToNextDelay = 1500;
    private final static int sDisabledButtonToNextClicks = 6;
    private Toast mPreviousToast;

    private boolean onNextAdditionalTouch(NextButton button, TouchEvent pSceneTouchEvent) {
        if (button == null || button.isEnabled()) {
            mDisabledNextButtonClicksAmount = 0;
            mDisabledNextButtonLastTouch = 0;
            return false;
        }
        if (!button.isEnabled() && pSceneTouchEvent.isActionUp()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mDisabledNextButtonLastTouch < sDisabledButtonToNextDelay) {
                mDisabledNextButtonClicksAmount++;
                if (mDisabledNextButtonClicksAmount >= sDisabledButtonToNextClicks) {
                    showClickToast(getString(R.string.manual_next_mission_enabled));
                    mDisabledNextButtonClicksAmount = 0;
                    mDisabledNextButtonLastTouch = 0;
                    mCampaignPassage.markNewCampaignPassed();
                } else if (mDisabledNextButtonClicksAmount == 5) {
                    showClickToast(getString(R.string.manual_next_enable_single, 1));
                } else if (mDisabledNextButtonClicksAmount >= 2) {
                    showClickToast(getString(R.string.manual_next_enable_plural,
                            sDisabledButtonToNextClicks - mDisabledNextButtonClicksAmount));
                }
            } else {
                mDisabledNextButtonClicksAmount = 0;
                mPreviousToast = null;
            }
            mNextScreenButton.setEnabled(mCampaignPassage.checkCampaignPassed(mScreenId));
            mDisabledNextButtonLastTouch = currentTime;
            return true;
        }
        return false;
    }

    private void showClickToast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mPreviousToast != null) {
                    mPreviousToast.cancel();
                }
                mPreviousToast = Toast.makeText(CampaignActivity.this, str,
                        Toast.LENGTH_SHORT);
                mPreviousToast.show();
            }
        });
    }
}
