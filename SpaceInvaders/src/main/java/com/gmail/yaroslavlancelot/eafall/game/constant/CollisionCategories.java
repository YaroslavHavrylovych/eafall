package com.gmail.yaroslavlancelot.eafall.game.constant;

import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.extension.physics.box2d.PhysicsFactory;

/**
 * Contains categories and maskbits box2d bodies. Was decided to create this class when was idea of
 * bullets which not collide with friendly objects
 */
public class CollisionCategories {
    /*
     * categories for all types of objects
     */
    public static short CATEGORY_STATIC_OBJECT = 1;
    public static short CATEGORY_PLAYER1 = 2;
    public static short CATEGORY_BULLET_PLAYER1 = 4;
    public static short CATEGORY_PLAYER2 = 8;
    public static short CATEGORY_BULLET_PLAYER2 = 16;
    /*
     * maskbits for all types of object
     */

    public static short MASKBITS_BULLET_PLAYER1 = (short) (CATEGORY_STATIC_OBJECT + CATEGORY_PLAYER2);
    public static short MASKBITS_BULLET_PLAYER2 = (short) (CATEGORY_STATIC_OBJECT + CATEGORY_PLAYER1);

    /* for thin objects (on client) */
    public static short MASKBITS_STATIC_OBJECT_THIN = (short) (CATEGORY_BULLET_PLAYER1 + CATEGORY_BULLET_PLAYER2);
    public static short MASKBITS_PLAYER1_THIN = (short) (CATEGORY_BULLET_PLAYER2);
    public static short MASKBITS_PLAYER2_THIN = (short) (CATEGORY_BULLET_PLAYER1);

    /* fot thick objects (on server on single game) */
    public static short MASKBITS_STATIC_OBJECT_THICK = (short) (CATEGORY_STATIC_OBJECT +
            CATEGORY_BULLET_PLAYER1 + CATEGORY_BULLET_PLAYER2 +
            CATEGORY_PLAYER1 + CATEGORY_PLAYER2);
    public static short MASKBITS_PLAYER1_THICK = (short) (CATEGORY_STATIC_OBJECT + CATEGORY_PLAYER1 +
            CATEGORY_PLAYER2 + CATEGORY_BULLET_PLAYER2);
    public static short MASKBITS_PLAYER2_THICK = (short) (CATEGORY_STATIC_OBJECT + CATEGORY_PLAYER1 +
            CATEGORY_PLAYER2 + CATEGORY_BULLET_PLAYER1);
    /*
     * holders for fixture def
     */
    private final static FixtureDef sFixtureDefBullet1 = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false,
            CollisionCategories.CATEGORY_BULLET_PLAYER1,
            CollisionCategories.MASKBITS_BULLET_PLAYER1, (short) 0);
    private volatile static FixtureDef sFixtureDefBullet2 = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false,
            CollisionCategories.CATEGORY_BULLET_PLAYER2,
            CollisionCategories.MASKBITS_BULLET_PLAYER2, (short) 0);
    public static FixtureDef getBulletFixtureDefByUnitCategory(short category) {
        if (category == CATEGORY_PLAYER1)
            return sFixtureDefBullet1;
        return sFixtureDefBullet2;
    }

    /** {@link com.badlogic.gdx.physics.box2d.FixtureDef} for obstacles (static bodies) */
    public final static FixtureDef STATIC_BODY_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false,
            CATEGORY_STATIC_OBJECT,
            MASKBITS_STATIC_OBJECT_THICK, (short) 0);
}
