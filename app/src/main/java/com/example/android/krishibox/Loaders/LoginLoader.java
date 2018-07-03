package com.example.android.krishibox.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.krishibox.Fragments.LoginFragment;
import com.example.android.krishibox.Utils.LoginUtil;

import org.json.JSONObject;

public class LoginLoader extends AsyncTaskLoader<String[]> {

    private String mUrl;
    private String mName;
    private String mNumber;
    public LoginLoader(Context context, String url, String name, String number) {
        super(context);
        mUrl = url;
        mName = name;
        mNumber = number;


    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }



    @Override
    public String[] loadInBackground() {

        if(mUrl==null||mName==null||mNumber==null)
        {
            return null;
        }
       return LoginUtil.sendRegistrationData(mName,mNumber,mUrl);
    }
}
