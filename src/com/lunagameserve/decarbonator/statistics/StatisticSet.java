package com.lunagameserve.decarbonator.statistics;

import android.content.Context;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sixstring982 on 2/23/15.
 */
public class StatisticSet {

    private List<PolaroidSet> sets = new ArrayList<PolaroidSet>();

    public StatisticSet(Context ctx, int amount) {
        initStatistics(ctx, amount);
    }

    private void initStatistics(Context ctx, int amount) {
        sets.add(new PolaroidSet(ctx, Statistics.Human));
        sets.add(new PolaroidSet(ctx, Statistics.PhoneCharges));
        sets.add(new PolaroidSet(ctx, Statistics.TShirts));
        sets.add(new PolaroidSet(ctx, Statistics.Showers));
    }

    public boolean canUpdate() {
        for (PolaroidSet set : sets) {
            if (set.canUpdate()) {
                return true;
            }
        }
        return false;
    }

    public void updatePolaroids(double totalGallons,
                                int maxX, int maxY) {
        for (PolaroidSet set : sets) {
            set.updatePolaroids(totalGallons, maxX, maxY);
        }
    }

    public void updatePolaroids() {
        for (PolaroidSet set : sets) {
            set.updatePolaroids();
        }
    }

    public void renderStoppedPolaroids(Canvas c) {
        for (PolaroidSet set : sets) {
            set.renderStoppedPolaroids(c);
        }
    }

    public void renderMovingPolaroids(Canvas c) {
        for (PolaroidSet set : sets) {
            set.renderMovingPolaroids(c);
        }
    }

    public void setOnSingleFinishMoving(Runnable onSingleFinishMoving) {
        for (PolaroidSet set : sets) {
            set.setOnSingleFinishMoving(onSingleFinishMoving);
        }
    }
}
