package com.lunagameserve.decarbonator.util;

/**
 * Created by sixstring982 on 2/22/15.
 */
public class DataUtil {

    public static boolean isDigits(String ival) {
        for (int i = 0; i < ival.length(); i++) {
            if (ival.charAt(i) < '0' ||
                ival.charAt(i) > '9') {
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
