package com.lunagameserve.decarbonator.statistics;

import android.content.Context;
import android.graphics.Canvas;
import com.lunagameserve.decarbonator.util.Screen;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by sixstring982 on 2/23/15.
 */
public class StatisticSet {

    @NotNull
    private List<PolaroidSet> sets = new ArrayList<PolaroidSet>();

    public StatisticSet(Context ctx, int amount) {
        initStatistics(ctx, amount);
    }

    private void initStatistics(Context ctx, int amount) {
        if (amount == 0) {
            for (Statistics s : Statistics.values()) {
                sets.add(new PolaroidSet(ctx, s));
            }
        } else if (amount > Statistics.values().length) {
            throw new IllegalArgumentException("Too many stats.");
        } else {
            List<Statistics> set = new ArrayList<Statistics>();
            Collections.addAll(set, Statistics.values());
            Collections.shuffle(set);
            for (int i = 0; i < amount; i++) {
                sets.add(new PolaroidSet(ctx, set.get(i)));
            }
        }
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

    public void renderStoppedPolaroids(@NotNull Canvas c) {
        for (PolaroidSet set : sets) {
            set.renderStoppedPolaroids(c);
        }
    }

    public void renderMovingPolaroids(@NotNull Canvas c) {
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
