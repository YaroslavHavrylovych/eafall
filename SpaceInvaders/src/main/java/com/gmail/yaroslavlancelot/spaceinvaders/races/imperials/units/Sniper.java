package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.HeavyWater;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Higgs;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Physical;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Annihilator;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Electric;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Neutrino;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Bullet;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Sniper extends Unit {
    public static final String KEY_IMPERIALS_FOURTH_UNIT = GameStringsConstantsAndUtils.getPathToUnits(Imperials.RACE_NAME) + "sniper.png";
    private EntityOperations mEntityOperations;

    public Sniper(final VertexBufferObjectManager vertexBufferObjectManager, EntityOperations entityOperations) {
        super(TextureRegionHolderUtils.getInstance().getElement(KEY_IMPERIALS_FOURTH_UNIT), vertexBufferObjectManager);
        mObjectArmor = new HeavyWater(3);
        mObjectDamage = new Neutrino(50);
        mObjectHealth = 220;
        mAttackRadius = 220;
        mViewRadius = 320;
        setWidth(SizeConstants.UNIT_SIZE);
        setHeight(SizeConstants.UNIT_SIZE);
        mEntityOperations = entityOperations;
    }

    public static void loadResources(final Context context, final BitmapTextureAtlas textureAtlas) {
        loadResource(KEY_IMPERIALS_FOURTH_UNIT, context, textureAtlas, 0, 16);
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
        setBackgroundArea(new Area(6, 3, 4, 10));
    }
}
