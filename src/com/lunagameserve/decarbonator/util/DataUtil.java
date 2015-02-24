package com.lunagameserve.decarbonator.util;

import org.jetbrains.annotations.NotNull;

/**
 * Created by sixstring982 on 2/22/15.
 */
public class DataUtil {

    public static boolean isDigits(@NotNull String intVal) {
        for (int i = 0; i < intVal.length(); i++) {
            if (intVal.charAt(i) < '0' ||
                intVal.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    public static long milliTime() {
        return System.nanoTime() / 1000000;
    }

    public static double metersToKilometers(double meters) {
        return meters / 1000.0;
    }

    public static double millisecondsToHours(double milliseconds) {
        return milliseconds / (1000.0 * 60.0 * 60.0);
    }
}
