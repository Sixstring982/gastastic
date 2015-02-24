package com.lunagameserve.decarbonator.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Created by sixstring982 on 2/21/15.
 */
public class BitmapUtil {
    public static Bitmap fromResource(@NotNull Context ctx, int resource) {
        return BitmapFactory.decodeResource(ctx.getResources(), resource);
    }
}
