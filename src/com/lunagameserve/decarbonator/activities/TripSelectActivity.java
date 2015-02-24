package com.lunagameserve.decarbonator.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import com.lunagameserve.decarbonator.R;
import com.lunagameserve.decarbonator.graphics.BitmapUtil;
import com.lunagameserve.decarbonator.graphics.Polaroid;
import com.lunagameserve.decarbonator.graphics.TextFader;
import com.lunagameserve.decarbonator.physics.PhysUtil;
import com.lunagameserve.decarbonator.util.Screen;
import com.lunagameserve.decarbonator.util.Ticker;
import com.lunagameserve.decarbonator.util.UnderActivity;

/**
 * Created by sixstring982 on 2/21/15.
 */
public class TripSelectActivity extends UnderActivity {

    private ImageButton carButton;
    private ImageButton walkButton;
    private ImageButton bikeButton;

    private Polaroid carPolaroid;
    private Polaroid walkPolaroid;
    private Polaroid bikePolaroid;

    private TextFader carFader;
    private TextFader walkFader;
    private TextFader bikeFader;

    private TextView carTextView;
    private TextView walkTextView;
    private TextView bikeTextView;

    private Ticker onStartTicker = new Ticker(30);

    private enum AnimationState {
        Setup {
            @Override public AnimationState nextState() {return ThrowCar;}
        },
        ThrowCar {
            @Override public AnimationState nextState() {return ThrowBike;}
        },
        ThrowWalk{
            @Override public AnimationState nextState() {return Ready;}
        },
        ThrowBike{
            @Override public AnimationState nextState() {return ThrowWalk;}
        },
        Ready{
            @Override public AnimationState nextState() {return Ready;}
        };

        public abstract AnimationState nextState();
    }

    private AnimationState animationState = AnimationState.Setup;

    @Override
    protected String getTag() {
        return "TripSelectActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.trip_select_activity_layout);

        Screen.initializeSizes(getBaseContext());

        onStartTicker.setTrigger(advanceAnimationState());

        carButton = (ImageButton)findViewById(
                R.id.trip_select_car_button);
        walkButton = (ImageButton)findViewById(
                R.id.trip_select_walk_button);
        bikeButton = (ImageButton)findViewById(
                R.id.trip_select_bike_button);

        carTextView = (TextView)findViewById(
                R.id.trip_select_car_textview);
        walkTextView = (TextView)findViewById(
                R.id.trip_select_walk_textview);
        bikeTextView = (TextView)findViewById(
                R.id.trip_select_bike_textview);

        carFader = new TextFader(carTextView);
        walkFader = new TextFader(walkTextView);
        bikeFader = new TextFader(bikeTextView);

        carPolaroid = new Polaroid(this.getBaseContext(),
                                   R.drawable.traffic_light);

        walkPolaroid = new Polaroid(this.getBaseContext(), R.drawable.shoes);

        bikePolaroid = new Polaroid(this.getBaseContext(), R.drawable.bike);
    }

    @Override
    protected void onResume() {
        super.onResume();

        findViewById(R.id.trip_select_layout).post(new Runnable() {
            @Override
            public void run() {
                reanimateButtons();
            }
        });
    }

    private void reanimateButtons() {

        Polaroid[] ps = new Polaroid[] {
                carPolaroid, bikePolaroid, walkPolaroid
        };
        int i = 0;
        for (Polaroid p : ps) {
            p.setOnStopCallback(advanceAnimationState());
            p.setPosition(80 + (i * (Screen.getWidthDP() - 40)) / 3f,
                          Screen.getHeightDP());
            p.setVelocity(-PhysUtil.PIOver2, 0, i * 15 + 40);
            p.setRotationVelocity(0, 0.05f);

            i++;
        }

        carButton.setImageBitmap(carPolaroid.getRotatedBitmap());
        bikeButton.setImageBitmap(bikePolaroid.getRotatedBitmap());
        walkButton.setImageBitmap(walkPolaroid.getRotatedBitmap());

        queueAnimate();
    }

    private void queueAnimate() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {

        }
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

    private void animate() {
        switch(animationState) {
            case Setup:
                onStartTicker.tick(); break;
            case ThrowCar:
                carPolaroid.update(carButton);
                break;
            case ThrowBike:
                carPolaroid.synchronizeTextView(carTextView);
                bikePolaroid.update(bikeButton);
                carFader.fadeUp(); break;
            case ThrowWalk:
                bikePolaroid.synchronizeTextView(bikeTextView);
                walkPolaroid.update(walkButton);
                bikeFader.fadeUp(); break;
            default: if (walkFader.fadeUp()) {
                        walkPolaroid.synchronizeTextView(walkTextView);
                        break;
                     } else {
                        return;
                     }
        }
        queueAnimate();
    }

    private Runnable advanceAnimationState() {
        return new Runnable() {
            @Override
            public void run() {
                animationState = animationState.nextState();
            }
        };
    }

    public void onBikeClick(View v) {
        logDebug("Bike!");
        pushActivity(TripActivity.class);
    }

    public void onWalkClick(View v) {
        logDebug("Walk!");
        pushActivity(TripActivity.class);
    }

    public void onCarClick(View v) {
        logDebug("Car!");
        pushActivity(ManageCarsActivity.class);
    }
}
