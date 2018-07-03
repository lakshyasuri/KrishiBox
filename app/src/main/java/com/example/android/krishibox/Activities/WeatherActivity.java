package com.example.android.krishibox.Activities;

import android.app.Fragment;

import com.example.android.krishibox.Fragments.WeatherFragment;

public class WeatherActivity extends BaseWeatherFragActivity {


    @Override
    Fragment createFragment() {
        return WeatherFragment.newInstance();
    }
}
