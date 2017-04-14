package com.gestion.textlater.textlater.gmail.connection;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.gestion.textlater.textlater.MainActivity;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.Gmail;

import java.io.IOException;

class Request extends AsyncTask<Void, Void, String> {

    Gmail mService = null;
    private Exception mLastError = null;
    AppCompatActivity app;
    Handler handler;

    Request(GoogleAccountCredential credential, AppCompatActivity app, Handler handler) {
        this.handler = handler;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.gmail.Gmail.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("TextLater")
                .build();
        this.app = app;
    }

    String getUserInfo() throws IOException{
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
            android.os.Message message = android.os.Message.obtain();
            message.obj = getUserInfo();
          handler.sendMessage(message);
        } catch (Exception e) {
            mLastError = e;
            cancel(true);

        }
        return "";
    }


    String trygetData() throws IOException{
        String user = "me";
        String response = "n";

        ListMessagesResponse listResponse = mService.users().messages()
                            .list(user)
                            .setMaxResults((long)10)
                            .execute();
            for (Message submessage : listResponse.getMessages()) {
                Message message =
                        mService.users().messages().get(user,submessage.getId()).execute();
                response += message.getSnippet();
            }

        return response;
    }

    @Override
    protected void onCancelled() {
        final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

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
                        originalAuth.REQUEST_AUTHORIZATION);
            }

        }


    }

}
