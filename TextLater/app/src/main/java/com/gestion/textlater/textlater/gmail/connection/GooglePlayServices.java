package com.gestion.textlater.textlater.gmail.connection;

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * This class have the responsibility to handle Google Play Services functionalities and related.
 *
 */

public class GooglePlayServices {

    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    public static boolean available(AppCompatActivity app){
            GoogleApiAvailability apiAvailability =
                    GoogleApiAvailability.getInstance();
            final int connectionStatusCode =
                    apiAvailability.isGooglePlayServicesAvailable(app);
            return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    public static void acquire(AppCompatActivity app) {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();

        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(app);

        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
                //showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


}
