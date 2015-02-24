package com.lunagameserve.decarbonator.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import com.lunagameserve.decarbonator.R;
import com.lunagameserve.decarbonator.util.UnderActivity;
import org.jetbrains.annotations.NotNull;

/**
 * Created by sixstring982 on 2/20/15.
 */
public class SplashActivity extends UnderActivity {
    @NotNull
    @Override
    protected String getTag() {
        return "SplashActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logDebug("onCreate called.");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                  WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_activity_layout);
    }

    @Override
    public boolean onTouchEvent(@NotNull MotionEvent event) {
        boolean superTouch =  super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pushActivity(TripSelectActivity.class);
        }
        return superTouch;
    }

    @Override
    protected void onResume() {
        super.onResume();
        logDebug("onResume called.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logDebug("onPause called.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logDebug("onDestroy called.");
        finish();
    }
}
