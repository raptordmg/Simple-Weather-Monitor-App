package com.example.weathermonitorapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReceiveData {

    //Gets the text data from a site as a string
    public String getData(String dataurl) throws IOException {
        URL url = new URL(dataurl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isReader = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(isReader);
            StringBuffer sb = new StringBuffer();
            String str;
            while((str = reader.readLine()) != null){
                sb.append(str);
            }
            return sb.toString();
        } finally {
            urlConnection.disconnect();
        }
    }
}
