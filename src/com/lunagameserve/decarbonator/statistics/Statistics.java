package com.lunagameserve.decarbonator.statistics;

import com.lunagameserve.decarbonator.R;
import com.lunagameserve.decarbonator.util.Screen;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by sixstring982 on 2/23/15.
 */
public enum Statistics {
    Human {
        @Override
        protected double gallonRate() {
            return 15.5;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                R.drawable.human,
                R.drawable.human2
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            return "The chemical energy needed to feed a human for " +
                    used + " days";
        }
    },
    PhoneCharges {
        @Override
        protected double gallonRate() {
            return 1111.1111;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                R.drawable.phone1,
                R.drawable.phone2
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            double usedNum = Double.parseDouble(used) * 1;
            String finalStr = String.format("%.2f", usedNum);
            return "Enough electrical energy to charge a cell phone " +
                    finalStr + " times";
        }
    },
    TShirts {
        @Override
        protected double gallonRate() {
            return 13.687;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                R.drawable.tshirt1,
                R.drawable.tshirt2
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            return "The carbon emitted when creating "
                    + used + " T-shirts";
        }
    },
    Showers {
        @Override
        protected double gallonRate() {
            return 15.384;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                R.drawable.shower1,
                R.drawable.shower2
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            return "The carbon emitted when heating " + used + " showers";
        }
    },
    MeatDiet {
        @Override
        protected double gallonRate() {
            return 1.237;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                    R.drawable.steak1
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            return "The carbon emitted by eating a meat diet for " +
                    used + " days";
        }
    },
    VegetarianDiet {
        @Override
        protected double gallonRate() {
            return 2.344;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                    R.drawable.veggie1
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            return "The carbon emitted by eating a vegetarian diet for " +
                    used + " days";
        }
    },
    VeganDiet {
        @Override
        protected double gallonRate() {
            return 3.072;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                    R.drawable.vegan1
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            return "The carbon emitted by eating a vegan diet for " +
                    used + " days";
        }
    },
    IncandescentLighting {
        @Override
        protected double gallonRate() {
            return 23.2;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                    R.drawable.icbulb
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            return "The electricity usage of an incandescent light bulb if " +
                    "left on for " + used + " days";
        }
    },
    CFLighting {
        @Override
        protected double gallonRate() {
            return 15.0;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                    R.drawable.cfbulb
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            return "The electricity usage of a compact florescent light bulb" +
                    " if left on for " + used + " weeks";
        }
    },
    GolfHole {
        @Override
        protected double gallonRate() {
            return 45.0;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                    R.drawable.golfer
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            return "The chemical energy used in " + used +
                    " holes of golf";
        }
    },
    DailyHomeUsage {
        @Override
        protected double gallonRate() {
            return 1.01;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                    R.drawable.house
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            return "The electrical energy used in " + used +
                    " average households per day";
        }
    },
    M2Minutes {
        @Override
        protected double gallonRate() {
            return 17.568;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                    R.drawable.m2
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            return "The energy produced by firing an M2 machine gun for " +
                    used + " minutes";
        }
    },
    TonTNT {
        @Override
        protected double gallonRate() {
            return 0.031;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                    R.drawable.tnt
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            return "The energy contained in " + used + " metric tons of TNT";
        }
    },
    BikeHours {
        @Override
        protected double gallonRate() {
            return 52.54;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                    R.drawable.biking1
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            return "The chemical energy used when biking for "
                    + used + " hours";
        }
    },
    WalkHours {
        @Override
        protected double gallonRate() {
            return 67.39;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                    R.drawable.walking
            };
        }

        @NotNull
        @Override
        public String generateGallonString(String used) {
            return "The energy used when walking for "
                    + used + " hours";
        }
    };


    /**
     * The number of polaroids which should be generated per gallon
     * of gas burned.
     *
     * @return
     */
    protected abstract double gallonRate();

    @NotNull
    protected abstract int[] getDrawables();

    protected abstract String generateGallonString(String used);

    @NotNull
    public String getGallonString(double gallons) {
        return generateGallonString(
                String.format("%.2f",gallons * gallonRate()));
    }

    public int getRandomDrawable() {
        int[] ds = getDrawables();
        int id = Screen.getRand().nextInt(ds.length);
        return ds[id];
    }

    public static Statistics fromOrdinal(byte ordinal) {
        for (Statistics s : values()) {
            if (s.ordinal() == ordinal) {
                return s;
            }
        }
        throw new IllegalArgumentException("Statistics[" + ordinal + "] DNE.");
    }

    private boolean isSuitableFor(StatisticType type) {
        return type.isSuitableFor(gallonRate());
    }

    public static ArrayList<Statistics> suitableStatistics(StatisticType type) {
        ArrayList<Statistics> stats = new ArrayList<Statistics>();
        for (Statistics s : values()) {
            if (s.isSuitableFor(type)) {
                stats.add(s);
            }
        }
        return stats;
    }
}
