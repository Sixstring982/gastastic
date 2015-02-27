package com.lunagameserve.decarbonator.util;

import android.widget.Toast;

/**
 * Created by sixstring982 on 2/22/15.
 */
public class ToastActivity extends UIRunnableActivity {

    protected void toastLong(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    protected void toastShort(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
}
