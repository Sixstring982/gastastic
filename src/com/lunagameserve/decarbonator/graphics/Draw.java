package com.lunagameserve.decarbonator.graphics;

import android.graphics.Bitmap;

/**
 * Created by sixstring982 on 2/21/15.
 */
public class Draw {

    private Bitmap bmp;
    private int color;

    public Draw(Bitmap bmp, int color) {
        this.bmp = bmp;
        this.color = color;
    }

    public Draw setColor(int color) {
        this.color = color;
        return this;
    }

    public Draw rect(int x, int y, int w, int h) {
        for (int xx = x; xx < x + w; xx++) {
            for (int yy = y; yy < y + h; yy++) {
                bmp.setPixel(xx, yy, color);
            }
        }
        return this;
    }

    public Draw clear() {
        rect(0, 0, bmp.getWidth(), bmp.getHeight());
        return this;
    }

    public Draw image(Bitmap src, int x, int y, int w, int h) {
        float dx = src.getWidth() / w;
        float dy = src.getHeight() / h;
        for (int xx = x; xx < x + w; xx++) {
            for (int yy = y; yy < y + h; yy++) {
                bmp.setPixel(xx, yy,
                             src.getPixel((int)(dx * (xx - x)),
                                          (int)(dy * (yy - y))));
            }
        }
        return this;
    }
}
