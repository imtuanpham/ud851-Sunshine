package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

//  COMPLETED (1) Create a class called SunshineSyncTask
public class SunshineSyncTask {
//  COMPLETED (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
//      COMPLETED (3) Within syncWeather, fetch new weather data
//      COMPLETED (4) If we have valid results, delete the old data and insert the new
    synchronized public static void syncWeather(Context context) {
        try {
            URL weatherUrl = NetworkUtils.getUrl(context);
            String responseJson = NetworkUtils.getResponseFromHttpUrl(weatherUrl);

            ContentValues[] contentValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, responseJson);

            if (contentValues != null && contentValues.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();

                // delete old data
                // DEBUG
//                contentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI, null, null);

                // insert new data
                contentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, contentValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }
}