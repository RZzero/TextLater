package com.gestion.textlater.textlater;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.*;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.gestion.textlater.textlater.gmail.connection.GmailConnector;
import com.gestion.textlater.textlater.gmail.connection.originalAuth;

import static android.R.attr.value;

public class MainActivity extends AppCompatActivity {


    private Toolbar appbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;


    FloatingActionButton fabEnviar, fabGmail, fabTelegram;
    Animation FabOpen, FabClose, FabRClockwise, FabRantiClockwise;
    boolean isOpen = false;

    GmailConnector gc;
    public Handler handler;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        MenuFragmentAdapter adapter = new MenuFragmentAdapter(getSupportFragmentManager(), this);

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);



        appbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

         handler = new Handler(Looper.getMainLooper()) {

            public void handleMessage(android.os.Message msg){
                if (msg.obj != null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("SUCCESS");
                    alertDialog.setMessage(msg.obj.toString());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            };
        };



        navView = (NavigationView) findViewById(R.id.navview);
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.nav_configuracion:
                                //Intent myIntent = new Intent(MainActivity.this, PrincipalActivity.class);
                               //g myIntent.putExtra("key", value); //Optional parameters
                                //MainActivity.this.startActivity(myIntent);
                                try{
                                    gc = new GmailConnector(MainActivity.this, handler);
                                    gc.tryAuth();
 //                                   Intent myIntent = new Intent(MainActivity.this, originalAuth.class);
 //                                   MainActivity.this.startActivity(myIntent);

                                }catch(Exception e){
                                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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

                                break;
                            case R.id.nav_nosotros:

                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("Información General: ");
                                alertDialog.setMessage("Miembros:\n\n\t\tClovis Ramírez\t\t\t\t\t\t\t1063120\n\t\tRafael Suazo\t\t\t\t\t\t\t\t1059627\n\t\tRoberto Amarante\t\t\t1060357\n");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();

                                break;
                            case R.id.nav_sendFeedBack:
                                final String url = "https://docs.google.com/forms/d/e/1FAIpQLScmQffr9p2As0DdHIkQdEn9c7G7-B7tiUrB0xZkNnd2FcRzOA/viewform";

                                Uri uri = Uri.parse(url);
                                Intent intent = new Intent();
                                intent.setData(uri);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                // fragmentTransaction = true;
                                break;
                        }

                        if (fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.viewpager, fragment)
                                    .commit();

                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });


        fabEnviar = (FloatingActionButton) findViewById(R.id.fab_send);
        fabGmail = (FloatingActionButton) findViewById(R.id.fab_gmail);
        fabTelegram = (FloatingActionButton) findViewById(R.id.fab_telegram);

        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRantiClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);


        fabEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                /*Intent myIntent = new Intent(MainActivity.this, EnviarMensajeActivity.class);
                myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);*/

                if (isOpen) {
                    fabGmail.startAnimation(FabClose);
                    fabTelegram.startAnimation(FabClose);
                    fabEnviar.startAnimation(FabRantiClockwise);
                    fabGmail.setClickable(false);
                    fabTelegram.setClickable(false);
                    isOpen = false;
                } else {
                    fabGmail.startAnimation(FabOpen);
                    fabTelegram.startAnimation(FabOpen);
                    fabEnviar.startAnimation(FabRClockwise);
                    fabGmail.setClickable(true);
                    fabTelegram.setClickable(true);
                    isOpen = true;
                }
            }
        });
    }

    public void telegramEnviar(View view) {
       /* Intent i = new Intent(MainActivity.this, EnviarMensajeActivity.class);
        i.putExtra("id", "telegram");
        startActivity(i);*/

        //This is going to be the method to be includedd when telegram app needs to be oppened, the message can be send throught every platfrom in the phone.
        //Take this as a second action.
        //anyway, we need to do it for telegram
        /*Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        */

    }

    public void gmailEnviar(View view) {
        Intent i = new Intent(MainActivity.this, EnviarMensajeActivity.class);
        i.putExtra("id", "gmail");
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Esta app requiere de Google Play Services.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    gc.tryAuth();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    gc.named(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
                    gc.tryAuth();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    gc.tryAuth();
                }
                break;
        }
    }





}
