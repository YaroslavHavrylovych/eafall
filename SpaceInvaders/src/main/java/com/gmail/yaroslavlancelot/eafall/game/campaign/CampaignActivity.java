package com.gmail.yaroslavlancelot.eafall.game.campaign;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.GameActivity;
import com.gmail.yaroslavlancelot.eafall.game.audio.BackgroundMusic;
import com.gmail.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.gmail.yaroslavlancelot.eafall.game.campaign.intents.CampaignIntent;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.CampaignDataLoader;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.CampaignListLoader;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.PositionLoader;
import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;

import org.andengine.audio.sound.Sound;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.simpleframework.xml.core.Persister;

import java.util.ArrayList;
import java.util.List;

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
    private Sound mSelectSound;
    private Sprite mSelectImage;

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
        mResourcesLoader.addImage(mCampaignListLoader.image_select,
                SizeConstants.SELECTOR_IMAGE_SIZE, SizeConstants.SELECTOR_IMAGE_SIZE);
        //loading resources
        mResourcesLoader.loadImages(getTextureManager(), getVertexBufferObjectManager());
        //sound
        if (Config.getConfig().isSoundsEnabled()) {
            mSelectSound = SoundFactory.getInstance().loadSound(mCampaignListLoader.sound_select);
        }
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
        VertexBufferObjectManager objectManager = getVertexBufferObjectManager();
        //background
        scene.setBackground(mCampaignListLoader.background, objectManager);
        //sprites
        List<Sprite> elementsList = new ArrayList<Sprite>(mCampaignListLoader.getList().size());
        for (CampaignDataLoader dataLoader : mCampaignListLoader.getList()) {
            PositionLoader position = dataLoader.position;
            Sprite sprite = new Sprite(position.x, position.y,
                    TextureRegionHolder.getRegion(dataLoader.picture), objectManager);
            elementsList.add(sprite);
            scene.attachChild(sprite);
            sprite.setTouchCallback(new ElementTouchCallback(sprite));
            scene.registerTouchArea(sprite);
        }
        //selected
        mSelectImage = new Sprite(0, 0,
                TextureRegionHolder.getRegion(mCampaignListLoader.image_select), objectManager);
        mSelectImage.setVisible(false);
        scene.attachChild(mSelectImage);
        click(elementsList.get(0), false);
        mSelectImage.setVisible(true);
    }

    private void click(IEntity entity, boolean sound) {
        if (sound && (mSelectSound != null)) {
            SoundFactory.getInstance().playSound(mSelectSound);
        }
        mSelectImage.setPosition(entity);
    }

    private void click(IEntity entity) {
        click(entity, true);
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
