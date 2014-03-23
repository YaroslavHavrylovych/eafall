package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameloop.MoneyUpdateCycle;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.SunStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 * extends {@link MainOperationsBaseGameActivity} with adding
 * physical world capabilities to it
 */
public abstract class PhysicWorldGameActivity extends MainOperationsBaseGameActivity {
    /** tag, which is used for debugging purpose */
    public static final String TAG = PhysicWorldGameActivity.class.getCanonicalName();
    /** {@link com.badlogic.gdx.physics.box2d.FixtureDef} for obstacles (static bodies) */
    private final FixtureDef mStaticBodyFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f);
    /** current game physics world */
    private PhysicsWorld mPhysicsWorld;

    @Override
    protected void onInitGameScene() {
        mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
        super.onInitGameScene();
    }

    /**
     * initialize physic world. For using in child classes without accessing private fields.
     */
    @Override
    protected void initPhysicWorld() {
        mHud.registerUpdateHandler(new TimerHandler(MONEY_UPDATE_TIME, true, new MoneyUpdateCycle(sTeams)));

        mGameScene.registerUpdateHandler(mPhysicsWorld);

        createBounds();
    }

    /** bound for objects so they can't get out of the screen */
    private void createBounds() {
        LoggerHelper.methodInvocation(TAG, "createBounds");
        PhysicsFactory.createLineBody(
                mPhysicsWorld, -1, -1, -1, SizeConstants.GAME_FIELD_HEIGHT + 1, mStaticBodyFixtureDef);
        PhysicsFactory.createLineBody(
                mPhysicsWorld, -1, -1, SizeConstants.GAME_FIELD_WIDTH + 1, -1, mStaticBodyFixtureDef);
        PhysicsFactory.createLineBody(
                mPhysicsWorld, SizeConstants.GAME_FIELD_WIDTH + 1, -1, SizeConstants.GAME_FIELD_WIDTH + 1,
                SizeConstants.GAME_FIELD_WIDTH + 1, mStaticBodyFixtureDef);
        PhysicsFactory.createLineBody(mPhysicsWorld, SizeConstants.GAME_FIELD_WIDTH + 1,
                SizeConstants.GAME_FIELD_HEIGHT + 1, -1, SizeConstants.GAME_FIELD_HEIGHT + 1, mStaticBodyFixtureDef);
    }

    @Override
    protected PlanetStaticObject createPlanet(final float x, final float y, final ITextureRegion textureRegion, final String key, final ITeam team, boolean isFakePlanet) {
        PlanetStaticObject planetStaticObject = super.createPlanet(x, y, textureRegion, key, team, isFakePlanet);
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, planetStaticObject, BodyDef.BodyType.StaticBody, mStaticBodyFixtureDef);
        planetStaticObject.setBody(body);
        return planetStaticObject;
    }

    @Override
    protected SunStaticObject createSun() {
        LoggerHelper.methodInvocation(TAG, "createSun");
        SunStaticObject sunStaticObject = super.createSun();
        sunStaticObject.setBody(PhysicsFactory.createCircleBody(mPhysicsWorld, sunStaticObject, BodyDef.BodyType.StaticBody, mStaticBodyFixtureDef));
        return sunStaticObject;
    }

    @Override
    protected Unit createAndAttachUnitCarcass(final int unitKey, final ITeam unitTeam) {
        Unit unit = super.createAndAttachUnitCarcass(unitKey, unitTeam);
        final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 0f);
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, unit, BodyDef.BodyType.DynamicBody, playerFixtureDef);
        unit.setBody(body);
        body.setUserData(unit);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(unit, body, true, true));
        return unit;
    }

    @Override
    public void detachPhysicsBody(final GameObject gameObject) {
        if (gameObject.getBody() == null)
            return;
        final PhysicsConnector pc = mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(gameObject);
        if (pc != null) {
            mPhysicsWorld.unregisterPhysicsConnector(pc);
        }
        mPhysicsWorld.destroyBody(gameObject.getBody());
    }

    protected void setContactListener(ContactListener contactListener) {
        mPhysicsWorld.setContactListener(contactListener);
    }
}
