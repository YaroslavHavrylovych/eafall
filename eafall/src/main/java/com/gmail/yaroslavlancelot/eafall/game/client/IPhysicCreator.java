package com.gmail.yaroslavlancelot.eafall.game.client;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite;

/**
 * Provide physic bodies creation interface
 *
 * @author Yaroslav Havrylovych
 */
public interface IPhysicCreator {
    Body createPhysicBody(BodiedSprite bodiedSprite, BodyDef.BodyType bodyType, FixtureDef fixtureDef);
}
