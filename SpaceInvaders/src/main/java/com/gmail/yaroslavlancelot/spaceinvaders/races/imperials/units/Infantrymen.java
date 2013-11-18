package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Higgs;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Physical;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Annihilator;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Electric;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Bullet;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Infantrymen extends Unit {
    public static final String KEY_IMPERIALS_THIRD_UNIT = GameStringsConstantsAndUtils.getPathToUnits(Imperials.RACE_NAME) + "infantrymen.png";
    private EntityOperations mEntityOperations;

    public Infantrymen(final VertexBufferObjectManager vertexBufferObjectManager, EntityOperations entityOperations) {
        super(TextureRegionHolderUtils.getInstance().getElement(KEY_IMPERIALS_THIRD_UNIT), vertexBufferObjectManager);
        mObjectArmor = new Physical(10);
        mObjectDamage = new Electric(20);
        mObjectHealth = 700;
        mAttackRadius = 70;
        mViewRadius = 190;
        setWidth(SizeConstants.UNIT_SIZE);
        setHeight(SizeConstants.UNIT_SIZE);
        mEntityOperations = entityOperations;
    }

    public static void loadResources(final Context context, final BitmapTextureAtlas textureAtlas) {
        loadResource(KEY_IMPERIALS_THIRD_UNIT, context, textureAtlas, 32, 0);
    }

    @Override
    protected void attackGoal() {
        Bullet bullet = new Bullet(getVertexBufferObjectManager(), mEntityOperations, getBackgroundColor());
        bullet.fire(getX() + SizeConstants.UNIT_SIZE / 2, getY() - Bullet.BULLET_SIZE,
                mObjectToAttack.getCenterX(), mObjectToAttack.getCenterY(), mEnemiesUpdater, mObjectDamage);

        mEntityOperations.attachEntity(bullet);
    }

    @Override
    public void setBackgroundArea() {
        setBackgroundArea(new Area(2, 11, 11, 3));
    }
}
