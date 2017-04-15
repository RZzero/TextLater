package com.gestion.textlater.textlater;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by kyonru on 15/04/17.
 */

public class UserPublicInformation {


    public String getNombreCmpleto() {
        return this.nombreCmpleto;
    }

    public String getUrlImage() {
        return this.urlImage;
    }

    static String nombreCmpleto;
    static String urlImage;
    final static String TEXTLATER_REQUEST_USER_DATA_URL = "http://picasaweb.google.com/data/entry/api/user/";


    public UserPublicInformation() {
        nombreCmpleto = "";
        urlImage = "";
    }

    public void init() {
        setOtherUserData();
    }

    private void setOtherUserData() {
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
    }

    private class TextLaterAsyncTask extends AsyncTask<URL, Void, Usuario> {

        @Override
        protected Usuario doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(TEXTLATER_REQUEST_USER_DATA_URL + MainActivity.copyGC.getUserMail() + "?alt=json");

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
}
