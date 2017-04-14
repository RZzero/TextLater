package com.gestion.textlater.textlater.gmail.connection;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class GmailConnector{

    private Auth auth;
    private AppCompatActivity app;
    private Request request;

    public GmailConnector(AppCompatActivity app){
        this.app = app;
        auth = new Auth(app);
        request = new Request();
    }

    public boolean tryAuth(){

        if (!new ErrorCheck(app).hasError()){

            try{
                auth.tryAuth();
                AlertDialog alertDialog = new AlertDialog.Builder(app).create();
                alertDialog.setTitle("SUCCESS");
                alertDialog.setMessage(auth.getPrefAccountName());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } catch(Exception e){
                AlertDialog alertDialog = new AlertDialog.Builder(app).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(auth.toString());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

            return true;
        }

        return false;
    }

    public void named(String name){
        auth.name(name);
    }

    }




