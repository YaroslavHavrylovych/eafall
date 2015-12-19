package com.yaroslavlancelot.eafall.game.client.thick;

import com.yaroslavlancelot.eafall.game.client.ClientGameActivity;
import com.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.ContactListener;
import com.yaroslavlancelot.eafall.game.events.periodic.money.MoneyUpdateCycle;

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

    @Override
    protected void hideSplash() {
        initThickClient();
        super.hideSplash();
    }

    private void initThickClient() {
        mPhysicsWorld.setContactListener(mContactListener = new ContactListener());
        mGamePeriodic.add(MoneyUpdateCycle.getPeriodic());
        createBounds();
    }

    /** bound for objects so they can't get out of the screen */
    private void createBounds() {
        //TODO logger was here
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
}
