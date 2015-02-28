package com.lunagameserve.decarbonator.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.lunagameserve.decarbonator.R;
import com.lunagameserve.decarbonator.cars.Car;
import com.lunagameserve.decarbonator.graphics.Polaroid;
import com.lunagameserve.decarbonator.graphics.TextFader;
import com.lunagameserve.decarbonator.location.GPSPath;
import com.lunagameserve.decarbonator.statistics.PolaroidSet;
import com.lunagameserve.decarbonator.statistics.StatisticSet;
import com.lunagameserve.decarbonator.statistics.StatisticType;
import com.lunagameserve.decarbonator.util.Screen;
import com.lunagameserve.decarbonator.util.UnderActivity;
import com.lunagameserve.nbt.NBTException;
import com.lunagameserve.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by sixstring982 on 2/22/15.
 */
public class TripActivity extends UnderActivity {

    @Nullable
    private Car currentCar = null;

    @NotNull
    private GPSPath currentPath = new GPSPath();

    private StatisticSet statistics;

    @NotNull
    private AtomicBoolean keepAnimating = new AtomicBoolean(false);

    @Nullable
    private Bitmap background = null;

    private Bitmap stoppedPolaroidBitmap;

    private double fakeGas = 0.0;

    private boolean driving = false;

    private TextFader backgroundFader;

    private boolean reportedConnection = false;

    private StatisticType statisticSetType;

    @NotNull
    @Override
    protected String getTag() {
        return "TripActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                  WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setContentView(R.layout.trip_activity_layout);
        setupFromIntent();
        statistics = new StatisticSet(getBaseContext(), statisticSetType);
        statistics.setOnSingleFinishMoving(onPolaroidFinishMoving());

        currentPath.setOnGPSDisconnectedListener(onGPSDisconnection());
        currentPath.setOnGPSConnectedListener(onGPSConnection());
        currentPath.setOnCollectListener(onCollectGPSPoint());
        currentPath.startCollecting(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE));

        backgroundFader =
                new TextFader((TextView)findViewById(
                        R.id.trip_begin_moving_label));
        backgroundFader.setAlpha(255);
        backgroundFader.setFadeRate(2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentPath.stopCollecting();
    }

