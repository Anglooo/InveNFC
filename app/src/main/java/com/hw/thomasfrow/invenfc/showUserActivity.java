package com.hw.thomasfrow.invenfc;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class showUserActivity extends ActionBarActivity {

    private View view2;
    private String ownID;
    ParseUser currentUser;
    boolean addButtonPressed = false;
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        currentUser = ParseUser.getCurrentUser();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);

        ownID = currentUser.getObjectId();

        Log.i("UserIDView", currentUser.getObjectId());



        updateInterface();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.menu_view_item);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_editItem:

                        //editItemInterface();

                        return true;

                    case R.id.action_deleteItem:

                        //deleteItemInterface();

                        return true;
                }

                return false;
            }


        });


        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        toolbar.setNavigationOnClickListener(new Toolbar.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), showInventoryActivity.class);
                startActivity(intent);
            }
        });

        drawImageInterface();


    }

    private void updateInterface() {

        TextView editView;

        editView = (TextView) this.findViewById(R.id.nameOutView);
        if(currentUser.has("name")){
            editView.setText(currentUser.get("name").toString());
        }else{
            editView.setVisibility(View.INVISIBLE);
        }

        editView = (TextView) this.findViewById(R.id.outIDView);
        editView.setVisibility(View.VISIBLE);
        editView.setText(currentUser.getObjectId());

        editView = (TextView) findViewById(R.id.outUserNameView);
        editView.setText(currentUser.getUsername().toString());


        editView = (TextView) findViewById(R.id.outEmailView);
        editView.setText(currentUser.getEmail().toString());

        editView = (TextView) findViewById(R.id.outCreatedView);
        editView.setText(currentUser.getCreatedAt().toString());

    }

    public void onClick(final View view) {

        switch (view.getId()) {
            case R.id.fabOpenAddMenu:


                ImageButton addPhoto = (ImageButton) findViewById(R.id.addPhotoButton);
                ImageButton addTag = (ImageButton) findViewById(R.id.addTagButton);

                ImageButton openAddMenu = (ImageButton) findViewById(R.id.fabOpenAddMenu);


                if (!addButtonPressed) {
                    addButtonPressed = !addButtonPressed;

                    openAddMenu.setBackgroundResource(R.drawable.button_cross);

                    addPhoto.setVisibility(View.VISIBLE);

                    addTag.setVisibility(View.VISIBLE);


                } else {

                    openAddMenu.setBackgroundResource(R.drawable.button_picker_plus);


                    addButtonPressed = !addButtonPressed;

                    addPhoto.setVisibility(View.GONE);

                    addTag.setVisibility(View.GONE);

                }


                break;

            case R.id.addPhotoButton:


                dispatchTakePictureIntent();

                break;

        }

    }

    private void dispatchTakePictureIntent() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            saveImage(photo);

            drawImageInterface();

            //imageView.setImageBitmap(photo);
        }
    }

    private void saveImage(Bitmap image) {

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

    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Photos");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "PHOTO_" + currentUser.getObjectId()+ ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private void drawImageInterface() {

        String fileName = "PHOTO_" + currentUser.getObjectId() + ".jpg";
        //File file = getBaseContext().getFileStreamPath(fileName);
        imageView = (ImageView) findViewById(R.id.itemImageView);

        File file = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Photos/" + fileName);

        Log.i("PHOTO", file.toString());
        if (file.exists()) {

            Log.i("PHOTO", "FILE EXISTS");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            //bitmap.setWidth(100);
            imageView.setImageBitmap(bitmap);
        }

    }

    protected void onResume() {

        updateInterface();
        super.onResume();

    }

    @Override
    protected void onPause() {
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
    public void onBackPressed() {

        Intent intent = new Intent(this, showInventoryActivity.class);
        startActivity(intent);

    }

}
