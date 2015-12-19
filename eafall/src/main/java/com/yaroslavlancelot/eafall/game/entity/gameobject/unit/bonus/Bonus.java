package com.yaroslavlancelot.eafall.game.entity.gameobject.unit.bonus;

import com.yaroslavlancelot.eafall.general.SelfCleanable;

import java.util.Set;
import java.util.WeakHashMap;

/**
 * Existing bonuses holder. Use {@link Bonus#getBonus(int, BonusType, boolean)} to reuse
 * existing bonuses instead of creating new. Each bonus have it's bonus value, type and
 * can this bonus be imposed with other bonuses or not.
 */
public class Bonus extends SelfCleanable {
    /** cache of existing game bonuses */
    private static WeakHashMap<String, Bonus> mBonuses;
    /** bonus value (e.g. additional damage, or defence or etc depending on type) */
    private int mValue;
    /** current bonus type */
    private BonusType mBonusType;
    /**
     * Decides can current bonus be imposed with other bonuses <br/>
     * General example for this is player bonuses. They are for all units and
     * unit can have other bonuses at the same time which are not coupled together.
     */
    private boolean mImposed;

    private Bonus(int value, BonusType bonusType, boolean imposed) {
        mValue = value;
        mBonusType = bonusType;
        mImposed = imposed;
    }

    /** create new or reuse existing one bonus */
    public static Bonus getBonus(int value, BonusType bonusType, boolean imposed) {
        if (mBonuses == null) {
            mBonuses = new WeakHashMap<String, Bonus>(20);
        }
        Bonus bonus = mBonuses.get(toString(value, bonusType, imposed));
        if (bonus == null) {
            bonus = new Bonus(value, bonusType, imposed);
            mBonuses.put(bonus.toString(), bonus);
        }
        return bonus;
    }

    /** to have to string same for instantiated objects and just anyone who needs some kind of this toString() */
    public static String toString(int value, BonusType bonusType, boolean imposed) {
        if (imposed) {
            return "i" + bonusType.toString() + Integer.toString(value);
        } else {
            return "n" + bonusType.toString() + Integer.toString(value);
        }
    }

    @Override
    public String toString() {
        return toString(mValue, mBonusType, mImposed);
    }

    /**
     * Filtering bonuses by bonus type and return bonus value based on existingValue and calculate sum
     * value from imposed and not imposed bonuses.
     *
     * @param bonuses       given set of bonuses
     * @param bonusType     bonus type to filter bonuses
     * @param existingValue value without bonuses
     * @return bonus value for give bonus type with filtering bonuses for suitable bonus type.
     * Calculate bonuses which imposed one another and bonuses which are not and return biggest of this.
     */
    public static int getBonusByType(Set<Bonus> bonuses, BonusType bonusType, int existingValue) {
        int imposed = 0, notImposed = 0;
        int increasingValue;
        for (Bonus bonus : bonuses) {
            if (!bonus.mBonusType.isSameType(bonusType)) {
                continue;
            }
            if (bonus.mBonusType.percent()) {
                increasingValue = ((int) (existingValue * (((float) bonus.mValue) / 100)));
            } else {
                increasingValue = bonus.mValue;
            }
            if (bonus.mImposed) {
                imposed += increasingValue;
            } else {
                notImposed = increasingValue;
            }
        }
        return imposed + notImposed;
    }

    @Override
    public void clear() {
        mBonuses.clear();
    }

    /** existing bonuses */
    public static enum BonusType {
        ATTACK, DEFENCE, HEALTH,
        ATTACK_PERCENTS, DEFENCE_PERCENTS, HEALTH_PERCENTS,
        AVOID_ATTACK_CHANCE;

        public boolean percent() {
            return this == ATTACK_PERCENTS || this == DEFENCE_PERCENTS;
        }

        /**
         * return true if both bonuses of same type (e.g. attack, or defence) without
         * taking a look both they percent increase or value increase
         */
        public boolean isSameType(BonusType bonusType) {
            if (this == bonusType) {
                return true;
            }
            if (toString().startsWith("DEFENCE") && bonusType.toString().startsWith("DEFENCE")) {
                return true;
            } else if (toString().startsWith("ATTACK") && bonusType.toString().startsWith("ATTACK")) {
                return true;
            } else if (bonusType.toString().contains("AVOID_ATTACK")) {
                return true;
            }
            return false;
        }
    }
}
