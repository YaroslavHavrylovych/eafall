package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons;

public class Higgs extends Damage {
    public static final DamageType DAMAGE_TYPE = DamageType.HIGGS;

    public Higgs(final int damageValue) {
        super(damageValue);
    }

    @Override
    public DamageType getDamageType() {
        return DAMAGE_TYPE;
    }
}
