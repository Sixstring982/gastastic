package com.lunagameserve.decarbonator.util;

import android.util.Log;
import org.jetbrains.annotations.NotNull;

/**
 * Created by sixstring982 on 2/20/15.
 */
public abstract class TaggableActivity extends ToastActivity {

    protected abstract String getTag();

    @NotNull
    private String bracketTag() {
        return "[" + getTag() + "]:";
    }

    protected void logError(String message) {
        Log.e(bracketTag(), message);
    }

    protected void logWarning(String message) {
        Log.w(bracketTag(), message);
    }

    protected void logDebug(String message) {
        Log.d(bracketTag(), message);
    }
}
