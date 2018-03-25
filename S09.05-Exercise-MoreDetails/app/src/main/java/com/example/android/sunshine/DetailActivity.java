/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.example.android.sunshine;

import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
//      COMPLETED (21) Implement LoaderManager.LoaderCallbacks<Cursor>

    private final String TAG = DetailActivity.class.getSimpleName();

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

//  COMPLETED (18) Create a String array containing the names of the desired data columns from our ContentProvider
//  COMPLETED (19) Create constant int values representing each column name's position above
//  COMPLETED (20) Create a constant int to identify our loader used in DetailActivity

    private static final int LOADER_DETAIL = 101;

    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

//  COMPLETED (15) Declare a private Uri field called mUri
    private Uri mUri;

//  COMPLETED (10) Remove the mWeatherDisplay TextView declaration
//    private TextView mWeatherDisplay;

//  COMPLETED (11) Declare TextViews for the date, description, high, low, humidity, wind, and pressure
    private TextView mDayDate;
    private TextView mDayDescription;
    private TextView mDayHighTemp;
    private TextView mDayLowTemp;
    private TextView mDayHumidity;
    private TextView mDayWind;
    private TextView mDayPressure;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//      COMPLETED (12) Remove mWeatherDisplay TextView
//        mWeatherDisplay = (TextView) findViewById(R.id.tv_display_weather);
//      COMPLETED (13) Find each of the TextViews by ID
        mDayDate = (TextView) findViewById(R.id.tv_day_date);
        mDayDescription = (TextView) findViewById(R.id.tv_day_description);
        mDayHighTemp = (TextView) findViewById(R.id.tv_day_high_temp);
        mDayLowTemp = (TextView) findViewById(R.id.tv_day_low_temp);
        mDayHumidity = (TextView) findViewById(R.id.tv_day_humidity);
        mDayWind = (TextView) findViewById(R.id.tv_day_wind);
        mDayPressure = (TextView) findViewById(R.id.tv_day_pressure);

//      COMPLETED (14) Remove the code that checks for extra text
        Intent intentThatStartedThisActivity = getIntent();
//        if (intentThatStartedThisActivity != null) {
//            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
//                mForecastSummary = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
//                mWeatherDisplay.setText(mForecastSummary);
//            }
//        }

//      COMPLETED (16) Use getData to get a reference to the URI passed with this Activity's Intent
        if (intentThatStartedThisActivity != null) {
            mUri = intentThatStartedThisActivity.getData();
        }

//      COMPLETED (17) Throw a NullPointerException if that URI is null
        if (mUri == null) {
            throw new NullPointerException("Invalid Uri");
        }

//      COMPLETED (35) Initialize the loader for DetailActivity
        getSupportLoaderManager().initLoader(LOADER_DETAIL, null, this);

    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     *
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     *
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }


//  COMPLETED (22) Override onCreateLoader
//          COMPLETED (23) If the loader requested is our detail loader, return the appropriate CursorLoader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == LOADER_DETAIL) {
            return new android.support.v4.content.AsyncTaskLoader<Cursor>(this) {

                Cursor mWeatherData = null;

                // onStartLoading() is called when a loader first starts loading data
                @Override
                protected void onStartLoading() {
                    if (mWeatherData != null) {
                        // Delivers any previously loaded data immediately
                        deliverResult(mWeatherData);
                    } else {
                        // Force a new load
                        forceLoad();
                    }
                }

                // loadInBackground() performs asynchronous loading of data
                @Override
                public Cursor loadInBackground() {
                    // Will implement to load data

                    Log.e(TAG, "Querying the detail data...");
                    Log.e(TAG, mUri.toString());

                    // [Hint] use a try/catch block to catch any errors in loading data

                    try {
                        return getContentResolver().query(mUri,
                                null,
                                null,
                                null,
                                null);

                    } catch (Exception e) {
                        Log.e(TAG, "Failed to asynchronously load data.");
                        e.printStackTrace();
                        return null;
                    }
                }

                // deliverResult sends the result of the load, a Cursor, to the registered listener
                public void deliverResult(Cursor data) {
                    mWeatherData = data;
                    super.deliverResult(data);
                }
            };
        }

        return null;
    }

//  COMPLETED (24) Override onLoadFinished
//      COMPLETED (25) Check before doing anything that the Cursor has valid data
//      COMPLETED (26) Display a readable data string
//      COMPLETED (27) Display the weather description (using SunshineWeatherUtils)
//      COMPLETED (28) Display the high temperature
//      COMPLETED (29) Display the low temperature
//      COMPLETED (30) Display the humidity
//      COMPLETED (31) Display the wind speed and direction
//      COMPLETED (32) Display the pressure
//      COMPLETED (33) Store a forecast summary in mForecastSummary
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) return;
        Log.e(TAG, data.toString());

        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            Log.e(TAG, "No DATA");
            return;
        }



        String dateDisp = SunshineDateUtils.getFriendlyDateString(this,
                data.getLong(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE)),
                false
        );

        String description = SunshineWeatherUtils.getStringForWeatherCondition(this,
                data.getInt(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID))
        );

        String highTemp = data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP));
        String lowTemp = data.getString(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP));

        mDayDate.setText(dateDisp);
        mDayDescription.setText(description);
        mDayHighTemp.setText(highTemp);
        mDayLowTemp.setText(lowTemp);

        mForecastSummary = dateDisp + " - " + description + " - " + highTemp + " - " + lowTemp;
    }

//  COMPLETED (34) Override onLoaderReset, but don't do anything in it yet
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}