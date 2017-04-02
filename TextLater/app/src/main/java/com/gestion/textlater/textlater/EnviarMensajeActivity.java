package com.gestion.textlater.textlater;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import static com.gestion.textlater.textlater.R.id.fab;

public class EnviarMensajeActivity extends AppCompatActivity {
    boolean asunto = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_mensaje);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getIntent()!=null){
            Intent intent = getIntent();
            String id = intent.getStringExtra("id");
            if(id.equals("telegram")){
                asunto = false;
            }
        }

        setAsunto();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_send);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setAsunto(){
        EditText Asunto = (EditText) findViewById(R.id.Mensaje_asunto_editText);
        if(!asunto){
            Asunto.setVisibility(View.GONE);
        }
    }


    @Override
    public void finish() {
        super.finish();
    }
}
