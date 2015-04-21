package com.hw.thomasfrow.invenfc;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;

import com.parse.ParseUser;


public class showInventoryActivity extends Activity {

    private ItemDataSource dataSource;
    private View view2;
    private String ownID;
    ParseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_inventory);

        currentUser = ParseUser.getCurrentUser();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //boolean firstLogin = extras.getBoolean("firstLogin");

        if(extras != null){
            if(extras.containsKey("firstLogin")){
                String toastString = "Welcome " + currentUser.getUsername() + ". To add an item, press the + button.";
                Toast.makeText(this, toastString,Toast.LENGTH_LONG).show();
            }
        }


        SharedPreferences prefs = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);

        //ownID = prefs.getString("userID","9999");
        ownID = currentUser.getObjectId();


        drawInterface();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.menu_show_inventory);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_addItem:

                        addItemInterface();

                        return true;

                    case R.id.action_logOut:

                        logOutInterface();

                        return true;
                }

                return false;
            }


        });


    }

    public void drawInterface(){
        dataSource = new ItemDataSource();
        dataSource.open();


        List<Item> items = dataSource.getItemsByOwner(ownID);

        LayoutInflater inflater = LayoutInflater.from(this);

        LinearLayout inside = (LinearLayout)findViewById(R.id.list_item);
        inside.removeAllViews();

        if(items.size() != 0){

            for(Item it : items){

                View itemView = inflater.inflate(R.layout.item_in_inventory,null, false);
                itemView.setClickable(true);

                TextView outName = (TextView) itemView.findViewById(R.id.nameView);
                String name = it.getName();
                if(name.length() > 18 ){
                    name = it.getName().substring(0,7);
                    name = name.concat("...");
                }
                outName.setText(name);


                TextView outID = (TextView) itemView.findViewById(R.id.idView);
                String id = Integer.toString(it.getId());
                outID.setText(id);

                TextView outRoom = (TextView) itemView.findViewById(R.id.roomView);
                String room = it.getRoom();
                if(room.length() > 10 ){
                    room = it.getRoom().substring(0,7);
                    room = room.concat("...");
                }
                outRoom.setText(room);


                final int id1 = it.getId();

                inside.addView(itemView);

                ImageButton trashButton = (ImageButton)itemView.findViewById(R.id.imageButtonDelete);

                trashButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteInterface(id1);

                    }
                });



                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), viewItem.class);
                        intent.putExtra("id", id1);
                        startActivity(intent);
                    }
                });

            }
        }else{

            View itemView = inflater.inflate(R.layout.inventory_empty,null, false);
            //Log.i("ID",ownID);
            inside.addView(itemView);

        }


    }

    private void deleteInterface(final int id){

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        view2 = inflater.inflate(R.layout.dialog_delete_item, null);

        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setView(view2)
                .setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dataSource.deleteItemByID(id);
                        Toast.makeText(getApplicationContext(),"Item has been deleted",Toast.LENGTH_LONG).show();
                        drawInterface();
                        //Intent intent = new Intent(getApplicationContext(), showInventoryActivity.class);
                        //startActivity(intent);

                    }

                })
                .show();

    }

    public void onClick(final View view) {

        switch (view.getId()) {
            case R.id.fabAdd:

                addItemInterface();

                break;

            case R.id.button_search:

                filterItemInterface();

                break;

            case R.id.button_account:

                Intent intent = new Intent(this, showUserActivity.class);
                startActivity(intent);

        }
    }
    
    private void filterItemInterface(){
        
            LayoutInflater inflater = this.getLayoutInflater();
            view2 = inflater.inflate(R.layout.dialog_search_item, null);
            new AlertDialog.Builder(this)
                    .setTitle("Filter Item")
                    .setView(view2)

                    .setPositiveButton("Filter items", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            final EditText nameEdit = (EditText)view2.findViewById(R.id.enterFilterName);
                            final EditText roomEdit = (EditText)view2.findViewById(R.id.enterFilterRoom);
                            final EditText brandEdit = (EditText)view2.findViewById(R.id.enterFilterBrand);
                            final EditText modelEdit = (EditText)view2.findViewById(R.id.enterFilterModel);

                            Bundle updateItem = new Bundle();

                            CheckBox checkFilterName = (CheckBox)view2.findViewById(R.id.checkFilterName);

                            System.out.print(checkFilterName.isChecked());

                            boolean itemHasUpdated = false;

                            if(checkFilterName.isChecked()){
                                String editedName = nameEdit.getText().toString();
                                if(editedName.trim().length() == 0){
                                    Toast.makeText(getApplicationContext(),"Please insert a value to filter.",Toast.LENGTH_SHORT).show();
                                }else{
                                    updateItem.putString(MySQLiteHelper.NAME,editedName);
                                    itemHasUpdated = true;

                                }

                            }

                            CheckBox checkFilterRoom = (CheckBox)view2.findViewById(R.id.checkFilterRoom);

                            if(checkFilterRoom.isChecked()){
                                String editedRoom = roomEdit.getText().toString();
                                if(editedRoom.trim().length() == 0){
                                    Toast.makeText(getApplicationContext(),"Please insert a value to filter.",Toast.LENGTH_SHORT).show();
                                }else{
                                    updateItem.putString(MySQLiteHelper.ROOM,editedRoom);
                                    itemHasUpdated = true;

                                }
                            }

                            CheckBox checkFilterModel = (CheckBox)view2.findViewById(R.id.checkFilterModel);

                            if(checkFilterModel.isChecked()){
                                String editedModel = modelEdit.getText().toString();
                                if(editedModel.trim().length() == 0){
                                    Toast.makeText(getApplicationContext(),"Please insert a value to filter.",Toast.LENGTH_SHORT).show();
                                }else{
                                    updateItem.putString(MySQLiteHelper.MODEL,editedModel);
                                    itemHasUpdated = true;
                                }
                            }

                            CheckBox checkFilterBrand = (CheckBox)view2.findViewById(R.id.checkFilterBrand);

                            if(checkFilterBrand.isChecked()){
                                String editedBrand = brandEdit.getText().toString();
                                if(editedBrand.trim().length() == 0){
                                    Toast.makeText(getApplicationContext(),"Please insert a value to filter.",Toast.LENGTH_SHORT).show();
                                }else{
                                    updateItem.putString(MySQLiteHelper.BRAND,editedBrand);
                                    itemHasUpdated = true;

                                }
                            }

                            if(itemHasUpdated){

                                Intent intent = new Intent(getApplicationContext(),filteredInventoryActivity.class);
                                intent.putExtras(updateItem);
                                startActivity(intent);

                                

                            }else{
                                Toast.makeText(getApplicationContext(),"Please select fields to filter.",Toast.LENGTH_LONG).show();
                            }

                        }
                    })

                    .setCancelable(true)

                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    })
                    .show();
    }

    private void addItemInterface(){

        LayoutInflater inflater = this.getLayoutInflater();
        view2 = inflater.inflate(R.layout.dialog_add_item, null);

        new AlertDialog.Builder(this)
                .setTitle("Add Item")
                .setView(view2)

                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        EditText nameEdit = (EditText)view2.findViewById(R.id.enterName);
                        EditText roomEdit = (EditText)view2.findViewById(R.id.enterRoom);
                        EditText brandEdit = (EditText)view2.findViewById(R.id.enterBrand);
                        EditText modelEdit = (EditText)view2.findViewById(R.id.enterModel);
                        EditText commentEdit = (EditText)view2.findViewById(R.id.enterComment);

                        dataSource.createItem(currentUser.getObjectId(), nameEdit.getText().toString(), roomEdit.getText().toString(), brandEdit.getText().toString(), modelEdit.getText().toString(), commentEdit.getText().toString());
                        Log.i("UserIDAdd",currentUser.getObjectId());
                        drawInterface();

                    }
                })

                .setCancelable(true)

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void logOutInterface(){

        LayoutInflater inflater = this.getLayoutInflater();
        view2 = inflater.inflate(R.layout.dialog_log_out, null);

        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setView(view2)

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("userDetails", getApplicationContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("isLoggedIn", false);
                        editor.putString("userID", "");
                        editor.commit();

                        ParseUser.logOut();
                        currentUser = ParseUser.getCurrentUser();

                        Intent intent = new Intent(getApplicationContext(), invenfc.class);
                        startActivity(intent);

                    }
                })

                .setCancelable(true)

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_inventory, menu);
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

    @Override
    protected void onResume() {
        drawInterface();
        super.onResume();
    }

    @Override
    protected void onPause() {
        drawInterface();
        dataSource.close();
        super.onPause();
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }
}
