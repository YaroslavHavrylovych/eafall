package com.yaroslavlancelot.eafall.game.constant;

import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.extension.physics.box2d.PhysicsFactory;

/**
 * box2d bodies categories and maskbits
 */
public class CollisionCategories {
    /*
     * CATEGORIES
     */
    public final static short CATEGORY_STATIC_OBJECT = 1;
    public final static short CATEGORY_PLAYER1 = 2;
    public final static short CATEGORY_PLAYER2 = 4;
    /*
     * MASKBIT
     */
    /* general */
    public final static short MASKBITS_STATIC_OBJECT = (short) (CATEGORY_STATIC_OBJECT +
            CATEGORY_PLAYER1 + CATEGORY_PLAYER2);

    /* thin client */
    public final static short MASKBITS_PLAYER1_THIN = (short) 0;
    public final static short MASKBITS_PLAYER2_THIN = (short) 0;

    /* thick client */
    public final static short MASKBITS_PLAYER1_THICK = (short) (CATEGORY_STATIC_OBJECT + CATEGORY_PLAYER1 +
            CATEGORY_PLAYER2);
    public final static short MASKBITS_PLAYER2_THICK = (short) (CATEGORY_STATIC_OBJECT + CATEGORY_PLAYER1 +
            CATEGORY_PLAYER2);
    /*
     * Fixture def constants
     */
    /** {@link com.badlogic.gdx.physics.box2d.FixtureDef} for obstacles (static bodies) */
    public final static FixtureDef STATIC_BODY_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false,
            CATEGORY_STATIC_OBJECT,
            MASKBITS_STATIC_OBJECT, (short) 0);
}
