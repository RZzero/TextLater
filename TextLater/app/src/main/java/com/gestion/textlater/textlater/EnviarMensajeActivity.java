package com.gestion.textlater.textlater;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import static com.gestion.textlater.textlater.R.id.fab;

public class EnviarMensajeActivity extends AppCompatActivity {
    boolean asunto = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_mensaje);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_v);
        setSupportActionBar(toolbar);
        if(getIntent()!=null){
            Intent intent = getIntent();
            String id = intent.getStringExtra("id");
            if(id.equals("telegram")){
                asunto = false;

            }
            setTitle(id.toUpperCase());
        }

        setAsunto();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_send);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id==R.id.action_settings_2){
                return true;
        }
        else if(id==R.id.action_settings_cerrar_envioMensaje){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
