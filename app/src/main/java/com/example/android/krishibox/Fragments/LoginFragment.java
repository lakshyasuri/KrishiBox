package com.example.android.krishibox.Fragments;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.krishibox.Activities.WeatherActivity;
import com.example.android.krishibox.Loaders.LoginLoader;
import com.example.android.krishibox.R;
import com.example.android.krishibox.Utils.Constants;
import com.example.android.krishibox.Utils.LoginUtil;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class LoginFragment extends BaseFragment implements LoaderCallbacks<String[]> {

    EditText userName, userNumber;
    Timer t;
    TimerTask timerTask;
    String name, number;
    private static final int LOGIN_LOADER_ID = 1;
    LoaderManager loaderManager;

    public static LoginFragment newInstance() {return new LoginFragment();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login,container,false);

        userName = rootView.findViewById(R.id.fragment_login_name);
        userNumber = rootView.findViewById(R.id.fragment_login_number);


        Button register = rootView.findViewById(R.id.fragment_login_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userName.getText().toString().trim();
                number = userNumber.getText().toString().trim();
                if(name.isEmpty())
                {
                    userName.setError("Please enter your name");
                } else if(number.isEmpty())
                {
                    userNumber.setError("please enter your number");
                }
                else
                {
                    loaderManager = getLoaderManager();
                    loaderManager.initLoader(LOGIN_LOADER_ID,null,LoginFragment.this);

                }
            }
        });

        return rootView;
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {


        Uri uri = Uri.parse(Constants.REGISTER_URL_POST);
        return new LoginLoader(getActivity(),uri.toString(),name,number);

    }

    @Override
    public void onLoadFinished(final Loader<String[]> loader, String[] data) {



        if(data[1].equals("no"))
        {
            Log.e("sad",data[1]);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getActivity(), "Server problem! Retrying", Toast.LENGTH_SHORT).show();
                    getLoaderManager().destroyLoader(loader.getId());
                    loaderManager.initLoader(2,null,LoginFragment.this);

                }
            },10000);

        }

        if(data[1].equals("400"))
        {
            Log.v("onLOADFINISHED",data[1]);
            getLoaderManager().destroyLoader(loader.getId());
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Response Code: "+data[1])
                    .setMessage("There seems to be a problem with your credentials")
                    .setPositiveButton("Re-enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loaderManager.initLoader(2,null,LoginFragment.this);
                            dialog.cancel();
                            getActivity().recreate();
                            //getActivity().recreate();
                            //loaderManager.initLoader(2,null,LoginFragment.this);

                        }
                    }).setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            }).create().show();

        }

        if(data[1].equals("200"))
        {
            Toast.makeText(getActivity(), "Registered !!", Toast.LENGTH_SHORT).show();
            LoginUtil.extractFromJson(data[0]);
            startActivity(new Intent(getActivity(), WeatherActivity.class));
        }

//         LoginUtil.extractFromJson(data);
       // Toast.makeText(getActivity(), "Login Successfull!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }


}
