package com.gmail.yaroslavlancelot.eafall.game.eventbus;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite;

/** holds data needed for registering body in PhysicWorld */
public class CreatePhysicBodyEvent {
    /** game object body for which needs to be created */
    private BodiedSprite mGameObject;
    /** body type (is it static or dynamic or kinematic) */
    private BodyDef.BodyType mBodyType;
    /** body fixture definition (define collaborations with other bodies) */
    private FixtureDef mFixtureDef;
    /** body force position */
    private float mX, mY, mAngle;
    /** is body force position needs to be applied */
    private boolean mIsCustomBodyTransform;

    /** used for registering static circle body */
    public CreatePhysicBodyEvent(BodiedSprite gameObject) {
        this(gameObject, BodyDef.BodyType.StaticBody, CollisionCategories.STATIC_BODY_FIXTURE_DEF);
    }

    public CreatePhysicBodyEvent(BodiedSprite gameObject, BodyDef.BodyType bodyType, FixtureDef fixtureDef) {
        mGameObject = gameObject;
        mBodyType = bodyType;
        mFixtureDef = fixtureDef;
    }

    public CreatePhysicBodyEvent(BodiedSprite gameObject, BodyDef.BodyType bodyType, FixtureDef fixtureDef, float x, float y, float angle) {
        this(gameObject, bodyType, fixtureDef);
        mX = x;
        mY = y;
        mAngle = angle;
        mIsCustomBodyTransform = true;
    }

    public BodiedSprite getGameObject() {
        return mGameObject;
    }

    public BodyDef.BodyType getBodyType() {
        return mBodyType;
    }

    public FixtureDef getFixtureDef() {
        return mFixtureDef;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public float getAngle() {
        return mAngle;
    }

    public boolean isCustomBodyTransform() {
        return mIsCustomBodyTransform;
    }
}
