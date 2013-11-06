package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Electrical;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Magnetic;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Bullet;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Agent extends Unit {
    public static final String KEY_IMPERIALS_FIFTH_UNIT = GameStringsConstantsAndUtils.getPathToUnits(Imperials.RACE_NAME) + "imperials_fifth_unit.png";
    private EntityOperations mEntityOperations;

    public Agent(final VertexBufferObjectManager vertexBufferObjectManager, EntityOperations entityOperations) {
        super(TextureRegionHolderUtils.getInstance().getElement(KEY_IMPERIALS_FIFTH_UNIT), vertexBufferObjectManager);
        mObjectArmor = new Electrical(5);
        mObjectDamage = new Magnetic(60);
        mObjectHealth = 400;
        mAttackRadius = 90;
        mViewRadius = 220;
        setWidth(SizeConstants.UNIT_SIZE);
        setHeight(SizeConstants.UNIT_SIZE);
        mEntityOperations = entityOperations;
    }

    public static void loadResources(final Context context, final BitmapTextureAtlas textureAtlas) {
        loadResource(KEY_IMPERIALS_FIFTH_UNIT, context, textureAtlas, 15, 15);
    }

    @Override
    protected void attackGoal() {
        Bullet bullet = new Bullet(getVertexBufferObjectManager(), mEntityOperations, getBackgroundColor());
        bullet.fire(getX() + SizeConstants.UNIT_SIZE / 2, getY() - Bullet.BULLET_SIZE,
                mObjectToAttack.getCenterX(), mObjectToAttack.getCenterY(), mEnemiesUpdater, mObjectDamage);

        mEntityOperations.attachEntity(bullet);
    }
}
