package com.example.ustyle.data;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class AccessToken extends AsyncTask<Void, Void, String> {


    public class AccessTokenScheduler extends Thread{
        public void run(){
            try {
                Thread.sleep(7195000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Global.accessToken = "";
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            new AccessToken().execute();
        }
    }


    // Reads an InputStream and converts it to a String.
    public String convertInputToString(InputStream stream, int len)
            throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    protected String doInBackground(Void... voids){



        //https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET

        final String requestBaseURL = "https://api.weixin.qq.com/cgi-bin/token?";
        final String grantType = "grant_type";
        final String appID = "appid";
        final String appSecret = "secret";

        Uri uri = Uri.parse(requestBaseURL).buildUpon()
                .appendQueryParameter(grantType, "client_credential")
                .appendQueryParameter(appID, "wx9930e3d63633ee8c")
                .appendQueryParameter(appSecret, "24b54976d9e374684f8d6678a04d8728")
                .build();

        String uriString = uri.toString();
        HttpsURLConnection conn;
        conn = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(uriString);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(100000);

            conn.connect();
            int response = conn.getResponseCode();
            Log.d("Connection", "The response is: " + response);
            inputStream = conn.getInputStream();

            String contentString = convertInputToString(inputStream, 5000);

            String result = contentString;
            try {
                JSONObject resultResponse = new JSONObject(result);
                String accessToken = resultResponse.getString("access_token");
                Global.accessToken = accessToken;
                Log.d("access token: ", Global.accessToken);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return contentString;
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        } finally {
            conn.disconnect();

            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Thread nextAccessToken = new AccessTokenScheduler();
            nextAccessToken.start();
        }
    }

    protected void onPostExecute(String result) {

//        try {
//            JSONObject response = new JSONObject(result);
//            String accessToken = response.getString("access_token");
//            Global.accessToken = accessToken;
//            Log.d("access token: ", Global.accessToken);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }
}
