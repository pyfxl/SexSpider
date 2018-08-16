package com.android.sexspider4.helper;

import android.util.Log;

import com.android.sexspider4.MyApplication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;

/**
 * Created by feng on 2017/5/5.
 */

public class HttpHelper {
    private static final String TAG = "HttpHelper";

    private HttpHelper() {
    }

    public static String getStringFromLink(String link) {
        return getStringFromLink(link, "utf-8", "http://www.fxlweb.com");
    }

    public static String getStringFromLink(String link, String encode, String domain) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", MyApplication.USER_AGENT);
            connection.setRequestProperty("Referer", domain);
            //connection.setRequestProperty("connection", "close");
            connection.setRequestProperty("Accept-Encoding", "");
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(MyApplication.DEFAULT_TIMEOUT);
            connection.setReadTimeout(MyApplication.DEFAULT_TIMEOUT);

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
            String lines = "";
            while ((lines = reader.readLine()) != null) {
                content.append(lines);
            }

            connection.disconnect();
            reader.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, link);
        }

        return content.toString();
    }

    public static byte[] getBytesFromLink(String link) {
        String domain = "http://www.baidu.com";
        return getBytesFromLink(link, domain);
    }

    public static byte[] getBytesFromLink(String link, String domain) {
        byte[] bytes = null;
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", MyApplication.USER_AGENT);
            connection.setRequestProperty("Referer", domain);
            //connection.setRequestProperty("connection", "close");
            connection.setRequestProperty("Accept-Encoding", "");
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(MyApplication.DEFAULT_TIMEOUT);
            connection.setReadTimeout(MyApplication.DEFAULT_TIMEOUT);

            InputStream in = connection.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[10240];
            int n = 0;
            while (-1 != (n=in.read(buffer))) {
                out.write(buffer, 0, n);
            }
            bytes = out.toByteArray();

            connection.disconnect();
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, link);
        }

        return bytes;
    }

}
