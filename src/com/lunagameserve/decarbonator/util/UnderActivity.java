package com.lunagameserve.decarbonator.util;

import android.content.Intent;

/**
 * Created by sixstring982 on 2/21/15.
 */
public abstract class UnderActivity extends TaggableActivity {

    protected void pushActivity(Class newActivity) {
        Intent intent = new Intent(this, newActivity);
        startActivity(intent);
    }

}
