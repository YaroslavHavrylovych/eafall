package com.yaroslavlancelot.eafall.game.campaign.missions.utils;

import android.os.Handler;
import android.os.Looper;
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
import com.yaroslavlancelot.eafall.game.entity.gameobject.VisibleAreaSprite;
import com.yaroslavlancelot.eafall.game.entity.gameobject.setlectable.Selectable;
import com.yaroslavlancelot.eafall.game.entity.gameobject.setlectable.selector.SelectorFactory;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.MoveBetweenPointsPath;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation.MoveByClickUnitPath;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.Player;
import com.yaroslavlancelot.eafall.game.popup.GameOverPopup;
import com.yaroslavlancelot.eafall.game.popup.IPopup;
import com.yaroslavlancelot.eafall.game.scene.hud.BaseGameHud;
import com.yaroslavlancelot.eafall.game.scene.hud.FindPathGameHud;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import static com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.MoveBetweenPointsPath.Point;

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
    private Line mMovingLine;
    private MainUnitHighlight mMainUnitHighlight = new MainUnitHighlight();
    private IUpdateHandler mUnitUpdateHandler = new IUpdateHandler() {
        @Override
        public void onUpdate(float pSecondsElapsed) {
            if (mMainUnit == null || !mMainUnit.isObjectAlive()) {
                return;
            }
            mMovingLine.setPosition(mMovingLine.getX1(), mMovingLine.getY1(),
                    mMainUnit.getX(), mMainUnit.getY());
        }

        @Override
        public void reset() {
        }
    };

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

    protected abstract void onPopulateEnemies();

    @Override
    protected void initWorkingScene() {
        super.initWorkingScene();
        EaFallScene scene = mSceneManager.getWorkingScene();
        initMainUnit(scene);
        initEndpoint(scene);
        initEndConditions(scene);
        onPopulateEnemies();
        initMovingLine(scene);
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
                Toast.makeText(FindPathMissionActivity.this, R.string.find_path_mission_hint,
                        Toast.LENGTH_LONG)
                        .show();
                runOnUpdateThread(new Runnable() {
                    @Override
                    public void run() {
                        highlightMainUnit();
                    }
                });
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
    protected void createEnemy(int key, int[] startPosition, int[] endPosition) {
        createEnemy(key, startPosition, endPosition, startPosition);
    }

    protected void createEnemy(int key, int[] startPosition, int[] endPosition, int initPosition[]) {
        //unit
        MoveBetweenPointsPath moveBetweenPointsPath =
                new MoveBetweenPointsPath(new Point(startPosition), new Point(endPosition));
        Unit unit = createMovableUnit(mSecondPlayer, key,
                initPosition[0], initPosition[1], moveBetweenPointsPath);
        //radius
        int size = (int) (unit.getViewRadius() * 2.1f);
        VisibleAreaSprite visibleAreaSprite;
        if (size > 400) {
            visibleAreaSprite = new VisibleAreaSprite(0, 0, size, size,
                    TextureRegionHolder.getRegion(StringConstants.KEY_BIGGER_VISIBLE_AREA), unit,
                    getVertexBufferObjectManager());
        } else {
            visibleAreaSprite = new VisibleAreaSprite(0, 0, size, size,
                    TextureRegionHolder.getRegion(StringConstants.KEY_VISIBLE_AREA), unit,
                    getVertexBufferObjectManager());
        }
        mSceneManager.getWorkingScene().attachChild(visibleAreaSprite);
    }

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

    private void initMovingLine(Scene scene) {
        mMovingLine = new Line(0, 0, 0, 0, 3, getVertexBufferObjectManager());
        mMovingLine.setColor(0.24f, 0.24f, 0.455f, 0.7f);
        mMovingLine.setVisible(false);
        scene.attachChild(mMovingLine);
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
        mMainUnit.setUnitCanNotAttack(true);
        mMainUnit.setHealth(1);
        scene.setSceneTouchSilentListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                if (mMainUnit == null || !mMainUnit.isObjectAlive()) {
                    mMovingLine.setVisible(false);
                    return false;
                }
                if (pSceneTouchEvent.getMotionEvent().getPointerCount() > 1 ||
                        pSceneTouchEvent.isActionUp()) {
                    mMainUnit.unregisterUpdateHandler(mUnitUpdateHandler);
                    mMovingLine.setVisible(false);
                    mMovingLine.setPosition(0, 0, 0, 0);
                } else if (pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionMove()) {
                    mMainUnit.registerUpdateHandler(mUnitUpdateHandler);
                    mMovingLine.setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(),
                            mMainUnit.getX(), mMainUnit.getY());
                    mMovingLine.setVisible(true);
                    mMainUnitPath.setNewPoint(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                }

                return false;
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

    private void highlightMainUnit() {
        SelectorFactory.getSelector().select(mMainUnitHighlight);
        mMainUnit.registerUpdateHandler(new TimerHandler(10, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                mMainUnit.unregisterUpdateHandler(pTimerHandler);
                SelectorFactory.getSelector().deselect();
            }
        }));
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    private class MainUnitHighlight implements Selectable {
        @Override
        public float getX() {
            return mMainUnit.getX();
        }

        @Override
        public float getY() {
            return mMainUnit.getY();
        }

        @Override
        public float getWidth() {
            return mMainUnit.getHeight() * 2;
        }

        @Override
        public float getHeight() {
            return mMainUnit.getHeight() * 2;
        }
    }
}
