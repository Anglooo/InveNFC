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
import android.os.Environment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.File;

import com.parse.ParseUser;


public class viewItem extends Activity{

    private ItemDataSource dataSource;
    private View view2;
    private Item item;
    private String ownID;
    private boolean addButtonPressed = false;
    int id;
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView imageView;
    ParseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item2);

        currentUser = ParseUser.getCurrentUser();


        if(getIntent().getExtras() != null){
            id = getIntent().getExtras().getInt("id");

        }

        dataSource = new ItemDataSource();
        dataSource.open();

        item = dataSource.getItemByID(id);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        boolean loggedInStatus = false;

        if(currentUser != null){

            loggedInStatus = true;

        }

        ownID = currentUser.getObjectId();
        Log.i("UserIDView",currentUser.getObjectId());

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

            Log.i("UserIDCompareOwnID",currentUser.getObjectId());
            Log.i("UserIDCompareOwnID",item.getOwnerID());


            if(!ownID.equals(item.getOwnerID())){
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

        drawImageInterface();


    }

    private void updateInterface(int id){


        item = dataSource.getItemByID(id);

        TextView editView;


        editView = (TextView)this.findViewById(R.id.nameOutView);
        editView.setText(item.getName());

        editView = (TextView)this.findViewById(R.id.outIDView);
        editView.setText(Integer.toString(item.getId()));

        editView = (TextView)findViewById(R.id.outBrandView);
        if(item.getBrand().equals("")){
            editView.setText("Edit item to add a value.");
        }else{
            editView.setText(item.getBrand());
        }

        editView = (TextView)findViewById(R.id.outModelView);
        if(item.getModel().equals("")){
            editView.setText("Edit item to add a value.");
        }else{
            editView.setText(item.getModel());
        }

        editView = (TextView)findViewById(R.id.outRoomView);
        if(item.getRoom().equals("")){
            editView.setText("Edit item to add a value.");
        }else{
            editView.setText(item.getRoom());
        }
        
        editView = (TextView)findViewById(R.id.outCommentView);
        if(item.getComment().equals("")){
            editView.setText("Edit item to add a value.");
        }else{
            editView.setText(item.getComment());
        }

        editView = (TextView)findViewById(R.id.outTagView);
        if(item.getTag()){
            editView.setText("A tag exists for this item");
        }else{
            editView.setText("There is no tag for this item");

        }


    }

    public void onClick(final View view) {

        switch (view.getId()) {
            case R.id.fabOpenAddMenu:
                TextView test = (TextView)findViewById(R.id.nameOutView);


                Log.i("BUG", Integer.toString(test.getVisibility()));
                ImageButton addPhoto = (ImageButton)findViewById(R.id.addPhotoButton);
                ImageButton addTag = (ImageButton)findViewById(R.id.addTagButton);

                ImageButton openAddMenu = (ImageButton)findViewById(R.id.fabOpenAddMenu);


                if(!addButtonPressed){
                    test.setVisibility(View.VISIBLE);
                    Log.i("BUG", Integer.toString(test.getVisibility()));
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


                dispatchTakePictureIntent();

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


    private void dispatchTakePictureIntent() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        dataSource.close();

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            saveImage(photo);

            drawImageInterface();

            //imageView.setImageBitmap(photo);
        }
    }

    private void saveImage(Bitmap image){

        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {

            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            //Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            //Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

    }

    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Photos");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="PHOTO_"+ item.getId() +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private void drawImageInterface(){

        String fileName = "PHOTO_" +item.getId() + ".jpg";
        //File file = getBaseContext().getFileStreamPath(fileName);
        imageView = (ImageView)findViewById(R.id.itemImageView);

        File file = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Photos/" +fileName);

        Log.i("PHOTO", file.toString());
        if(file.exists()){

            Log.i("PHOTO","FILE EXISTS");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            //bitmap.setWidth(100);
            imageView.setImageBitmap(bitmap);
        }

    }

    protected void onResume() {

        dataSource.open();
        updateInterface(id);
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

