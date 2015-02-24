package com.lunagameserve.decarbonator.location;

import android.graphics.*;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import com.lunagameserve.decarbonator.util.DataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by sixstring982 on 2/22/15.
 */
public class GPSPath implements LocationListener {

    private static final int BITMAP_WIDTH = 500;
    private static final int BITMAP_HEIGHT = 500;
    private static final int CALIBRATION_POINTS = 5;

    private int calibrationPointsSeen = 0;

    private final static String PROVIDER = LocationManager.GPS_PROVIDER;

    private static final long MILLIS_BETWEEN_COLLECTION = 100;

    private static final float METERS_BETWEEN_COLLECTION = 0.5f;

    private List<Location> locations = new ArrayList<Location>();

    private Location lastLocation = null;

    private long lastTimestampMS = 0;

    private Bitmap bmpCache;

    private AtomicBoolean cacheValid = new AtomicBoolean(false);

    /**
     * The total distance of all accumulated points read since this path has
     * begun collecting, in meters.
     */
    private double totalDistance = 0.0;

    private final AtomicBoolean connected = new AtomicBoolean(true);

    private LocationManager locationManager;

    /**
     * This is run once a GPS point has been collected. It is run on the GPS
     * collection thread.
     */
    private Runnable onCollectListener;

    /**
     * Called once GPS Service has been acquired.
     */
    private Runnable onGPSConnectedListener;

    /**
     * Called once GPS Service has been acquired.
     */
    private Runnable onGPSDisconnectedListener;

    private double currentVelocity;

    public void setOnCollectListener(Runnable onCollectListener) {
        this.onCollectListener = onCollectListener;
    }

    public void setOnGPSConnectedListener(Runnable onGPSConnectedListener) {
        this.onGPSConnectedListener = onGPSConnectedListener;
    }

    public void setOnGPSDisconnectedListener(Runnable onGPSDisconnectedListener)
    {
        this.onGPSDisconnectedListener = onGPSDisconnectedListener;
    }

    public void startCollecting(LocationManager locationManager) {
        this.locationManager = locationManager;
        locationManager.requestLocationUpdates(PROVIDER,
                                               MILLIS_BETWEEN_COLLECTION,
                                               METERS_BETWEEN_COLLECTION, this);
    }

    public void stopCollecting() {
        this.locationManager.removeUpdates(this);
    }

    public double getTotalDistanceInMeters() {
        return totalDistance;
    }

    public double getTotalDistanceInKm() {
        return getTotalDistanceInMeters() / 1000.0;
    }

    public double getTotalDistanceInMiles() {
        return getTotalDistanceInKm() / 1.60934;
    }

    /**
     * The current velocity in km/h
     * @return The current velocity in km/h
     */
    public double getCurrentVelocity() {
        return currentVelocity;
    }

    public double getCurrentVelocityMPH() {
        return currentVelocity / 1.60934;
    }

    public double getCurrentLatitude() {
        if (lastLocation != null) {
            return lastLocation.getLatitude();
        }
        return 0;
    }

    public double getCurrentLongitude() {
        if (lastLocation != null) {
            return lastLocation.getLongitude();
        }
        return 0;
    }

    private void gatherDataPoint(Location loc) {
        if (connected.get()) {
            if (calibrationPointsSeen < CALIBRATION_POINTS) {
                calibrationPointsSeen++;
            } else {
                if (lastLocation != null) {
                    totalDistance += loc.distanceTo(lastLocation);
                    currentVelocity = DataUtil.metersToKilometers(
                            loc.distanceTo(lastLocation)) /
                            DataUtil.millisecondsToHours(
                                    DataUtil.milliTime() - lastTimestampMS);
                }

                locations.add(loc);
                cacheValid.set(false);

                lastLocation = loc;
                lastTimestampMS = DataUtil.milliTime();

                onCollectListener.run();
            }
        }
    }

    @Override
    public void onLocationChanged(Location loc) {
        gatherDataPoint(loc);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (provider.equals(PROVIDER)) {
            if (status == LocationProvider.AVAILABLE) {
                onGPSConnectedListener.run();
                connected.set(true);
            } else {
                onGPSDisconnectedListener.run();
                connected.set(false);
            }
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private RectF getLatLongBounds() {
        RectF rect = null;
        for (Location loc : locations) {
            if (rect == null) {
                rect = new RectF((float)loc.getLongitude(),
                                 (float)loc.getLatitude(),
                                 (float)loc.getLongitude(),
                                 (float)loc.getLatitude());
            } else {
                if (rect.bottom > loc.getLatitude()) {
                    rect.bottom = (float)loc.getLatitude();
                }
                if (rect.top < loc.getLatitude()) {
                    rect.top = (float)loc.getLatitude();
                }
                if (rect.right < loc.getLongitude()) {
                    rect.right = (float)loc.getLongitude();
                }
                if (rect.left > loc.getLongitude()) {
                    rect.left = (float)loc.getLongitude();
                }
            }
        }
        return rect;
    }

    private Bitmap recreateBitmapCache() {
        Bitmap bmp = Bitmap.createBitmap(BITMAP_WIDTH,
                                         BITMAP_HEIGHT,
                                         Bitmap.Config.ARGB_8888);
        if (locations.size() >= 2) {
            RectF bounds = getLatLongBounds();
            Canvas c = new Canvas(bmp);
            float dx = BITMAP_WIDTH / bounds.width();
            float dy = BITMAP_HEIGHT / bounds.height();
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);


            float x1dist;
            float y1dist;
            float x2dist;
            float y2dist;

            for (int i = 1; i < locations.size(); i++) {
                x1dist = (float)
                        (locations.get(i - 1).getLongitude() - bounds.left);
                x2dist = (float)
                        (locations.get(i).getLongitude() - bounds.left);
                y1dist = (float)
                        (locations.get(i - 1).getLatitude() - bounds.top);
                y2dist = (float)
                        (locations.get(i).getLatitude() - bounds.top);
                c.drawLine(x1dist * dx, y1dist * dy,
                           x2dist * dx, y2dist * dy, paint);
            }
        }
        return bmp;
    }

    public Bitmap renderPath() {
        if (!cacheValid.get()) {
            bmpCache = recreateBitmapCache();
            cacheValid.set(true);
        }
        return Bitmap.createBitmap(bmpCache);
    }
}