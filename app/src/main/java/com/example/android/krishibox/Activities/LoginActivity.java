package com.example.android.krishibox.Activities;

import android.app.Fragment;

import com.example.android.krishibox.Fragments.LoginFragment;

public class LoginActivity extends BaseFragmentActivity {


    @Override
    Fragment createFragment() {
        return LoginFragment.newInstance();

    }




}
