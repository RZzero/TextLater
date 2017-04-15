package com.gestion.textlater.textlater;


import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistorialFragment extends Fragment {

    static ArrayList<Message> mensajes;

    View view;
    private String TEXTLATER_REQUEST_MESSAGES_URL =
            "http://52.36.200.87/api/v1/message/mymessages?sender=" + MainActivity.copyGC.getUserMail();

    public HistorialFragment() {
        // Required empty public constructor
    }

    private void updateUi(Message message) {




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
        TEXTLATER_REQUEST_MESSAGES_URL =
                "http://52.36.200.87/api/v1/message/mymessages?sender=" + MainActivity.copyGC.getUserMail();
        view = inflater.inflate(R.layout.list, container, false);
        mensajes = new ArrayList<>();
        TextLaterAsyncTask task = new TextLaterAsyncTask();
        task.execute();

        try {
            task.get(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        MensajeAdapter adapter = new MensajeAdapter(getActivity(), mensajes);
        ListView  listView = (ListView) view.findViewById(R.id.list);

        listView.setAdapter(adapter);
        //Hace visible los cambios
        adapter.notifyDataSetChanged();
        //Listener for catching the touch when the user wants to here the pronunciation of the word.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                /*
                another way to do it.
                Word value = (Word) adapter.getItemAtPosition(position);
                */
                Message value = mensajes.get(position);
                if(value.getMessageStatus().equals("NS")){
                    Intent myIntent = new Intent(getActivity(), ModificarActivity.class);
                    myIntent.putExtra("idMessage", value.getIdMessage());
                    myIntent.putExtra("platform", value.getPlatform());
                    myIntent.putExtra("sender", value.getSender());
                    myIntent.putExtra("messageStatus", value.getMessageStatus());
                    myIntent.putExtra("subject", value.getSubject());
                    myIntent.putExtra("content", value.getContent());
                    myIntent.putExtra("timeToSend", value.getTimeToSend());
                    myIntent.putExtra("ToM", value.getToM());
                    myIntent.putExtra("id", "gmail");


                    startActivity(myIntent);

                }


            }
        });
        return view;
    }

}
