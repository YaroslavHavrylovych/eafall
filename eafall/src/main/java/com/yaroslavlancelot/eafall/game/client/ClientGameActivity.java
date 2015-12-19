package com.yaroslavlancelot.eafall.game.client;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.yaroslavlancelot.eafall.game.BaseGameObjectsActivity;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.client.thick.income.ClientIncomeHandler;
import com.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.SunStaticObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.events.GameStartCooldown;
import com.yaroslavlancelot.eafall.game.events.aperiodic.endgame.GameOverEvent;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.CreateBuildingEvent;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingSettingsPopupShowEvent;
import com.yaroslavlancelot.eafall.game.events.periodic.IPeriodic;
import com.yaroslavlancelot.eafall.game.events.periodic.time.GameTime;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.Player;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.popup.BuildingSettingsDialog;
import com.yaroslavlancelot.eafall.game.popup.GameOverPopup;
import com.yaroslavlancelot.eafall.game.popup.IPopup;
import com.yaroslavlancelot.eafall.game.rule.IRuler;
import com.yaroslavlancelot.eafall.game.rule.RulesFactory;
import com.yaroslavlancelot.eafall.game.scene.hud.BaseGameHud;
import com.yaroslavlancelot.eafall.game.scene.hud.ClientGameHud;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;

