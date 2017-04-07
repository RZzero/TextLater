/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gestion.textlater.textlater;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
/**
 * Utility class with methods to help perform the HTTP request and
 * parse the response.
 */
public final class Utils {
    Message message;
    String From, To, Subject, Text, platform, date, status;
    String data;
    String response = "";
    private static AsyncHttpClient client = new AsyncHttpClient();
    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = Utils.class.getSimpleName();


    //TODO: NEED TO GET PLATFORM!!!
    public Utils(Message message) {
        this.message = message;
        GetAtributes();
    }

    private void GetAtributes() {
        // Get user defined values
        /*
        * @platform
        * @sender
        * @ToM
        * @subject
        * @content
        * @timeToSend
        * @messageStatus
        * */

        From = message.getSender();
        To = message.getToM();
        Subject = message.getSubject();
        Text = message.getContent();
        platform = message.getPlatform();
        date = message.getTimeToSend();
        status = message.getMessageStatus();
        SetRequest();
    }

    private void SetRequest() {
        try {
            //TODO: NEED TO EDIT THE @PARAMS:
            data = URLEncoder.encode("sender", "UTF-8")
                    + "=" + URLEncoder.encode(From, "UTF-8");

            data += "&" + URLEncoder.encode("ToM", "UTF-8") + "="
                    + URLEncoder.encode(To, "UTF-8");

            data += "&" + URLEncoder.encode("subject", "UTF-8")
                    + "=" + URLEncoder.encode(Subject, "UTF-8");

            data += "&" + URLEncoder.encode("content", "UTF-8")
                    + "=" + URLEncoder.encode(Text, "UTF-8");

            //TODO: OTHER PARAMS NEED TO BE ADDED

            data += "&" + URLEncoder.encode("platform", "UTF-8")
                    + "=" + URLEncoder.encode(platform, "UTF-8");

            data += "&" + URLEncoder.encode("messageStatus", "UTF-8")
                    + "=" + URLEncoder.encode(status, "UTF-8");

            data += "&" + URLEncoder.encode("timeToSend", "UTF-8")
                    + "=" + URLEncoder.encode(date, "UTF-8");
        } catch (Exception ex) {

        }
    }

    // Create GetText Metod
    public void GetText() throws UnsupportedEncodingException {

        // Create data variable for sent values to server

        RequestParams params = new RequestParams();
        params.put("notes", "Test api support");
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("52.36.200.87:80/api/v1/message/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[]     response) {
                // called when response HTTP status is "200 OK"
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                //called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }





    public String getRespuestaServidor() {
        return response;
    }
}
