package com.hw.thomasfrow.invenfc;

import android.content.Intent;
import android.os.Bundle;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.*;


public class invenfc extends Activity {

    private static Context context;
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        invenfc.context = getApplicationContext();

        Log.i("checkLoggedIn",Boolean.toString(checkLoggedIn()));

        if(checkLoggedIn()){
            //Intent intent = new Intent(this, viewItem.class);
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("userDetails",Context.MODE_PRIVATE);
            int userID = prefs.getInt("ownerID",12345);
            Log.i("prefsOut",prefs.toString());

            Log.i("ownerID1",Integer.toString(userID));
            Intent intent = new Intent(this,showInventoryActivity.class);
            intent.putExtra("userID",userID);
            startActivity(intent);

        }else{

            Intent intent = new Intent(this,LoginActivity.class);
            // Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
        }



    }

    private boolean checkLoggedIn() {

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("userDetails",Context.MODE_PRIVATE);
        boolean defaultLogin = false;

        return prefs.getBoolean("isLoggedIn",defaultLogin);

     }

    public static Context getAppContext() {
        return invenfc.context;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
