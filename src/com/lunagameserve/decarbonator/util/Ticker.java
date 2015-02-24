package com.lunagameserve.decarbonator.util;

import android.util.Log;

/**
 * Created by sixstring982 on 2/21/15.
 */
public class Ticker {

    private int triggerTicks;

    private int ticks = 0;

    private Runnable onTrigger;

    public Ticker(int triggerTicks) {
        this.triggerTicks = triggerTicks;
    }

    public void setTrigger(Runnable onTrigger) {
        this.onTrigger = onTrigger;
    }

    public void tick() {
        ticks++;
        if (ticks > triggerTicks) {
            if (this.onTrigger != null) {
                this.onTrigger.run();
            } else {
                Log.w("Ticker", "Ticker onTrigger has not been set.");
            }
        }
    }
}
