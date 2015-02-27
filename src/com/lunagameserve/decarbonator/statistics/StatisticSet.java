package com.lunagameserve.decarbonator.statistics;

import android.content.Context;
import android.graphics.Canvas;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sixstring982 on 2/23/15.
 */
public class StatisticSet {

    @NotNull
    private List<PolaroidSet> sets = new ArrayList<PolaroidSet>();

    public StatisticSet(Context ctx, StatisticType type) {
        initStatistics(ctx, type);
    }

    private void initStatistics(Context ctx, StatisticType type) {
        for (Statistics s : Statistics.suitableStatistics(type)) {
            sets.add(new PolaroidSet(ctx, s));
        }
    }

    public byte[] getStatisticOrdinals() {
        byte[] ordinals = new byte[sets.size()];
        int i = 0;
        for (PolaroidSet p : sets) {
            ordinals[i++] = (byte)p.getStatistic().ordinal();
        }
        return ordinals;
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

    public void setOnSingleFinishMoving(
            PolaroidSet.SingleFinishMovingListener onSingleFinishMoving) {
        for (PolaroidSet set : sets) {
            set.setOnSingleFinishMoving(onSingleFinishMoving);
        }
    }
}
