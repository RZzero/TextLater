package com.gestion.textlater.textlater;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistorialFragment extends Fragment {

    static ArrayList<Message> mensajes;

    View view;
    private String TEXTLATER_REQUEST_MESSAGES_URL =
            "http://52.36.200.87/api/v1/message/mymessages?sender=rafaelsuazoh@gmail.com";

    public HistorialFragment() {
        // Required empty public constructor
    }

    private void updateUi(Message message) {

        TextView titleTextView = (TextView) view.findViewById(R.id.subject_TextView);
        titleTextView.setText(message.getSubject());

        // Display the earthquake date in the UI
        TextView dateTextView = (TextView) view.findViewById(R.id.some_text_TextView);
        dateTextView.setText(message.getContent());


    }

    private class TextLaterAsyncTask extends AsyncTask<URL, Void, Message> {

        @Override
        protected Message doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(TEXTLATER_REQUEST_MESSAGES_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                //Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            Message message = extractFeatureFromJson(jsonResponse);

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return message;
        }

        /**
         * Update the screen with the given earthquake (which was the result of the
         * {@link TextLaterAsyncTask}).
         */
        @Override
        protected void onPostExecute(
                Message message) {
            if (message == null) {
                return;
            }

           updateUi(message);
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
         * Return an {@link Message} object by parsing out information
         * about the first earthquake from the input earthquakeJSON string.
         */
        private Message extractFeatureFromJson(String earthquakeJSON) {
            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(earthquakeJSON)) {
                return null;
            }

            try {
                JSONArray baseJsonResponse = new JSONArray(earthquakeJSON);
                //JSONArray featureArray = baseJsonResponse.getJSONArray("features");

                // If there are results in the features array
                if (baseJsonResponse.length() > 0) {
                    for (int i = 0; i < baseJsonResponse.length(); i++){
                        JSONObject elemento = (JSONObject) baseJsonResponse.get(i);

                        String idMessage = elemento.getString("idMessage");
                        String platform = elemento.getString("platform");
                        String sender = elemento.getString("sender");
                        String ToM = elemento.getString("ToM");
                        String subject = elemento.getString("subject");
                        String content = elemento.getString("content");
                        String timeToSend = elemento.getString("timeToSend");
                        String messageStatus = elemento.getString("messageStatus");

                        Message message = new Message(idMessage, platform, sender, ToM, subject, content, timeToSend, messageStatus);

                        mensajes.add(message);

                        Log.d("dsfdsfdsfsf", message.toString());
                    }



                    // Create a new {@link Event} object
                    return mensajes.get(0);
                }
            } catch (JSONException e) {
                //Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return null;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.item_historial, container, false);
        mensajes = new ArrayList<>();
        TextLaterAsyncTask task = new TextLaterAsyncTask();
        task.execute();

        return view;
    }

}
