package com.hw.thomasfrow.invenfc;

import android.content.ContentValues;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.app.PendingIntent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.LinearLayout;
import android.content.Intent;
import android.nfc.tech.*;
import android.nfc.*;
import java.io.IOException;

import android.widget.TextView;
import android.widget.Toolbar;

import java.nio.charset.Charset;
import android.content.IntentFilter.*;



public class createTagActivity extends ActionBarActivity {

    private ItemDataSource dataSource;
    NfcAdapter adapter;
    PendingIntent pendingIntent;
    Item item;
    int itemID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tag);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        itemID = getIntent().getExtras().getInt("itemID");

        dataSource = new ItemDataSource();
        dataSource.open();

        item = dataSource.getItemByID(itemID);


        adapter = NfcAdapter.getDefaultAdapter(this);

        Log.i("NFCAdapter","The adapter is below");
        Log.i("NFCAdapter",adapter.toString());


        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);




        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        }
        catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_tag, menu);
        return true;
    }

    public void onPause() {
        super.onPause();
       adapter.disableForegroundDispatch(this);
    }

    public void onResume() {
        super.onResume();

        adapter.enableForegroundDispatch(this, pendingIntent, null,null);
    }

    public void onNewIntent(Intent intent) {

        Log.i("NFC","new intent");
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Log.i("NFC",tagFromIntent.toString());

        try {
            write(tagFromIntent, itemID);
        }catch(IOException i){
            Log.i("NFC","IO Error");

        }catch(FormatException f){
            Log.i("NFC","Format Error");
        }
    }

    private void write(Tag tag, int id) throws IOException, FormatException {


        String stringID = Integer.toString(id);
        byte[] byteID = stringID.getBytes();
        Log.i("NFCString",stringID);



        NdefRecord appRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA ,
                "application/com.hw.thomasfrow.invenfc".getBytes(Charset.forName("US-ASCII")),
                new byte[0],byteID);
        NdefMessage message = new NdefMessage(new NdefRecord[] { appRecord });

        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();
        TextView outView = (TextView)findViewById(R.id.statusView);
        LinearLayout layout = (LinearLayout)findViewById(R.id.outLayout);
        layout.setBackgroundResource(R.color.mat_green);
        outView.setText("Tag has been successfuly created");
        ContentValues updateTag = new ContentValues();
        updateTag.put(SQLiteHelper.TAG,1);
        dataSource.updateItem(id,updateTag);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }

}
