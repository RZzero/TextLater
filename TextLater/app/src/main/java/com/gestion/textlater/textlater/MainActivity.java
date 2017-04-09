package com.gestion.textlater.textlater;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;

import static android.R.attr.value;


public class MainActivity extends AppCompatActivity {


    private Toolbar appbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private FilePickerDialog dialog;


    FloatingActionButton fabEnviar, fabGmail, fabTelegram;
    Animation FabOpen, FabClose, FabRClockwise, FabRantiClockwise;
    boolean isOpen = false;

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


        navView = (NavigationView) findViewById(R.id.navview);
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.nav_configuracion:
                                Intent myIntent = new Intent(MainActivity.this, PrincipalActivity.class);
                               //g myIntent.putExtra("key", value); //Optional parameters
                                MainActivity.this.startActivity(myIntent);
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
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
        dialog = new FilePickerDialog(MainActivity.this,properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
            }
        });
        dialog.show();
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
    //Add this method to show Dialog when the required permission has been granted to the app.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(dialog!=null)
                    {   //Show dialog if the read permission has been granted.
                        dialog.show();
                    }
                }
                else {
                    //Permission has not been granted. Notify the user.
                    Toast.makeText(MainActivity.this,"Permission is Required for getting list of files",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
