package com.yaroslavlancelot.eafall.game.client.thick.single;

import android.app.Activity;
import android.content.Intent;

import com.yaroslavlancelot.eafall.android.StartableIntent;
import com.yaroslavlancelot.eafall.game.ai.IBot;
import com.yaroslavlancelot.eafall.game.ai.VeryFirstBot;
import com.yaroslavlancelot.eafall.game.campaign.intents.CampaignIntent;
import com.yaroslavlancelot.eafall.game.client.thick.ThickClientGameActivity;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.player.IPlayer;

import timber.log.Timber;

/**
 * Single player game
 */
public class SinglePlayerGameActivity extends ThickClientGameActivity {
    private Thread mBotThread;

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();
        hideSplash();
    }

    @Override
    protected void initPlayer(final IPlayer player) {
        super.initPlayer(player);

        if (player.getControlType().bot()) {
            initBotControlledPlayer(player);
        }
    }

    @Override
    protected void userWantCreateBuilding(final IPlayer userPlayer, BuildingId buildingId) {
        PlanetStaticObject planetStaticObject = userPlayer.getPlanet();
        if (planetStaticObject != null) {
            userPlayer.getPlanet().createBuilding(buildingId);
        }
    }

    protected void initBotControlledPlayer(final IPlayer initializingPlayer) {
        IBot bot = null;
        try {
            Class<?> clazz = Class.forName(mMissionConfig.getBotLogic().trim());
            bot = (IBot) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            Timber.e(e, "can't found the bot class");
        } catch (InstantiationException e) {
            Timber.e(e, "can't instantiate the bot class");
        } catch (IllegalAccessException e) {
            Timber.e(e, "can't get access to the bot class");
        }
        if (bot == null) {
            bot = new VeryFirstBot();
        }
        bot.init(initializingPlayer);
        Thread thread = new Thread(bot);
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        mBotThread = thread;
        mBotThread.start();
    }

    @Override
    protected void onGameOver() {
        String campaignFileName = getIntent().getStringExtra(CampaignIntent.CAMPAIGN_FILE_NAME_KEY);
        if (campaignFileName != null) {
            StartableIntent intent = new CampaignIntent(campaignFileName, mRuler.isSuccess());
            intent.start(SinglePlayerGameActivity.this);
        }
        super.onGameOver();
    }
}
