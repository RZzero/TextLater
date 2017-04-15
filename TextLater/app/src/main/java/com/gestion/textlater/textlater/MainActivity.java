package com.gestion.textlater.textlater;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gestion.textlater.textlater.gmail.connection.GmailConnector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.R.attr.value;
import static com.gestion.textlater.textlater.R.layout.item;
import static com.gestion.textlater.textlater.UserPublicInformation.TEXTLATER_REQUEST_USER_DATA_URL;
import static com.gestion.textlater.textlater.UserPublicInformation.urlImage;

public class MainActivity extends AppCompatActivity {


    private Toolbar appbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;


    FloatingActionButton fabEnviar, fabGmail, fabTelegram;
    Animation FabOpen, FabClose, FabRClockwise, FabRantiClockwise;
    boolean isOpen = false;

    GmailConnector gc;
    public static GmailConnector copyGC;
    public Handler handler;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static String nombreCmpleto;
    static String urlImage;

    public static boolean userGmailLogged;

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

            public void handleMessage(android.os.Message msg) {
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
            }

            ;
        };

        gc = new GmailConnector(MainActivity.this);
        //BORRAR
        gc.tryAuth();

        copyGC = gc;
        try {
            userGmailLogged = MainActivity.this.getPreferences(Context.MODE_PRIVATE).getString("accountName", null).length() > 3;


        } catch (Exception e) {
            userGmailLogged = false;
        }

        if (userGmailLogged) {
            navHeader();
        }

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

                                gc.tryAuth();
                                AlertDialog testtt = new AlertDialog.Builder(MainActivity.this).create();
                                testtt.setTitle("Mail");
                                testtt.setMessage(gc.getUserMail());
                                testtt.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                testtt.show();

                                break;
                            case R.id.nav_nosotros:

//                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//                                alertDialog.setTitle("Información General: ");
//                                if (userGmailLogged) {
//                                    alertDialog.setMessage("Logged");
//                                } else {
//                                    alertDialog.setMessage("Not Logged");
//                                }
//
//                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.dismiss();
//                                            }
//                                        });
//                                alertDialog.show();


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

        if (userGmailLogged) {
            Intent i = new Intent(MainActivity.this, EnviarMensajeActivity.class);
            i.putExtra("id", "gmail");
            if (gc.getUserMail() != null) {
                i.putExtra("email", gc.getUserMail());
            }
            startActivity(i);
        } else {
            //Muestra el mensaje
            Toast.makeText(MainActivity.this, getResources().getString(R.string.NotLogged), Toast.LENGTH_SHORT).show();
        }
    }


    private void navHeader() {
        TextLaterAsyncTask task = new TextLaterAsyncTask();
        task.execute();
        try {
            task.get(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        Log.e("AYUDA!", nombreCmpleto + urlImage);
        Log.e("dasdasdsad", "dasdas");

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Log.e("URL:", urldisplay);
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    private class TextLaterAsyncTask extends AsyncTask<URL, Void, Usuario> {

        @Override
        protected Usuario doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(TEXTLATER_REQUEST_USER_DATA_URL + gc.getUserMail() + "?alt=json");

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                //Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            Usuario usuario = extractFeatureFromJson(jsonResponse);

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return usuario;
        }

        /**
         * Update the screen with the given earthquake (which was the result of the
         * {@link TextLaterAsyncTask}).
         */
        @Override
        protected void onPostExecute(
                Usuario usuario) {
            if (usuario == null) {
                return;
            }

            nombreCmpleto = usuario.getNombre();
            urlImage = usuario.getImgUrl();
            Log.e("nombre", nombreCmpleto);
            Log.e("imageasddsd", urlImage);
            NavigationView navigationView = (NavigationView) findViewById(R.id.navview);
            View hView = navigationView.getHeaderView(0);

            TextView nav_user = (TextView) hView.findViewById(R.id.name_perfil_textView);
            nav_user.setText(nombreCmpleto);

            TextView nav_email = (TextView) hView.findViewById(R.id.correo_perfil_textView);
            nav_email.setText(gc.getUserMail());

            ImageView nav_image = (ImageView) hView.findViewById(R.id.Perfil_imageView);
            new DownloadImageTask(nav_image)
                    .execute(urlImage);

        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                //Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            // If the URL is null, then return early.
            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    //Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                //Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Return an {@link Usuario} object by parsing out information
         * about the first earthquake from the input earthquakeJSON string.
         */
        private Usuario extractFeatureFromJson(String earthquakeJSON) {
            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(earthquakeJSON)) {
                return null;
            }

            Log.e("JSON:", earthquakeJSON);
            Usuario user = null;
            String nombre = "";
            String imageURL = "";
            try {
                JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

                JSONObject entryObject = baseJsonResponse.getJSONObject("entry");
                JSONArray authorArray = entryObject.getJSONArray("author");
                JSONObject nameObject = authorArray.getJSONObject(0);
                JSONObject authorName = nameObject.getJSONObject("name");
                nombre = authorName.getString("$t");


                JSONObject image = entryObject.getJSONObject("gphoto$thumbnail");
                imageURL = image.getString("$t");

                user = new Usuario(nombre, imageURL);

            } catch (JSONException e) {
                //Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return user;
        }
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

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
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
                    userGmailLogged = true;
                    navHeader();
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
