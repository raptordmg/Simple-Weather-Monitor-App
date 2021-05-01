package com.example.weathermonitorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView temperature;
    private TextView maxTemp;
    private TextView minTemp;
    private TextView humidity;
    private TextView dayCheck;
    private TextView wind;
    private TextView pressure;
    private TextView precipitation;
    private Button refreshButton;

    private ProcessJson jsonProcessor = new ProcessJson();
    private ReceiveData receiveData = new ReceiveData();
    private String weatherApiJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Allows running network functions on the main thread
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Sets textview text
        temperature = (TextView) findViewById(R.id.temperature);
        try {
            temperature.setText(receiveTemperature());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        maxTemp = (TextView) findViewById(R.id.temperatureMax);
        try {
            maxTemp.setText(receiveMaxTemp());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        minTemp = (TextView) findViewById(R.id.temperatureMin);
        try {
            minTemp.setText(receiveMinTemp());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        humidity = (TextView) findViewById(R.id.humidity);
        try {
            humidity.setText(receiveHumidity());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        try {
            updateApiData();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        dayCheck = (TextView) findViewById(R.id.daytimeCheck);
        try{
            dayCheck.setText(doDayCheck());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        wind = (TextView) findViewById(R.id.wind);
        try{
            wind.setText(receiveWind());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        pressure = (TextView) findViewById(R.id.pressure);
        try{
            pressure.setText(receivePressure());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        precipitation = (TextView) findViewById(R.id.precipitation);
        try {
            precipitation.setText(receivePrecipitation());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Sets functions performed when button is clicked
        refreshButton = (Button) findViewById(R.id.refreshButton);
            refreshButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    try {
                        temperature.setText(receiveTemperature());
                        maxTemp.setText(receiveMaxTemp());
                        minTemp.setText(receiveMinTemp());
                        humidity.setText(receiveHumidity());
                        updateApiData();
                        dayCheck.setText(doDayCheck());
                        wind.setText(receiveWind());
                        pressure.setText(receivePressure());
                        precipitation.setText(receivePrecipitation());
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    //Functions that get data from apis
    private void updateApiData() throws IOException, JSONException {
        String weatherApiRaw = receiveData.getData("https://api.weatherapi.com/v1/current.json?key=a9d353ecaf2648b9bcf120504212004&q=Plean&aqi=no");
        weatherApiJson = jsonProcessor.getValue("current", weatherApiRaw);
    }

    private String receiveTemperature() throws IOException, JSONException {
        return "Temperature: " + jsonProcessor.getValue("result", receiveData.getData("https://api.particle.io/v1/devices/e00fce68fbf5c55ce7fe8567/temperature?access_token=41254dece3bb92c521b6bacb9f9e8d8f5d91b662") + "C");
    }

    private String receiveMaxTemp() throws IOException, JSONException {
        return "Max temperature: " + jsonProcessor.getValue("result", receiveData.getData("https://api.particle.io/v1/devices/e00fce68fbf5c55ce7fe8567/maxTemp?access_token=41254dece3bb92c521b6bacb9f9e8d8f5d91b662") + "C");
    }

    private String receiveMinTemp() throws IOException, JSONException {
        return "Min temperature: " + jsonProcessor.getValue("result", receiveData.getData("https://api.particle.io/v1/devices/e00fce68fbf5c55ce7fe8567/minTemp?access_token=41254dece3bb92c521b6bacb9f9e8d8f5d91b662") + "C");
    }

    private String receiveHumidity() throws IOException, JSONException{
        return "Humidity: " + jsonProcessor.getValue("result", receiveData.getData("https://api.particle.io/v1/devices/e00fce68fbf5c55ce7fe8567/humidity?access_token=41254dece3bb92c521b6bacb9f9e8d8f5d91b662"));
    }

    private String doDayCheck() throws JSONException, IOException {
        if (jsonProcessor.getValue("result", receiveData.getData("https://api.particle.io/v1/devices/e00fce68fbf5c55ce7fe8567/isDay?access_token=41254dece3bb92c521b6bacb9f9e8d8f5d91b662")).equals("true")){
            return "It's daytime";
        }else {
            return "It's night";
        }
    }

    private String receiveWind() throws JSONException{
        return "Wind: " + jsonProcessor.getValue("wind_mph", weatherApiJson) + "mph at " + jsonProcessor.getValue("wind_degree", weatherApiJson) + " degrees";
    }

    private String receivePressure() throws JSONException{
        return "Pressure: " + jsonProcessor.getValue("pressure_mb", weatherApiJson) + "mb";
    }

    private String receivePrecipitation() throws JSONException {
        return "Precipitation: " + jsonProcessor.getValue("precip_mm", weatherApiJson) + "mm";
    }
}