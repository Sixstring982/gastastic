package com.lunagameserve.decarbonator.util;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Created by sixstring982 on 2/21/15.
 */
public class Screen {

    private static int HEIGHT;
    private static int WIDTH;
    private static float LOGICAL_DENSITY;

    @NotNull
    private static Random rand = new Random(System.nanoTime());

    private static boolean initialized = false;

    public static void initializeSizes(@NotNull Context ctx) {
        if (!initialized) {
            Point devDims = new Point();
            DisplayMetrics metrics = new DisplayMetrics();

            WindowManager wm =
                    (WindowManager)
                            ctx.getSystemService(Context.WINDOW_SERVICE);

            wm.getDefaultDisplay().getSize(devDims);

            HEIGHT = devDims.y;
            WIDTH = devDims.x;

            wm.getDefaultDisplay().getMetrics(metrics);

            LOGICAL_DENSITY = metrics.density;

            initialized = true;
        }
    }

    public static int getWidth() {
        if (initialized) {
            return WIDTH;
        } else {
            throw new IllegalStateException("Screen.WIDTH has never been " +
                                            "initialized. Use initializeSizes" +
                                            "() first.");
        }
    }

    public static float getWidthDP() {
        return pxToDP(getWidth());
    }

    public static int getHeight() {
        if (initialized) {
            return HEIGHT;
        } else {
            throw new IllegalStateException("Screen.WIDTH has never been " +
                    "initialized. Use initializeSizes" +
                    "() first.");
        }
    }

    public static float getHeightDP() {
        return pxToDP(getHeight());
    }

    @NotNull
    public static Random getRand() {
        return rand;
    }

    public static float dpToPx(float dp) {
        return (float)Math.ceil(LOGICAL_DENSITY * dp);
    }

    public static float pxToDP(float px) {
        return (int)(px / LOGICAL_DENSITY);
    }
}
