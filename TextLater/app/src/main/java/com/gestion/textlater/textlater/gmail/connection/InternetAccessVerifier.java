package com.gestion.textlater.textlater.gmail.connection;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

public class InternetAccessVerifier {

    public static boolean check(AppCompatActivity app){
            ConnectivityManager connMgr = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
    }

}
