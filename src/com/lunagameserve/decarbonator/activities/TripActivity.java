package com.lunagameserve.decarbonator.activities;

import android.content.Context;
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
import com.lunagameserve.decarbonator.location.GPSPath;
import com.lunagameserve.decarbonator.statistics.StatisticSet;
import com.lunagameserve.decarbonator.util.Screen;
import com.lunagameserve.decarbonator.util.UnderActivity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

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
        setupCurrentCar();
        statistics = new StatisticSet(getBaseContext(), 0);
        statistics.setOnSingleFinishMoving(onPolaroidFinishMoving());

        currentPath.setOnGPSDisconnectedListener(onGPSDisconnection());
        currentPath.setOnGPSConnectedListener(onGPSConnection());
        currentPath.setOnCollectListener(onCollectGPSPoint());
        currentPath.startCollecting(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentPath.stopCollecting();
    }

    @NotNull
    private Runnable onGPSConnection() {
        return new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv =
                                (TextView)findViewById(
                                        R.id.
                                        trip_activity_layout_connectionlabel);

                        tv.setText(R.string.gps_connected);
                    }
                });
            }
        };
    }

    private void queueAnimate() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {

        }
        if (keepAnimating.get()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            animate();
                        }
                    });
                }
            }).start();
        }
    }

    private void animate() {
        statistics.updatePolaroids();
        ImageView bg =
                (ImageView) findViewById(
                        R.id.background_trip);

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

        queueAnimate();
    }

    @NotNull
    private Runnable onPolaroidFinishMoving() {
        return new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Canvas c = new Canvas(stoppedPolaroidBitmap);
                        statistics.renderStoppedPolaroids(c);
                    }
                });
            }
        };
    }

    @NotNull
    private Runnable onGPSDisconnection() {
        return new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv =
                                (TextView)findViewById(
                                        R.id.
                                        trip_activity_layout_connectionlabel);

                        tv.setText(R.string.gps_waiting);
                    }
                });
            }
        };
    }

    @NotNull
    private Runnable onCollectGPSPoint() {
        return new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv =
                                (TextView)findViewById(
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
                                (TextView)findViewById(
                                       R.id.trip_activity_layout_velocitylabel);

                        vel.setText("Velocity: " +
                                String.format("%.2f",
                                        currentPath.getCurrentVelocity()) +
                                "km/h (" +
                                String.format("%.2f",
                                        currentPath.getCurrentVelocityMPH()) +
                                "mph)");

                        TextView lat =
                                (TextView)findViewById(
                                        R.id.
                                        trip_activity_layout_latitudelabel);

                        lat.setText("Latitude: " +
                                currentPath.getCurrentLatitude());

                        TextView lon =
                                (TextView)findViewById(
                                        R.id.
                                        trip_activity_layout_longitudelabel);

                        lon.setText("Longitude: " +
                                currentPath.getCurrentLongitude());

                        ImageView iv =
                                (ImageView)findViewById(
                                        R.id.trip_mapview);

                        iv.setImageBitmap(currentPath.renderPath());
                        iv.invalidate();

                        if (currentCar != null) {
                            double miles = currentPath
                                    .getTotalDistanceInMiles();

                            double gpm = 1.0 / currentCar.getMilesPerGallon();
                            double gallons = miles * gpm;

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
                });
            }
        };
    }

    private void setupCurrentCar() {
        Bundle extras = getIntent().getExtras();
        if (extras != null &&
                extras.getInt("car_mpg") > 0 &&
                extras.getString("car_name") != null) {
            currentCar = new Car(extras.getString("car_name"),
                    extras.getInt("car_mpg"));
            toastLong("Please remember to pay attention to the road.");
        } else {
            toastLong("No car selected. Going by US Average of 22mpg.");
            currentCar = new Car("US Average", 22);
        }
    }

    public void finishTrip(View view) {
        /* TODO Show trip statistics */
    }

    public void addPolaroids(View view) {
        statistics.updatePolaroids(fakeGas += 0.001,
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
