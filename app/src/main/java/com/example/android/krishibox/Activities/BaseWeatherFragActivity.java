package com.example.android.krishibox.Activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.example.android.krishibox.R;

public abstract class BaseWeatherFragActivity extends AppCompatActivity {

    abstract Fragment createFragment();

    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_weather_base);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.weather_fragment_container);

        if(fragment==null)
        {
            fragment = createFragment();
            fragmentManager.beginTransaction().add(R.id.weather_fragment_container,fragment).commit();
        }

        /*getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View customActionBar = getSupportActionBar().getCustomView();*/
        setSupportActionBar(toolbar);

    }

    }
