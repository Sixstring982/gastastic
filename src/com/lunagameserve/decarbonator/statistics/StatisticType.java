package com.lunagameserve.decarbonator.statistics;

/**
 * Created by sixstring982 on 2/26/15.
 */
public enum StatisticType {
    Walk {
        @Override
        public boolean isSuitableFor(double gallonRate) {
            return true;
        }
    },
    Bike {
        @Override
        public boolean isSuitableFor(double gallonRate) {
            return gallonRate < 750;
        }
    },
    Drive {
        @Override
        public boolean isSuitableFor(double gallonRate) {
            return gallonRate < 250;
        }
    };

    public abstract boolean isSuitableFor(double gallonRate);

    public static StatisticType fromOrdinal(int ordinal) {
        for (StatisticType t : values()) {
            if (t.ordinal() == ordinal) {
                return t;
            }
        }
        return null;
    }
}
