package com.lunagameserve.decarbonator.statistics;

import com.lunagameserve.decarbonator.R;
import com.lunagameserve.decarbonator.util.Screen;
import org.jetbrains.annotations.NotNull;

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
    },
    PhoneCharges {
        @Override
        protected double gallonRate() {
            return 1111.11111111;
        }

        @NotNull
        @Override
        protected int[] getDrawables() {
            return new int[] {
                R.drawable.phone1,
                R.drawable.phone2
            };
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
                    R.drawable.shower1,
                    R.drawable.shower2
            };
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

    public int getRandomDrawable() {
        int[] ds = getDrawables();
        int id = Screen.getRand().nextInt(ds.length);
        return ds[id];
    }
}
