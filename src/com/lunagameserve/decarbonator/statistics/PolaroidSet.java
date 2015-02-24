package com.lunagameserve.decarbonator.statistics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.lunagameserve.decarbonator.graphics.Polaroid;
import com.lunagameserve.decarbonator.physics.PhysUtil;
import com.lunagameserve.decarbonator.util.Screen;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sixstring982 on 2/23/15.
 */
public class PolaroidSet {

    private Statistics statistic;

    @NotNull
    private List<Polaroid> gennedPolaroids = new ArrayList<Polaroid>();

    private final Context context;

    private Runnable onSingleFinishMoving;

    public PolaroidSet(Context context, Statistics stat) {
        this.context = context;
        this.statistic = stat;
    }

    public void setOnSingleFinishMoving(Runnable onSingleFinishMoving) {
        this.onSingleFinishMoving = onSingleFinishMoving;
    }

    public void updatePolaroids(double gallons, int maxX, int maxY) {
        double targetPolaroids = statistic.gallonRate() * gallons;
        while (targetPolaroids > (gennedPolaroids.size() + 1)) {
            addNewPolaroid(maxX, maxY);
        }
        updatePolaroids();
    }

    public boolean canUpdate() {
        for (Polaroid p : gennedPolaroids) {
            if (p.isMoving()) {
                return true;
            }
        }
        return false;
    }

    public void updatePolaroids() {
        for (Polaroid p : gennedPolaroids) {
            if (p.isMoving()) {
                p.update(null);
                if (!p.isMoving() &&
                    onSingleFinishMoving != null) {
                    onSingleFinishMoving.run();
                }
            }
        }
    }

    private void addNewPolaroid(int maxX, int maxY) {
        Polaroid p = new Polaroid(context,
                statistic.getRandomDrawable());
        p.setPosition(Screen.getRand().nextInt(maxX),
                      Screen.getRand().nextInt(maxY));
        p.setRotationVelocity(1f, 0.5f);
        p.setPosition(Screen.getWidth() / 2f,
                      Screen.getHeight());
        float newSpeed = 5f + 300f * Screen.getRand().nextFloat();
        switch (Screen.getRand().nextInt(3)) {
            case 0:
                    p.setPosition(Screen.getWidth() / 2f, Screen.getHeight());
                    p.setVelocity(-PhysUtil.PIOver2, PhysUtil.PIOver4,
                                  newSpeed);
                break;
            case 1:
                p.setPosition(Screen.getWidth(), Screen.getHeight());
                p.setVelocity(-(PhysUtil.PIOver2 + PhysUtil.PIOver4),
                              PhysUtil.PIOver8 + PhysUtil.PIOver16,
                              newSpeed);
                break;
            default:
                p.setPosition(0, Screen.getHeight());
                p.setVelocity(-PhysUtil.PIOver4,
                              PhysUtil.PIOver8 + PhysUtil.PIOver16,
                              newSpeed);
                break;
        }
        gennedPolaroids.add(p);
    }

    public void renderPolaroids(@NotNull Canvas c) {
        for (Polaroid p : gennedPolaroids) {
            Bitmap rotated = p.getRotatedBitmap();
            c.drawBitmap(rotated,
                         p.getX() - rotated.getWidth() / 2f,
                         p.getY() - rotated.getHeight() / 2f,
                         null);
        }
    }

    public void renderStoppedPolaroids(@NotNull Canvas c) {
        for (Polaroid p : gennedPolaroids) {
            if (!p.isMoving()) {
                Bitmap rotated = p.getRotatedBitmap();
                c.drawBitmap(rotated,
                        p.getX() - rotated.getWidth() / 2f,
                        p.getY() - rotated.getHeight() / 2f,
                        null);
            }
        }
    }

    public void renderMovingPolaroids(@NotNull Canvas c) {
        for (Polaroid p : gennedPolaroids) {
            if (p.isMoving()) {
                Bitmap rotated = p.getRotatedBitmap();
                c.drawBitmap(rotated,
                        p.getX() - rotated.getWidth() / 2f,
                        p.getY() - rotated.getHeight() / 2f,
                        null);
            }
        }
    }
}
