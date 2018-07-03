package com.example.android.krishibox.Collections;

public class User {

    private String mName;
    private String mNumber;
    private String mID;

    public User (String name, String number, String ID)
    {
        mName = name;
        mNumber = number;
        mID = ID;
    }

    public String getName()
    {
        return mName;
    }

    public String getNumber()
    {
        return mNumber;
    }

    public String getID()
    {
        return mID;
    }
}
