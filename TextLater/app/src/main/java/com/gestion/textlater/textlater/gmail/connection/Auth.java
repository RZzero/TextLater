package com.gestion.textlater.textlater.gmail.connection;

import com.gestion.textlater.textlater.MainActivity;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Auth {

    private GoogleAccountCredential mCredential;
    private AppCompatActivity app;

    private final int REQUEST_ACCOUNT_PICKER = 1000;

    private final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "accountName";

    private static final String[] SCOPES = {GmailScopes.MAIL_GOOGLE_COM};

    private Handler handler;

    Auth(AppCompatActivity app){
        this.app = app;
        this.handler = handler;

        mCredential = GoogleAccountCredential.usingOAuth2(
                app.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    void tryAuth(){
        if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        }
    }

    String getPrefAccountName(){
        return mCredential.getSelectedAccountName();
    }

    GoogleAccountCredential getCredentials(){
        return mCredential;
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (hasPermission()) {

            String accountName = app.getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                tryAuth();
            } else {
                app.startActivityForResult(mCredential.newChooseAccountIntent(),REQUEST_ACCOUNT_PICKER);
            }
            return;
        }
            EasyPermissions.requestPermissions(app,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
    }

    private boolean hasPermission(){
        return EasyPermissions.hasPermissions(app, Manifest.permission.GET_ACCOUNTS);
    }


    public void name(String name){
        if (name != null) {
            SharedPreferences settings =
                    app.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PREF_ACCOUNT_NAME, name);
            editor.apply();
            mCredential.setSelectedAccountName(name);
        }
    }

    public String getRes(){

        return "GO SLOWER";

    }

}