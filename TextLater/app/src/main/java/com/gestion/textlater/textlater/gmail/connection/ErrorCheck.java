package com.gestion.textlater.textlater.gmail.connection;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


class ErrorCheck {

    private AppCompatActivity app;


    ErrorCheck(AppCompatActivity app){
        this.app = app;
    }

    boolean hasError(){
        return internetConnectionError() || googlePlayServicesError();
    }

    private boolean internetConnectionError(){
        ConnectivityManager connMgr = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (!(networkInfo != null && networkInfo.isConnected())){
            AlertDialog alertDialog = new AlertDialog.Builder(app).create();
            alertDialog.setTitle("Error de conexion:");
            alertDialog.setMessage("No se puede conectar con la red");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return true;
        }

        return false;
    }

    private boolean googlePlayServicesError(){
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(app);
        final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

        if (!(connectionStatusCode == ConnectionResult.SUCCESS)){

            if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
                Dialog dialog = apiAvailability.getErrorDialog(app, connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
                return true;
            }
        }
        return false;
    }

}
