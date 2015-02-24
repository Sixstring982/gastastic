package com.lunagameserve.decarbonator.util;

import android.os.Bundle;
import org.jetbrains.annotations.NotNull;

/**
 * Created by sixstring982 on 2/22/15.
 */
public abstract class OpeningAnimationActivity extends UnderActivity {

    private boolean ranOpening = false;

    public abstract void onFirstOpen();

    @Override
    protected void onCreate(@NotNull Bundle savedInstance) {
        super.onCreate(savedInstance);

        ranOpening = savedInstance.getBoolean("ranOpening", false);

        if (!ranOpening) {
            onFirstOpen();
            ranOpening = true;
        }
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("ranOpening", ranOpening);
    }
}
