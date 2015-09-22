package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.general.locale.Locale;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

/**
 * Represent unit armor. Used for calculate damage to unit depends on {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor.ArmorType}
 * and {@link com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage.DamageType}.
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
        this(ArmorType.valueOf(armorType.toUpperCase()), armorValue);
    }

    public Armor(ArmorType armorType, final int armorValue) {
        mArmorValue = armorValue;
        mArmorType = armorType;
    }

    public int getArmorValue() {
        return mArmorValue;
    }

    public ArmorType getArmorType() {
        return mArmorType;
    }

    public String getString() {
        Locale locale = LocaleImpl.getInstance();
        int val;
        switch (mArmorType) {
            case UNARMORED:
                val = R.string.armor_unarmored;
                break;
            case PHYSICAL:
                val = R.string.armor_physical;
                break;
            case ELECTROMAGNETIC:
                val = R.string.armor_electromagnetic;
                break;
            case CHERENKOV:
                val = R.string.armor_cherenkov;
                break;
            case HIGGS_SHIELD:
                val = R.string.armor_higgs;
                break;
            case MIXED:
            default:
                val = R.string.armor_mixed;
        }
        return locale.getStringById(val);
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

    public enum ArmorType {
        UNARMORED, PHYSICAL, ELECTROMAGNETIC, CHERENKOV, HIGGS_SHIELD, MIXED
    }
}
