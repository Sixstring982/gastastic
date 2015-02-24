package com.lunagameserve.decarbonator.graphics;

import android.widget.TextView;

/**
 * Created by sixstring982 on 2/21/15.
 */
public class TextFader {

    private TextView textView;

    private int fadeRate = 10;

    private int alpha = 0;

    public TextFader(TextView textView) {
        this.textView = textView;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setFadeRate(int fadeRate) {
        this.fadeRate = fadeRate;
    }

    public boolean fadeUp() {
        alpha += fadeRate;
        if (alpha > 255) {
            alpha = 255;
            return false;
        }
        setTextColor();
        return true;
    }

    public void fadeDown() {
        alpha -= fadeRate;
        if (alpha < 0) {
            alpha = 0;
        }
        setTextColor();
    }

    private void setTextColor() {
        int color = ((alpha & 0xff) << 24) |
                    (textView.getCurrentTextColor() & 0xffffff);
        textView.setTextColor(color);
    }
}
