package com.gmail.yaroslavlancelot.eafall.game.client.thick;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.client.ClientGameActivity;
import com.gmail.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.ContactListener;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.extension.physics.box2d.PhysicsFactory;

/**
 * extends {@link ClientGameActivity} with adding world boundaries and
 * registering money update cycles (not used for client because all money logic handles on server)
 */
public abstract class ThickClientGameActivity extends ClientGameActivity {
    /** tag, which is used for debugging purpose */
    public static final String TAG = ThickClientGameActivity.class.getCanonicalName();
    /** game objects contact listener */
    protected ContactListener mContactListener;

    /**
     * initialize physic world. For using in child classes without accessing private fields.
     */
    @Override
    protected void initThickClient() {
        mPhysicsWorld.setContactListener(mContactListener = new ContactListener());
        mHud.registerUpdateHandler(new TimerHandler(MoneyUpdateCycle.MONEY_UPDATE_TIME,
                true, new MoneyUpdateCycle()));
        createBounds();
    }

    /** bound for objects so they can't get out of the screen */
    private void createBounds() {
        LoggerHelper.methodInvocation(TAG, "createBounds");
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

    /** update money amount in the game */
    protected class MoneyUpdateCycle implements ITimerCallback {
        public static final int MONEY_UPDATE_TIME = 10;

        /** money update cycle timer */
        public MoneyUpdateCycle() {
        }

        @Override
        public void onTimePassed(final TimerHandler pTimerHandler) {
            for (IPlayer player : PlayersHolder.getInstance().getElements()) {
                player.incomeTime();
            }
        }
    }
}
