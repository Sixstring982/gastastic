package com.lunagameserve.decarbonator.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.lunagameserve.decarbonator.R;
import com.lunagameserve.decarbonator.graphics.Polaroid;
import com.lunagameserve.decarbonator.statistics.Statistics;
import com.lunagameserve.decarbonator.util.Screen;
import com.lunagameserve.decarbonator.util.UnderActivity;
import org.jetbrains.annotations.NotNull;

/**
 * Created by sixstring982 on 2/24/15.
 */
public class TripFinishActivity extends UnderActivity {

    /* Facebook stuff */
    private UiLifecycleHelper lifecycleHelper;

    private double gallons;

    private boolean driving;

    @Override
    protected String getTag() {
        return "TripFinishActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setContentView(R.layout.finish_trip_layout);

        lifecycleHelper = new UiLifecycleHelper(this, null);
        lifecycleHelper.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            addTextViews(extras);
            setupHeader(extras);
        } else {
            logError("Bundle is null!");
        }
    }

    private void setupHeader(Bundle bundle) {
        this.gallons = bundle.getDouble("gallons", 0.0);
        String galStr = String.format("%.2f", gallons);
        this.driving = bundle.getBoolean("driving");

        TextView tv = (TextView)findViewById(R.id.finish_header);
        tv.setText("You " + (driving ? "burned " : "saved ")  + galStr + " " +
                "gallons of gasoline, which is equivalent to:");
    }



    private void addTextViews(Bundle bundle) {
        this.gallons = bundle.getDouble("gallons", 0.0);
        byte[] ordinals = bundle.getByteArray("ordinals");

        Statistics[] stats = new Statistics[ordinals.length];
        for (int i = 0; i < stats.length; i++) {
            stats[i] = Statistics.fromOrdinal(ordinals[i]);
        }

        LinearLayout layout =
                (LinearLayout)findViewById(R.id.finish_internal_layout);

        for (int i = 0; i < stats.length; i++) {
            addImage(layout, stats[i], (i % 2) == 0);

            addStatisticText(gallons, layout, stats[i]);

            if (i < stats.length - 1) {
                addOrSeparator(layout);
            }
        }
    }

    private void addStatisticText(double gallons,
                                  LinearLayout layout, Statistics stat) {
        TextView tv = new TextView(getBaseContext());

        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(p);

        tv.setGravity(Gravity.CENTER);
        tv.setShadowLayer(2f, 1, 1, Color.BLACK);
        tv.setTextSize(24f);

        tv.setText(stat.getGallonString(gallons));

        layout.addView(tv);
    }

    private void addImage(LinearLayout layout, Statistics s, boolean left) {
        ImageView iv = new ImageView(getBaseContext());

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.gravity = left ? Gravity.START : Gravity.END;
        iv.setLayoutParams(p);
        iv.setImageBitmap(new Polaroid(getBaseContext(),
                                       s.getRandomDrawable())
                                                           .getRotatedBitmap());

        layout.addView(iv);
    }

    private void addOrSeparator(LinearLayout layout) {
        TextView tv = new TextView(getBaseContext());

        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(p);

        tv.setGravity(Gravity.CENTER);
        tv.setShadowLayer(2f, 1, 1, Color.BLACK);

        tv.setText("---OR---");
        tv.setTextSize(20f);

        layout.addView(tv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifecycleHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleHelper.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        lifecycleHelper.onActivityResult(requestCode, resultCode,
                                         data, new FacebookDialog.Callback() {

                    @Override
                    public void onComplete(
                            FacebookDialog.PendingCall pendingCall,
                            Bundle data) {

                    }

                    @Override
                    public void onError(FacebookDialog.PendingCall pendingCall,
                                        Exception error, Bundle data) {

                    }
                });
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        lifecycleHelper.onSaveInstanceState(outState);
    }

    public void onFacebookClick(View view) {
        String usageStr = driving ? "used " : "saved ";
        FacebookDialog dialog = new FacebookDialog.ShareDialogBuilder(this)
                .setApplicationName("Gastastic")
                .setName("Gastastic for Android")
                .setDescription("I " + usageStr +
                                String.format("%.2f", gallons) +
                                " gallons of gas, which is equivalent to " +
                                Statistics.fromOrdinal(
                                        (byte)Screen.getRand().nextInt(
                                              (Statistics.values().length)))
                                                .getGallonString(gallons))
                .setLink("https://lunagameserve.com/")
                .setPicture("https://lunagameserve.com/img/pump-handle.jpg")
                .build();
        lifecycleHelper.trackPendingDialogCall(dialog.present());
    }
}
