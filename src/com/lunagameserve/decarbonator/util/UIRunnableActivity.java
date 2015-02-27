package com.lunagameserve.decarbonator.util;

import android.app.Activity;

/**
 * Created by sixstring982 on 2/26/15.
 */
public class UIRunnableActivity extends Activity {
    protected Runnable makeUIRunnable(final Runnable runnable) {
        return new Runnable() {
            @Override
            public void run() {
                runOnUiThread(runnable);
            }
        };
    }
}
