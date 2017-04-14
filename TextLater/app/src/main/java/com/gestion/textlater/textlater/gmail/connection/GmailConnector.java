package com.gestion.textlater.textlater.gmail.connection;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class GmailConnector{

    private Auth auth;
    private AppCompatActivity app;
    Request req;

    public GmailConnector(AppCompatActivity app){
        this.app = app;
        auth = new Auth(app);
    }



    public boolean tryAuth(){

        if (!new ErrorCheck(app).hasError()){

            try{
                auth.tryAuth();

            } catch(Exception e){
                AlertDialog alertDialog = new AlertDialog.Builder(app).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(e.getMessage());
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

    public String getUserMail(){
        return auth.getCredentials().getSelectedAccountName();
    }

    public void getUserInformation(Handler handler){
        try{
            new Request(auth.getCredentials(),app,handler).execute();
        }catch (Exception e){
            auth.tryAuth();
            getUserInformation(handler);
        }

    }

}




