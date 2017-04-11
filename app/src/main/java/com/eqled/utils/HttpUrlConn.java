package com.eqled.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Administrator on 2016/10/25.
 */

public class HttpUrlConn extends AsyncTask {

    public static String wifiIP;

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            String wifiUrl = "http://" + wifiIP + "/";
            Log.d("...............","wifiUrl......" + wifiUrl);
            URL url = new URL(wifiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                InputStream stream = connection.getInputStream();
                String json = StringUtils.getStringByInputStream(stream, "UTF-8");
                Log.i("wbxoo", json + "");
                return json;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}