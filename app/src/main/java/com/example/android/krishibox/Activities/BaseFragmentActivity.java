package com.example.android.krishibox.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.android.krishibox.R;

public abstract class BaseFragmentActivity extends AppCompatActivity {

    abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_base);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected())
        {
            if(fragment == null)
            {
                fragment = createFragment();
                fragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit();
            }
        }
        else
        {
            View view = findViewById(R.id.connectivity_text);
            view.setVisibility(View.VISIBLE);
        }


    }
}
