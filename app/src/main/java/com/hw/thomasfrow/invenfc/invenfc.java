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
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.nfc.NdefRecord;

import android.util.Log;
import android.widget.Toast;
import java.util.Arrays;
import java.io.UnsupportedEncodingException;




public class invenfc extends Activity {

    private static Context context;
    private boolean loggedIn = false;
    int itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        invenfc.context = getApplicationContext();

        Log.i("checkLoggedIn",Boolean.toString(checkLoggedIn()));


        if(checkLoggedIn()){
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("userDetails",Context.MODE_PRIVATE);
            int userID = prefs.getInt("userID",12345);


            if(!readTag(getIntent())){
                Log.i("ownerID1",Integer.toString(userID));
                Intent intent = new Intent(this,showInventoryActivity.class);
                startActivity(intent);
            }else{
                Log.i("NFCREAD","Should start intent with id");
                Log.i("NFCREAD",Integer.toString(itemID));
                Intent intentStart = new Intent(this, viewItem.class);
                intentStart.putExtra("id",itemID);
                startActivity(intentStart);

            }

        }else{

            if(!readTag(getIntent())){
                Log.i("NFCREAD", "shouldnt come here after read");
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
            }else{

                Log.i("NFCREAD","Should start intent with id");
                Log.i("NFCREAD",Integer.toString(itemID));
                Intent intent = new Intent(this, viewItem.class);
                intent.putExtra("id",itemID);
                startActivity(intent);
            }

        }



    }

    private boolean readTag(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            Log.i("NFCREAD","Gets here 1");

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Ndef ndef = Ndef.get(tag);
            Log.i("NFCREAD","Gets here 2");
            Log.i("NFCREAD",ndef.toString());

            if(ndef==null){
                Log.i("NFCREAD","Error Reading tag");
                Toast.makeText(this,"Error Reading Tag", Toast.LENGTH_SHORT).show();
                return false;
            }
            NdefMessage message = ndef.getCachedNdefMessage();
            Log.i("NFCREAD","Gets here 3");
            Log.i("NFCREAD",message.toString());

            NdefRecord[]records = message.getRecords();
            Log.i("NFCREAD","Gets here 4");
            Log.i("NFCREAD",records.toString());

            for(NdefRecord ndefRecord: records){
                    Log.i("NFCREAD","Gets here 5");


                    byte[] payload = ndefRecord.getPayload();
                    String payString = new String(payload);
                    Log.i("NFCREAD",payString);
                    itemID = Integer.parseInt(payString);

                    Log.i("NFCREAD","got to 6 and not started?");


                    return true;
            }

        }else{
            return false;
        }

        return false;
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

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
