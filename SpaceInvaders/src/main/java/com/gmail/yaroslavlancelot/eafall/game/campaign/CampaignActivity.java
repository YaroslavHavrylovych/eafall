package com.gmail.yaroslavlancelot.eafall.game.campaign;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.GameActivity;
import com.gmail.yaroslavlancelot.eafall.game.audio.BackgroundMusic;
import com.gmail.yaroslavlancelot.eafall.game.campaign.intents.CampaignIntent;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.CampaignDataLoader;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.CampaignListLoader;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.PositionLoader;
import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.simpleframework.xml.core.Persister;

/**
 * Used to display missions list or campaigns list.
 * <br/>
 * The main thing is to parse xml with campaign data and correctly display it on the screen.
 *
 * @author Yaroslav Havrylovych
 */
public class CampaignActivity extends GameActivity {
    private String mCampaignFileName;
    private CampaignListLoader mCampaignListLoader;

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
    protected void asyncResourcesLoading() {
        mCampaignListLoader = loadObjects(mCampaignFileName, CampaignListLoader.class);
        //adding resources
        mResourcesLoader.addImage(mCampaignListLoader.background,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT);
        for (CampaignDataLoader dataLoader : mCampaignListLoader.getList()) {
            mResourcesLoader.addImage(dataLoader.picture, dataLoader.width, dataLoader.height);
        }
        //loading resources
        mResourcesLoader.loadImages(getTextureManager(), getVertexBufferObjectManager());
        //scene populating
        populateScene();
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
    public void onResourcesLoaded() {
        hideSplash();
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

    private void populateScene() {
        EaFallScene scene = mSceneManager.getWorkingScene();
        //background
        scene.setBackground(mCampaignListLoader.background,
                getVertexBufferObjectManager());
        //sprites
        for (CampaignDataLoader dataLoader : mCampaignListLoader.getList()) {
            PositionLoader position = dataLoader.position;
            Sprite sprite = new Sprite(position.x, position.y,
                    TextureRegionHolder.getRegion(dataLoader.picture),
                    getVertexBufferObjectManager());
            scene.attachChild(sprite);
        }
    }
}