    private void queueAnimate() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {

        }
        if (keepAnimating.get()) {
            new Thread(
                makeUIRunnable(
                    new Runnable() {
                        @Override
                        public void run() {
                            animate();
                        }
                    }
                )
            ).start();
        }
    }

    private void animate() {
        statistics.updatePolaroids();
        ImageView bg =
                (ImageView) findViewById(
                        R.id.background_trip);

        backgroundFader.fadeDown();

        if (bg.getWidth() > 0 && bg.getHeight() > 0) {
            if (background == null) {
                background = Bitmap.createBitmap(bg.getWidth(),
                             bg.getHeight(),
                             Bitmap.Config.ARGB_8888);
            }
            if (stoppedPolaroidBitmap == null) {
                stoppedPolaroidBitmap =
                        Bitmap.createBitmap(bg.getWidth(),
                        bg.getHeight(),
                        Bitmap.Config.ARGB_8888);
            }

            background.eraseColor(0);
            Canvas c = new Canvas(background);
            c.drawBitmap(stoppedPolaroidBitmap, 0, 0, null);
            statistics.renderMovingPolaroids(c);
            bg.setImageBitmap(background);
        }

        if (!statistics.canUpdate()) {
            keepAnimating.set(false);
        }

        queueAnimate();
    }

    @NotNull
    private PolaroidSet.SingleFinishMovingListener onPolaroidFinishMoving() {
        return new PolaroidSet.SingleFinishMovingListener() {
            @Override
            public void onSingleFinishMoving(final Polaroid p) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Canvas c = new Canvas(stoppedPolaroidBitmap);
                        p.renderRotatedBitmap(c);
                    }
                });
            }
        };
    }

    @NotNull
    private Runnable onGPSConnection() {
        return setGPSIcon(true);
    }

    @NotNull
    private Runnable onGPSDisconnection() {
        return setGPSIcon(false);
    }

    private Runnable setGPSIcon(final boolean visible) {
        return makeUIRunnable(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.trip_location)
                        .setVisibility(visible ?
                                View.VISIBLE : View.GONE);

                TextView tv = (TextView)findViewById(
                        R.id.trip_begin_moving_label);

                if (visible && !reportedConnection) {
                    toastLong("GPS Connected.");
                    tv.setText(R.string.gps_keep_moving);
                    reportedConnection = true;
                }
            }
        });
    }

    @NotNull
    private Runnable onCollectGPSPoint() {
        return makeUIRunnable(
            new Runnable() {
                @Override
                public void run() {
                    TextView tv =
                            (TextView) findViewById(
                                    R.id.
                                            trip_activity_layout_distancelabel);

                    tv.setText("Travelled " +
                            String.format("%.2f",
                                    currentPath.getTotalDistanceInMeters()) +
                            " meters (" +
                            String.format("%.2f",
                                    currentPath.getTotalDistanceInMiles()) +
                            " miles)");

                    TextView vel =
                            (TextView) findViewById(
                                    R.id.trip_activity_layout_velocitylabel);

                    vel.setText("Velocity: " +
                            String.format("%.2f",
                                    currentPath.getCurrentVelocity()) +
                            "km/h (" +
                            String.format("%.2f",
                                    currentPath.getCurrentVelocityMPH()) +
                            "mph)");

                    ImageView iv =
                            (ImageView) findViewById(
                                    R.id.trip_mapview);

                    iv.setImageBitmap(currentPath.renderPath());
                    iv.invalidate();

                    if (currentCar != null) {
                        double gallons = totalGallonsUsed();

                        statistics.updatePolaroids(gallons,
                                Screen.getWidth(),
                                Screen.getHeight());
                        if (statistics.canUpdate()) {
                            if (!keepAnimating.get()) {
                                keepAnimating.set(true);
                                queueAnimate();
                            }
                        }
                    }
                }
            }
        );
    }

    private void setDebugMode(final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int visibility = enabled ? View.VISIBLE : View.INVISIBLE;

                int[] ids = new int[] {
                        R.id.trip_debug,
                        R.id.trip_activity_layout_distancelabel,
                        R.id.trip_activity_layout_velocitylabel,
                        R.id.trip_mapview, R.id.trip_add_polaroid_button
                };

                for (int i : ids) {
                    findViewById(i).setVisibility(visibility);
                }
            }
        });
    }

    private void setupFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.statisticSetType =
                    StatisticType.fromOrdinal(extras.getInt("setType"));
        }
        if (extras != null && extras.get("debug") != null) {
            setDebugMode(extras.getBoolean("debug"));
        } else {
            setDebugMode(false);
        }
        if (extras != null &&
                extras.getInt("car_mpg") > 0 &&
                extras.getString("car_name") != null) {
            currentCar = new Car(extras.getString("car_name"),
                    extras.getInt("car_mpg"));
            driving = true;
            toastLong("Please remember to pay attention to the road.");
        } else {
            toastLong("No car selected. Going by US Average of 22mpg.");
            currentCar = new Car("US Average", 22);
            driving = false;
        }
    }

    private double totalGallonsUsed() {
        if (currentCar == null) {
            return 0.0;
        }
        double miles = currentPath
                .getTotalDistanceInMiles();

        double gpm = 1.0 / currentCar.getMilesPerGallon();
        return miles * gpm + fakeGas;
    }

    public void finishTrip(View view) {
        Intent intent = new Intent(getBaseContext(), TripFinishActivity.class);

        Bundle bundle = new Bundle();
        bundle.putBoolean("driving", driving);
        bundle.putDouble("gallons", totalGallonsUsed());
        bundle.putByteArray("ordinals", statistics.getStatisticOrdinals());
        intent.putExtras(bundle);

        try {
            accumulateUsage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startActivity(intent);

        finish();
    }

    private void accumulateUsage() throws IOException {
        double used = 0.0;
        double saved = 0.0;
        File usageFile = new File(getFilesDir(), "usage.nbt");
        if (!usageFile.exists()) {
            usageFile.createNewFile();
        } else {
            GZIPInputStream in =
                    new GZIPInputStream(
                            new FileInputStream(usageFile));
            try {
                Tag.Compound root = Tag.readCompound(in);

                used = root.getDouble("used");
                saved = root.getDouble("saved");

            } catch (NBTException e ) {
                e.printStackTrace();
            } finally {
                in.close();
            }
        }

        Tag.Compound.Builder builder = new Tag.Compound.Builder();
        if (driving) {

            builder.addDouble("used", used + totalGallonsUsed())
                    .addDouble("saved", saved);
        } else {
            builder.addDouble("saved", saved + totalGallonsUsed())
                    .addDouble("used", used);
        }

        GZIPOutputStream out =
                new GZIPOutputStream(
                        new FileOutputStream(usageFile));
        try {
            builder.toCompound("usage").writeNamed(out);
        } catch (NBTException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    public void addPolaroids(View view) {
        statistics.updatePolaroids(fakeGas += 0.1,
                                   Screen.getWidth(), Screen.getHeight());
        if (statistics.canUpdate()) {
            if (!keepAnimating.get()) {
                keepAnimating.set(true);
                queueAnimate();
            }
        }
    }
}
