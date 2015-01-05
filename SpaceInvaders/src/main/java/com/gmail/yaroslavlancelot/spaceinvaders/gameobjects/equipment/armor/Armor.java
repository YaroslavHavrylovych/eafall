package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;

/**
 * Represent unit armor. Used for calculate damage to unit depends on {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Armor.ArmorType}
 * and {@link com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage.DamageType}.
 */
public class Armor {
    private static final float[][] sArmorSafetyTable = new float[][]{
            new float[]{1.5f, .85f, 1f, 1.2f, .45f, .45f},
            new float[]{1.2f, .9f, 1.2f, .7f, 1f, .7f},
            new float[]{1f, 1.05f, 1.4f, .6f, .95f, .6f},
            new float[]{1f, 1f, 1f, 1f, 1f, 1f},
            new float[]{1f, 1.1f, .75f, .95f, 1.2f, .75f},
            new float[]{.3f, .5f, .45f, .6f, .5f, 1.75f}
    };
    private int mArmorValue;
    private volatile int mAdditionalArmor;
    private ArmorType mArmorType;

    public Armor(String armorType, final int armorValue) {
        mArmorValue = armorValue;
        mArmorType = ArmorType.valueOf(armorType.toUpperCase());
    }

    public void setAdditionalArmor(int additionalArmor) {
        mAdditionalArmor = additionalArmor;
    }

    public int getDamage(Damage damage) {
        int realDamage = getDamageAfterConsumption(damage);
        return realDamage * realDamage / (realDamage + mArmorValue + mAdditionalArmor) + 1;
    }

    protected int getDamageAfterConsumption(Damage damage) {
        return (int) ((damage.getDamageValue() + damage.getAdditionalDamage()) *
                sArmorSafetyTable[damage.getDamageType().ordinal()][mArmorType.ordinal()]);
    }

    public int getArmorValue() {
        return mArmorValue;
    }

    public ArmorType getArmorType() {
        return mArmorType;
    }

    public static enum ArmorType {
        UNARMORED, PHYSICAL, ELECTROMAGNETIC, CHERENKOV, HIGGS_SHIELD, MIXED;
    }
}
