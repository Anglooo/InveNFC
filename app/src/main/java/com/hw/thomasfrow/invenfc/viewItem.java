package com.hw.thomasfrow.invenfc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CheckBox;
import android.content.ContentValues;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.ImageButton;



public class viewItem extends Activity{

    private ItemDataSource dataSource;
    private View view2;
    private Item item;
    private boolean loggedInStatus;
    private int ownID;
    private boolean addButtonPressed = false;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item2);


        if(getIntent().getExtras() != null){
            id = getIntent().getExtras().getInt("id");

        }

        dataSource = new ItemDataSource();
        dataSource.open();

        item = dataSource.getItemByID(id);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        boolean loggedInStatus = prefs.getBoolean("isLoggedIn",false);
        ownID = prefs.getInt("userID",9999);

        if(loggedInStatus != true){
            new AlertDialog.Builder(this)
                    .setTitle("You are not logged in.")
                    .setCancelable(false)
                    .setView(R.layout.dialog_go_login)
                    .setPositiveButton("Login",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("redirect",true);
                            intent.putExtra("redirID",id);
                            startActivity(intent);
                        }
                    })
                    .show();
        }else{

            if(ownID != item.getOwnerID()){
                new AlertDialog.Builder(this)
                        .setTitle("Permission Denied")
                        .setView(R.layout.dialog_not_owner)
                        .setCancelable(false)
                        .setPositiveButton("View Inventory", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(getApplicationContext(),showInventoryActivity.class);
                                startActivity(intent);

                            }
                        })
                        .show();
            }

        }



        updateInterface(id);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.menu_view_item);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_editItem:

                        editItemInterface();

                        return true;

                    case R.id.action_deleteItem:

                        deleteItemInterface();

                        return true;
                }

                return false;
            }


        });



        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        toolbar.setNavigationOnClickListener(new Toolbar.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),showInventoryActivity.class);
                intent.putExtra("userID",ownID);
                startActivity(intent);
            }
        });


    }

    private void updateInterface(int id){

        System.out.println("updateInterface called.");

        item = dataSource.getItemByID(id);

        TextView editView;

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("ITEM: " + item.getName());

        ImageView imageView = (ImageView)findViewById(R.id.itemImageView);


        editView = (TextView)this.findViewById(R.id.outIDView);
        System.out.print(R.id.outIDView);
        editView.setText(Integer.toString(item.getId()));


        editView = (TextView)findViewById(R.id.outBrandView);
        editView.setText(item.getBrand());

        editView = (TextView)findViewById(R.id.outModelView);
        editView.setText(item.getModel());

        editView = (TextView)findViewById(R.id.outRoomView);
        editView.setText(item.getRoom());

        editView = (TextView)findViewById(R.id.outCommentView);
        editView.setText(item.getComment());

    }

    public void onClick(final View view) {

        switch (view.getId()) {
            case R.id.fabOpenAddMenu:
                ImageButton addPhoto = (ImageButton)findViewById(R.id.addPhotoButton);
                ImageButton addTag = (ImageButton)findViewById(R.id.addTagButton);

                ImageButton openAddMenu = (ImageButton)findViewById(R.id.fabOpenAddMenu);


                if(!addButtonPressed){

                    addButtonPressed = !addButtonPressed;

                    openAddMenu.setBackgroundResource(R.drawable.button_cross);

                    addPhoto.setVisibility(View.VISIBLE);

                    addTag.setVisibility(View.VISIBLE);

                }else{

                    openAddMenu.setBackgroundResource(R.drawable.button_picker_plus);


                    addButtonPressed = !addButtonPressed;

                    addPhoto.setVisibility(View.GONE);

                    addTag.setVisibility(View.GONE);

                }


                break;

            case R.id.addPhotoButton:

                Toast.makeText(getApplicationContext(),"camera activity start",Toast.LENGTH_SHORT);
                Log.i("buttonPress","Start Photo");

                break;

            case R.id.addTagButton:

                Log.i("buttonPress","Start Tag");
                Intent intent = new Intent(this, createTagActivity.class);
                Log.i("NFCStringOutput",Integer.toString(id));
                intent.putExtra("itemID",id);
                startActivity(intent);

                break;


        }

    }

    private void deleteItemInterface(){

        LayoutInflater inflater = this.getLayoutInflater();
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

                        dataSource.deleteItem(item);
                        Toast.makeText(getApplicationContext(),"Item " + item.getId() + " has been deleted",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), showInventoryActivity.class);
                        startActivity(intent);

                    }

                })
                .show();

    }

    private void editItemInterface(){

        LayoutInflater inflater = this.getLayoutInflater();
        view2 = inflater.inflate(R.layout.dialog_edit_item, null);
        new AlertDialog.Builder(this)
                .setTitle("Edit Item")
                .setView(view2)

                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        final EditText nameEdit = (EditText)view2.findViewById(R.id.enterName);
                        final EditText roomEdit = (EditText)view2.findViewById(R.id.enterRoom);
                        final EditText brandEdit = (EditText)view2.findViewById(R.id.enterBrand);
                        final EditText modelEdit = (EditText)view2.findViewById(R.id.enterModel);
                        final EditText commentEdit = (EditText)view2.findViewById(R.id.enterComment);

                        nameEdit.setHint(item.getName());
                        roomEdit.setHint(item.getRoom());
                        brandEdit.setHint(item.getBrand());
                        modelEdit.setHint(item.getModel());
                        commentEdit.setHint(item.getComment());

                        ContentValues updateItem = new ContentValues();

                        CheckBox checkName = (CheckBox)view2.findViewById(R.id.checkName);

                        System.out.print(checkName.isChecked());

                        boolean itemHasUpdated = false;

                        if(checkName.isChecked()){
                            String editedName = nameEdit.getText().toString();
                            if(editedName.trim().length() == 0){
                                Toast.makeText(getApplicationContext(),"Please insert a value to edit in Name",Toast.LENGTH_SHORT).show();
                            }else{
                                updateItem.put(MySQLiteHelper.NAME,editedName);
                                itemHasUpdated = true;

                            }

                        }

                        CheckBox checkRoom = (CheckBox)view2.findViewById(R.id.checkRoom);

                        if(checkRoom.isChecked()){
                            String editedRoom = roomEdit.getText().toString();
                            if(editedRoom.trim().length() == 0){
                                Toast.makeText(getApplicationContext(),"Please insert a value to edit in Room",Toast.LENGTH_SHORT).show();
                            }else{
                                updateItem.put(MySQLiteHelper.ROOM,editedRoom);
                                itemHasUpdated = true;

                            }
                        }

                        CheckBox checkModel = (CheckBox)view2.findViewById(R.id.checkModel);

                        if(checkModel.isChecked()){
                            String editedModel = modelEdit.getText().toString();
                            if(editedModel.trim().length() == 0){
                                Toast.makeText(getApplicationContext(),"Please insert a value to edit in Model",Toast.LENGTH_SHORT).show();
                            }else{
                                updateItem.put(MySQLiteHelper.MODEL,editedModel);
                                itemHasUpdated = true;
                            }
                        }

                        CheckBox checkBrand = (CheckBox)view2.findViewById(R.id.checkBrand);

                        if(checkBrand.isChecked()){
                            String editedBrand = brandEdit.getText().toString();
                            if(editedBrand.trim().length() == 0){
                                Toast.makeText(getApplicationContext(),"Please insert a value to edit in Brand",Toast.LENGTH_SHORT).show();
                            }else{
                                updateItem.put(MySQLiteHelper.BRAND,editedBrand);
                                itemHasUpdated = true;

                            }
                        }

                        CheckBox checkComment = (CheckBox)view2.findViewById(R.id.checkComment);

                        if(checkComment.isChecked()){
                            String editedComment = commentEdit.getText().toString();
                            if(editedComment.trim().length() == 0){
                                Toast.makeText(getApplicationContext(),"Please insert a value to edit in Comment",Toast.LENGTH_SHORT).show();
                            }else{
                                updateItem.put(MySQLiteHelper.COMMENT,editedComment);
                                itemHasUpdated = true;

                            }
                        }

                        if(itemHasUpdated){

                            if(dataSource.updateItem(item.getId(),updateItem) != 0){
                                System.out.println(updateItem.toString());
                                Toast.makeText(getApplicationContext(),"Item updated",Toast.LENGTH_LONG).show();
                                updateInterface(item.getId());
                            }else{
                                Toast.makeText(getApplicationContext(),"There was a problem with the update",Toast.LENGTH_LONG).show();

                            }

                        }else{
                            Toast.makeText(getApplicationContext(),"Nothing has been updated, Please use check box to select fields.",Toast.LENGTH_LONG).show();
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

    protected void onResume() {



        dataSource.open();
        super.onResume();


    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_item, menu);
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

    @Override
    public void onBackPressed(){

        Intent intent = new Intent(this,showInventoryActivity.class);
        startActivity(intent);

    }


}
