package com.hw.thomasfrow.invenfc;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;


public class InvenfcApplication extends Application {

public void onCreate(){
    super.onCreate();

    Parse.enableLocalDatastore(this);

    Log.i("parse","init");
    Parse.initialize(this, "eamqdPdo7sy5YyijMDOUYuBasl2uBdFOAgjN9DaO", "fBEcBcxNF3sXc6fZN0YGaQlhkHMcrf2DKMJ4vu4d");

    ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();

    ParseACL.setDefaultACL(defaultACL, true);
}
}S