package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.extension.physics.box2d.PhysicsFactory;

/**
 * Contains categories and maskbits box2d bodies. Was decided to create this class when was idea of
 * bullets which not collide with friendly objects
 */
public class CollisionCategoriesUtils {
    /*
     * categories for all types of objects
     */
    public static short CATEGORY_STATIC_OBJECT = 1;
    public static short CATEGORY_TEAM1 = 2;
    public static short CATEGORY_BULLET_TEAM1 = 4;
    public static short CATEGORY_TEAM2 = 8;
    public static short CATEGORY_BULLET_TEAM2 = 16;
    /*
     * maskbits for all types of object
     */

    public static short MASKBITS_BULLET_TEAM1 = (short) (CATEGORY_STATIC_OBJECT + CATEGORY_TEAM2);
    public static short MASKBITS_BULLET_TEAM2 = (short) (CATEGORY_STATIC_OBJECT + CATEGORY_TEAM1);

    /* for thin objects (on client) */
    public static short MASKBITS_STATIC_OBJECT_THIN = (short) (CATEGORY_BULLET_TEAM1 + CATEGORY_BULLET_TEAM2);
    public static short MASKBITS_TEAM1_THIN = (short) (CATEGORY_BULLET_TEAM2);
    public static short MASKBITS_TEAM2_THIN = (short) (CATEGORY_BULLET_TEAM1);

    /* fot thick objects (on server on single game) */
    public static short MASKBITS_STATIC_OBJECT_THICK = (short) (CATEGORY_STATIC_OBJECT +
            CATEGORY_BULLET_TEAM1 + CATEGORY_BULLET_TEAM2 +
            CATEGORY_TEAM1 + CATEGORY_TEAM2);
    public static short MASKBITS_TEAM1_THICK = (short) (CATEGORY_STATIC_OBJECT + CATEGORY_TEAM1 +
            CATEGORY_TEAM2 + CATEGORY_BULLET_TEAM2);
    public static short MASKBITS_TEAM2_THICK = (short) (CATEGORY_STATIC_OBJECT + CATEGORY_TEAM1 +
            CATEGORY_TEAM2 + CATEGORY_BULLET_TEAM1);
    /*
     * holders for fixture def
     */
    private final static FixtureDef sFixtureDefBullet1 = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false,
            CollisionCategoriesUtils.CATEGORY_BULLET_TEAM1,
            CollisionCategoriesUtils.MASKBITS_BULLET_TEAM1, (short) 0);
    private volatile static FixtureDef sFixtureDefBullet2 = PhysicsFactory.createFixtureDef(1f, 0f, 0f, false,
            CollisionCategoriesUtils.CATEGORY_BULLET_TEAM2,
            CollisionCategoriesUtils.MASKBITS_BULLET_TEAM2, (short) 0);

    public static FixtureDef getBulletFixtureDefByUnitCategory(short category) {
        if (category == CATEGORY_TEAM1)
            return sFixtureDefBullet1;
        return sFixtureDefBullet2;
    }
}
