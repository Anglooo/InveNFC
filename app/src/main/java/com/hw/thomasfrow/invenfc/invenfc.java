package com.hw.thomasfrow.invenfc;

import android.content.Intent;
import android.os.Bundle;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;


public class invenfc extends Activity {

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        invenfc.context = getApplicationContext();
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

    public void sendMessage2(View view) {

        Intent intent = new Intent(this, TestDatabaseActivity.class);
        startActivity(intent);
    }

    public void sendMessage3(View view) {

        Intent intent = new Intent(this, showInventoryActivity.class);
        startActivity(intent);
    }


}
