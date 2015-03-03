package com.hw.thomasfrow.invenfc;

import android.content.Intent;
import android.os.Bundle;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.nfc.NdefRecord;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.ParseACL;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseObject;


public class invenfc extends Activity {

    private static Context context;
    int itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Code for deleting database
        //context.deleteDatabase(MySQLiteHelper.DATABASE_NAME);

        setContentView(R.layout.activity_main);
        invenfc.context = getApplicationContext();

        Log.i("checkLoggedIn",Boolean.toString(checkLoggedIn()));


        if(checkLoggedIn()){

            if(!readTag(getIntent())){
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

       // SharedPreferences prefs = getApplicationContext().getSharedPreferences("userDetails",Context.MODE_PRIVATE);

        // Determine whether the current user is an anonymous user
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // If user is anonymous, send the user to LoginSignupActivity.class
            return false;
        } else {
            // If current user is NOT anonymous user
            // Get current user data from Parse.com
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                // Send logged in users to Welcome.class
                return true;

            } else {
                // Send user to LoginSignupActivity.class
                return false;
            }
        }

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
