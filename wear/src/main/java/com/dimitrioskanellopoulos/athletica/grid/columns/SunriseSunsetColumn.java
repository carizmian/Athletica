package com.dimitrioskanellopoulos.athletica.grid.columns;

import android.Manifest;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.util.Log;
import android.util.Pair;

import com.dimitrioskanellopoulos.athletica.grid.columns.abstracts.GoogleApiColumn;
import com.dimitrioskanellopoulos.athletica.helpers.EmulatorHelper;
import com.dimitrioskanellopoulos.athletica.helpers.SunriseSunsetHelper;
import com.dimitrioskanellopoulos.athletica.permissions.PermissionsHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.TimeZone;

class SunriseSunsetColumn extends GoogleApiColumn {
    private final static String TAG = "SunriseSunsetColumn";

    private static final long LOCATION_UPDATE_INTERVAL_MS = 3600000;
    private static final long LOCATION_UPDATE_FASTEST_INTERVAL_MS = 3600000;
    private static final LocationRequest locationRequest = new LocationRequest()
            .setInterval(LOCATION_UPDATE_INTERVAL_MS)
            .setFastestInterval(LOCATION_UPDATE_FASTEST_INTERVAL_MS)
            .setPriority(LocationRequest.PRIORITY_LOW_POWER);
    static Pair<String, String> sunriseSunset;
    private static final LocationListener locationChangedReceiver = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "Location changed");
            Log.d(TAG, "Provider: " + location.getProvider());
            Log.d(TAG, "Lat: " + location.getLatitude());
            Log.d(TAG, "Long: " + location.getLongitude());
            Log.d(TAG, "Altitude: " + location.getAltitude());
            Log.d(TAG, "Accuracy: " + location.getAccuracy());
            sunriseSunset = SunriseSunsetHelper.getSunriseAndSunset(location, TimeZone.getDefault().getID());
            Log.d(TAG, "Successfully updated sunrise");
        }
    };
    private static boolean hasRegisteredReceivers = false;
    private static GoogleApiClient googleApiClient;

    private PermissionsHelper permissionsHelper;

    SunriseSunsetColumn(Context context, Typeface paintTypeface, Float paintTextSize, int paintColor) {
        super(context, paintTypeface, paintTextSize, paintColor);
        // Get a Google API client if not set
        if (getGoogleApiClient() == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            getGoogleApiClient().connect();
        }


        // Get the helper for the permissions
        permissionsHelper = new PermissionsHelper(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BODY_SENSORS});
        // Run the emulator stuff
        if (EmulatorHelper.isEmulator()) {
            Location location = new Location("dummy");
            location.setLatitude(41);
            location.setLongitude(11);
            location.setTime(System.currentTimeMillis());
            location.setAccuracy(3.0f);
            sunriseSunset = SunriseSunsetHelper.getSunriseAndSunset(location, TimeZone.getDefault().getID());
        }
    }

    @Override
    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    @Override
    public void registerReceivers() {
        // Check permissions (hopefully the receiver wont be registered
        if (!permissionsHelper.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            if (permissionsHelper.canAskAgainForPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                permissionsHelper.askForPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                Log.d(TAG, "Asking for location permissions");
            }
            return;
        }

        hasRegisteredReceivers = true;
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationChangedReceiver);
        Log.d(TAG, "Listening for location updates");
    }

    @Override
    public void unRegisterReceivers() {
        hasRegisteredReceivers = false;
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationChangedReceiver);
        Log.d(TAG, "Stopped listening for location updates");
    }

    @Override
    public Boolean hasRegisteredReceivers() {
        return hasRegisteredReceivers;
    }
}