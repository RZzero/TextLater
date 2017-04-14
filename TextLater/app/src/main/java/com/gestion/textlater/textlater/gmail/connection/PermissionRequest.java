package com.gestion.textlater.textlater.gmail.connection;


import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;

import java.io.IOException;

class PermissionRequest extends AsyncTask<Void, Void, String> {

    Gmail mService = null;
    private Exception mLastError = null;
    AppCompatActivity app;

    PermissionRequest(GoogleAccountCredential credential, AppCompatActivity app) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.gmail.Gmail.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("TextLater")
                .build();
        this.app = app;
    }

    private String trygetInfo() throws IOException{
        String user = "me";
        String response = "";

        response = mService.users()
                .getProfile(user)
                .execute()
                .toPrettyString();

        return response;
    }

    @Override
    protected String doInBackground(Void... param) {

        try {
            trygetInfo();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
        }
        return "";
    }

    @Override
    protected void onCancelled() {
        final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
        final int REQUEST_AUTHORIZATION = 1001;

        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {

                GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
                Dialog dialog = apiAvailability.getErrorDialog(
                        app,
                        ((GooglePlayServicesAvailabilityIOException) mLastError).getConnectionStatusCode(),
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }

            if (mLastError instanceof UserRecoverableAuthIOException) {
                app.startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        REQUEST_AUTHORIZATION);
            }

        }


    }

}
