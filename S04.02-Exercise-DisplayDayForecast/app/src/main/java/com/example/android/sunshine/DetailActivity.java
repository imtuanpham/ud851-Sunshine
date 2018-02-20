package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    private TextView mWeatherDetailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mWeatherDetailTextView = (TextView) findViewById(R.id.tv_weather_detail);

        // COMPLETED (2) Display the weather forecast that was passed from MainActivity
        Intent parentIntent = getIntent();

        if (parentIntent.hasExtra(Intent.EXTRA_TEXT)) {
            mWeatherDetailTextView.setText(parentIntent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }
}