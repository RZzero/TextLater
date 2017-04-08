package com.gestion.textlater.textlater;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class Nombre extends AppCompatActivity {
    Button config;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre);
        config = (Button) findViewById(R.id.config);
    }







}
