package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons;

public class Electric extends Damage {
    public static final DamageType DAMAGE_TYPE = DamageType.ELECTRICAL;

    public Electric(final int damageValue) {
        super(damageValue);
    }

    @Override
    public DamageType getDamageType() {
        return DAMAGE_TYPE;
    }
}