import org.andengine.entity.IEntity;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 * Extends {@link BaseGameObjectsActivity} with the star, planets,
 * specific game periodic`s (timer, game lifecycle logic), etc.
 */
public abstract class ClientGameActivity extends BaseGameObjectsActivity {
    /** defines whether the game is over and who is the winner */
    protected IRuler mRuler;
    /** popup to change particular building settings */
    private BuildingSettingsDialog mBuildingSettingsDialog;

    @Override
    protected BaseGameHud createHud() {
        return new ClientGameHud();
    }

    @Override
    protected void loadResources() {
        super.loadResources();
        //whether or not the mission is bounded (timing)
        if (mMissionConfig.isTimerEnabled()) {
            mGamePeriodic.add(GameTime.getPeriodic(mMissionConfig.getTime()));
        }
    }

    @Override
    protected void onPopulateWorkingScene(final EaFallScene scene) {
        //initSun
        createSun();
        //planets
        initFirstPlanet();
        initSecondPlanet();
        super.onPopulateWorkingScene(scene);
        for (IPlayer player : PlayersHolder.getInstance().getElements()) {
            if (player.getControlType().user()) {
                ClientIncomeHandler.init(player, mSceneManager.getWorkingScene(),
                        getVertexBufferObjectManager());
            }
        }
    }

    @Override
    protected void onShowWorkingScene() {
        super.onShowWorkingScene();
        mSceneManager.getWorkingScene().setIgnoreUpdate(true);
        final GameStartCooldown timerHandler = new GameStartCooldown((ClientGameHud) mHud) {
            @Override
            public void timerEnded() {
                mSceneManager.getWorkingScene().setIgnoreUpdate(false);
                mFirstPlayer.incomeTime();
                mSecondPlayer.incomeTime();
                startRuler();
            }
        };
        timerHandler.start();
    }

    @Override
    protected IPlayer createPlayer(String name, IAlliance alliance, IPlayer.ControlType playerType,
                                   final MissionConfig missionConfig) {
        return new Player(name, alliance, playerType, 10, missionConfig);
    }

    /** start tracker which tracks game rules */
    protected void startRuler() {
        mRuler = RulesFactory.createRuler(
                mMissionConfig.getMissionType(),
                mMissionConfig.getValue(),
                mMissionConfig.isTimerEnabled());
        mRuler.startTracking();
    }

    /** init second player and planet */
    protected void initSecondPlanet() {
        PlanetStaticObject planet = createPlanet(
                SizeConstants.GAME_FIELD_WIDTH - SizeConstants.PLANET_DIAMETER / 2 - SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                SizeConstants.HALF_FIELD_HEIGHT,
                TextureRegionHolder.getRegion(StringConstants.KEY_SECOND_PLANET),
                StringConstants.KEY_SECOND_PLANET,
                mSecondPlayer);
        mSecondPlayer.setPlanet(planet);
    }

    /** create planet game object */
    protected PlanetStaticObject createPlanet(float x, float y,
                                              ITextureRegion textureRegion,
                                              String key,
                                              IPlayer player,
                                              long... unitUniqueId) {
        //TODO logger was here
        PlanetStaticObject planet = new PlanetStaticObject(x, y, textureRegion,
                getVertexBufferObjectManager()) {
            @Override
            public void registerTouch(final IEntity entity) {
                mSceneManager.getWorkingScene().registerTouchArea(entity);
            }

            @Override
            public void unregisterTouch(final IEntity entity) {
                mSceneManager.getWorkingScene().unregisterTouchArea(entity);
            }
        };
        int planetNameRes = player.getControlType().user()
                ? mMissionConfig.getPlayerPlanet() : mMissionConfig.getOpponentPlanet();
        planet.init(player.getName(), planetNameRes, this, mMissionConfig.getPlanetHealth());
        mSceneManager.getWorkingScene().registerTouchArea(planet);
        planet.attachSelf();
        if (unitUniqueId.length > 0) {
            planet.setObjectUniqueId(unitUniqueId[0]);
        }
        mGameObjectsMap.put(planet.getObjectUniqueId(), planet);
        createPhysicBody(planet, BodyDef.BodyType.StaticBody, CollisionCategories.STATIC_BODY_FIXTURE_DEF);
        return planet;
    }

    /** init first player and planet */
    protected void initFirstPlanet() {
        PlanetStaticObject planet = createPlanet(SizeConstants.PLANET_DIAMETER / 2 + SizeConstants.ADDITION_MARGIN_FOR_PLANET,
                SizeConstants.HALF_FIELD_HEIGHT,
                TextureRegionHolder.getRegion(StringConstants.KEY_FIRST_PLANET),
                StringConstants.KEY_FIRST_PLANET, mFirstPlayer);
        mFirstPlayer.setPlanet(planet);
    }

    /** create sun */
    protected SunStaticObject createSun() {
        SunStaticObject sunStaticObject = new SunStaticObject(
                SizeConstants.HALF_FIELD_WIDTH, SizeConstants.HALF_FIELD_HEIGHT,
                mEngine.getVertexBufferObjectManager());
        attachSprite(sunStaticObject);
        mSceneManager.getWorkingScene().registerTouchArea(sunStaticObject);
        mGameObjectsMap.put(sunStaticObject.getObjectUniqueId(), sunStaticObject);
        createPhysicBody(sunStaticObject, BodyDef.BodyType.StaticBody, CollisionCategories.STATIC_BODY_FIXTURE_DEF);
        sunStaticObject.initDescription(mMissionConfig.getStarCodeName(), mMissionConfig.getStarConstellation());
        return sunStaticObject;
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final BuildingSettingsPopupShowEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mBuildingSettingsDialog == null) {
                    mBuildingSettingsDialog = new BuildingSettingsDialog(ClientGameActivity.this);
                }
                mBuildingSettingsDialog.init(event.getUnitBuilding());
                mBuildingSettingsDialog.show();
            }
        });
    }

    @SuppressWarnings("unused")
    public void onEvent(final GameOverEvent gameOverEvent) {
        //stop stoppable periodic(s)
        for (IPeriodic periodic : mGamePeriodic) {
            if (periodic.stoppableWhenGameOver()) {
                mSceneManager.getWorkingScene().unregisterUpdateHandler(periodic.getUpdateHandler());
            }
        }
        //show popup
        GameOverPopup popup = new GameOverPopup(mHud, mCamera, getVertexBufferObjectManager());
        popup.setSuccess(mRuler.isSuccess());
        popup.setStateChangeListener(new IPopup.StateChangingListener() {
            @Override
            public void onShowed() {
            }

            @Override
            public void onHided() {
                ClientGameActivity.this.finish();
            }
        });
        popup.showPopup();
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final CreateBuildingEvent createBuildingEvent) {
        userWantCreateBuilding(PlayersHolder.getPlayer(createBuildingEvent.getPlayerName()), createBuildingEvent.getBuildingId());
    }

    protected abstract void userWantCreateBuilding(IPlayer userPlayer, BuildingId buildingId);
}