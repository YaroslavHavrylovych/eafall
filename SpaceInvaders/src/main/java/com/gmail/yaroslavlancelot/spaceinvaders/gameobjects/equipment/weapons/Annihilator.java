package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons;

public class Annihilator extends Damage {
    public static final DamageType DAMAGE_TYPE = DamageType.ANNIHILATOR;

    public Annihilator(final int damageValue) {
        super(damageValue);
    }

    @Override
    public DamageType getDamageType() {
        return DAMAGE_TYPE;
    }
}
