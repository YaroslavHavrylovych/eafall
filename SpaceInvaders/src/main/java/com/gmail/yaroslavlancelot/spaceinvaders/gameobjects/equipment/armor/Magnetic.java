package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;

public class Magnetic extends Armor {
    public static final ArmorType ARMOR_TYPE = ArmorType.MAGNETIC;

    public Magnetic(final int armorValue) {
        super(armorValue);
    }

    @Override
    protected int getDamageAfterConsumption(Damage damage) {
        Damage.DamageType damageType = damage.getDamageType();
        if(damageType.equals(Damage.DamageType.ANNIHILATOR))
            return (int) (damage.getDamageValue() * .8);
        if(damageType.equals(Damage.DamageType.ELECTRICAL))
            return (int) (damage.getDamageValue() * .85);
        if(damageType.equals(Damage.DamageType.MAGNETIC))
            return (int) (damage.getDamageValue() * 1.05);
        if(damageType.equals(Damage.DamageType.NEUTRINO))
            return (int) (damage.getDamageValue() * .9);
        if(damageType.equals(Damage.DamageType.HIGGS))
            return (int) (damage.getDamageValue() * 1.0);
        throw new IllegalArgumentException("unsupported damage type");
    }

    @Override
    public ArmorType getArmorType() {
        return ARMOR_TYPE;
    }
}
