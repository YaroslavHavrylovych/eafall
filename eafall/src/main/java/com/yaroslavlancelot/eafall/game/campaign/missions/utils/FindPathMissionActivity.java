package com.yaroslavlancelot.eafall.game.campaign.missions.utils;

import android.widget.Toast;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.StartableIntent;
import com.yaroslavlancelot.eafall.game.BaseGameObjectsActivity;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.campaign.intents.CampaignIntent;
import com.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.EndPointAnimatedSprite;
import com.yaroslavlancelot.eafall.game.entity.gameobject.setlectable.selector.SelectorFactory;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation.MoveByClickUnitPath;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.Player;
import com.yaroslavlancelot.eafall.game.popup.GameOverPopup;
import com.yaroslavlancelot.eafall.game.popup.IPopup;
import com.yaroslavlancelot.eafall.game.scene.hud.BaseGameHud;
import com.yaroslavlancelot.eafall.game.scene.hud.FindPathGameHud;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

/** basic activity to create obstacles for the unit, which has to go from point A to point B */
public abstract class FindPathMissionActivity extends BaseGameObjectsActivity {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private OffenceUnit mMainUnit;
    private MoveByClickUnitPath mMainUnitPath;
    /** left, top, right, bottom */
    private int[] mEndpointCircleBounds = new int[4];

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    protected BaseGameHud createHud() {
        return new FindPathGameHud();
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();
        hideSplash();
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    protected abstract int[] getMainUnitCoords();

    protected abstract int[] getEndpointCoords();

    protected abstract int getMainUnitId();

    @Override
    protected void initWorkingScene() {
        super.initWorkingScene();
        EaFallScene scene = mSceneManager.getWorkingScene();
        initMainUnit(scene);
        initEndpoint(scene);
        initEndConditions(scene);
    }

    @Override
    protected void hideSplash() {
        createBounds();
        super.hideSplash();
    }

    @Override
    protected void onShowWorkingScene() {
        super.onShowWorkingScene();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FindPathMissionActivity.this, R.string.sandbox_hint,
                        Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    protected IPlayer createPlayer(String name, IAlliance alliance, IPlayer.ControlType playerType,
                                   int startMoney, int buildingsLimit, MissionConfig missionConfig) {
        return new Player(name, alliance, playerType, 0, -1, buildingsLimit, missionConfig);
    }

    // ===========================================================
    // Methods
    // ===========================================================
    private void initEndConditions(final Scene scene) {
        scene.registerUpdateHandler(new TimerHandler(.2f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                if (!mMainUnit.isObjectAlive()) {
                    scene.unregisterUpdateHandler(pTimerHandler);
                    gameOver(false);
                } else if (endpointCondition()) {
                    scene.unregisterUpdateHandler(pTimerHandler);
                    gameOver(true);
                }
            }
        }));
    }

    private boolean endpointCondition() {
        return mMainUnit.getX() > mEndpointCircleBounds[0]
                && mMainUnit.getY() > mEndpointCircleBounds[1]
                && mMainUnit.getX() < mEndpointCircleBounds[2]
                && mMainUnit.getY() < mEndpointCircleBounds[3];
    }

    private void gameOver(final boolean success) {
        GameOverPopup popup = new GameOverPopup(mHud, mCamera, getVertexBufferObjectManager());
        popup.setSuccess(success);
        popup.setStateChangeListener(new IPopup.StateChangingListener() {
            @Override
            public void onShowed() {
                SelectorFactory.getSelector().deselect();
            }

            @Override
            public void onHided() {
                String campaignFileName = getIntent()
                        .getStringExtra(CampaignIntent.CAMPAIGN_FILE_NAME_KEY);
                StartableIntent intent = new CampaignIntent(campaignFileName, success,
                        getIntent().getIntExtra(CampaignIntent.CAMPAIGN_MISSION_ID_KEY, 0));
                intent.start(FindPathMissionActivity.this);
                finish();
            }
        });
        popup.showPopup();
    }

    private void initMainUnit(EaFallScene scene) {
        int[] position = getMainUnitCoords();
        mMainUnitPath = new MoveByClickUnitPath(position[0], position[1]);
        mMainUnit = createMovableUnit(mFirstPlayer, getMainUnitId(),
                position[0], position[1], mMainUnitPath);
        scene.setClickListener(new ClickDetector.IClickDetectorListener() {
            @Override
            public void onClick(final ClickDetector pClickDetector, final int pPointerID,
                                final float pSceneX, final float pSceneY) {
                mMainUnitPath.setNewPoint(pSceneX, pSceneY);
            }
        });
    }

    private void initEndpoint(Scene scene) {
        EndPointAnimatedSprite endPointAnimatedSprite = new EndPointAnimatedSprite(
                getEndpointCoords()[0], getEndpointCoords()[1],
                (ITiledTextureRegion) TextureRegionHolder.getRegion(StringConstants.KEY_ENDPOINT),
                getVertexBufferObjectManager());
        scene.attachChild(endPointAnimatedSprite);
        int radius = (int) endPointAnimatedSprite.getWidth() / 2;
        mEndpointCircleBounds[0] = getEndpointCoords()[0] - radius;
        mEndpointCircleBounds[1] = getEndpointCoords()[1] - radius;
        mEndpointCircleBounds[2] = getEndpointCoords()[0] + radius;
        mEndpointCircleBounds[3] = getEndpointCoords()[1] + radius;
    }

    /** bound for objects so they can't get out of the screen */
    private void createBounds() {
        PhysicsFactory.createLineBody(
                mPhysicsWorld, -1, -1, -1, SizeConstants.GAME_FIELD_HEIGHT + 1,
                CollisionCategories.STATIC_BODY_FIXTURE_DEF);
        PhysicsFactory.createLineBody(
                mPhysicsWorld, -1, -1, SizeConstants.GAME_FIELD_WIDTH + 1, -1,
                CollisionCategories.STATIC_BODY_FIXTURE_DEF);
        PhysicsFactory.createLineBody(
                mPhysicsWorld, SizeConstants.GAME_FIELD_WIDTH + 1, -1, SizeConstants.GAME_FIELD_WIDTH + 1,
                SizeConstants.GAME_FIELD_WIDTH + 1, CollisionCategories.STATIC_BODY_FIXTURE_DEF);
        PhysicsFactory.createLineBody(mPhysicsWorld, SizeConstants.GAME_FIELD_WIDTH + 1,
                SizeConstants.GAME_FIELD_HEIGHT + 1, -1, SizeConstants.GAME_FIELD_HEIGHT + 1,
                CollisionCategories.STATIC_BODY_FIXTURE_DEF);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
