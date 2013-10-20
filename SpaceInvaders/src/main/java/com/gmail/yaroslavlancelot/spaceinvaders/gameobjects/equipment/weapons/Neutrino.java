package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons;

public class Neutrino extends Damage {
    public static final Damage.DamageType DAMAGE_TYPE = DamageType.NEUTRINO;

    public Neutrino(final int damageValue) {
        super(damageValue);
    }

    @Override
    public Damage.DamageType getDamageType() {
        return DAMAGE_TYPE;
    }
}
