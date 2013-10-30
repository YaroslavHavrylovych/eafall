package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class UnitWithBullets extends ImperialsUnit {
    private EntityOperations mEntityOperations;

    public UnitWithBullets(final float x, final float y, final ITextureRegion textureRegion,
                           final VertexBufferObjectManager vertexBufferObjectManager, Damage unitDamage, Armor unitArmor,
                           EntityOperations entityOperations) {
        super(x, y, textureRegion, vertexBufferObjectManager, unitDamage, unitArmor);
        setWidth(SizeConstants.UNIT_SIZE);
        setHeight(SizeConstants.UNIT_SIZE);
        mEntityOperations = entityOperations;
    }

    @Override
    protected void attackGoal() {
        Bullet bullet = new Bullet(getVertexBufferObjectManager(), mEntityOperations, getBackgroundColor()
        );
        bullet.fire(getX() + SizeConstants.UNIT_SIZE / 2, getY() - Bullet.BULLET_SIZE,
                mUnitToAttack.getX(), mUnitToAttack.getY(), mEnemiesUpdater, mUnitDamage);

        mEntityOperations.attachEntity(bullet);
    }
}
