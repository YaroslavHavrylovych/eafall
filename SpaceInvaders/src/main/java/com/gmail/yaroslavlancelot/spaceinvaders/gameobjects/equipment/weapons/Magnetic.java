package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons;

public class Magnetic extends Damage {
    public static final DamageType DAMAGE_TYPE = DamageType.MAGNETIC;

    public Magnetic(final int damageValue) {
        super(damageValue);
    }

    @Override
    public DamageType getDamageType() {
        return DAMAGE_TYPE;
    }
}
