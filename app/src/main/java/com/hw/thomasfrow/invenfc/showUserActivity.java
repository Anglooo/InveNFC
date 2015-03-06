package com.hw.thomasfrow.invenfc;

import android.app.AlertDialog;
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

        toolbar.inflateMenu(R.menu.menu_show_user);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_editUser:

                        drawEditInterface();

                        return true;

                    case R.id.action_deleteItem:



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

            case R.id.fabCameraButton:


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

    private void drawEditInterface(){

        LayoutInflater inflater = this.getLayoutInflater();
        view2 = inflater.inflate(R.layout.dialog_edit_account, null);
        new AlertDialog.Builder(this)
                .setTitle("Edit Account Details")
                .setView(view2)

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        final EditText nameEdit = (EditText)view2.findViewById(R.id.enterName);
                        final EditText usernameEdit = (EditText)view2.findViewById(R.id.enterUsername);
                        final EditText emailEdit = (EditText)view2.findViewById(R.id.enterEmail);
                        final EditText newPass1Edit = (EditText)view2.findViewById(R.id.enterNewPass1);
                        final EditText newPass2Edit = (EditText)view2.findViewById(R.id.enterNewPass2);


                        CheckBox checkName = (CheckBox)view2.findViewById(R.id.checkName);
                        CheckBox checkUsername = (CheckBox)view2.findViewById(R.id.checkUsername);
                        CheckBox checkEmail = (CheckBox)view2.findViewById(R.id.checkEmail);
                        CheckBox checkPass = (CheckBox)view2.findViewById(R.id.checkPassword);

                        boolean hasUpdated = false;

                        if(checkName.isChecked()){
                            String editedName = nameEdit.getText().toString();
                            if(editedName.trim().length() == 0){
                                Toast.makeText(getApplicationContext(),"Please insert a value to edit in Name",Toast.LENGTH_SHORT).show();
                            }else{
                                currentUser.put("name",editedName);
                                hasUpdated = true;
                            }
                        }

                        if(checkUsername.isChecked()){
                            String editedUsername = usernameEdit.getText().toString();
                            if(editedUsername.trim().length() == 0){
                                Toast.makeText(getApplicationContext(),"Please insert a value to edit in Username",Toast.LENGTH_SHORT).show();
                            }else{
                                currentUser.setUsername(editedUsername);
                                hasUpdated = true;
                            }
                        }

                        if(checkEmail.isChecked()){
                            String editedEmail = emailEdit.getText().toString();
                            if(editedEmail.trim().length() == 0){
                                Toast.makeText(getApplicationContext(),"Please insert a value to edit in Email",Toast.LENGTH_SHORT).show();
                            }else{
                                currentUser.setEmail(editedEmail);
                                hasUpdated = true;
                            }
                        }

                        if(checkPass.isChecked()){
                            String newPass1 = newPass1Edit.getText().toString();
                            String newPass2 = newPass2Edit.getText().toString();
                            if(newPass1.trim().length() == 0){
                                Toast.makeText(getApplicationContext(),"Please insert your a new password.", Toast.LENGTH_SHORT);
                            }else{
                                if(newPass1.equals(newPass2)){
                                    currentUser.setPassword(newPass1);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Password values do not match.", Toast.LENGTH_SHORT);
                                }
                            }
                        }

                        if(!hasUpdated){
                            Toast.makeText(getApplicationContext(),"No values inserted. Nothing has been changed.",Toast.LENGTH_LONG).show();
                        }else{

                            currentUser.saveInBackground();
                            updateInterface();
                            Toast.makeText(getApplicationContext(),"Updates done successfully.",Toast.LENGTH_LONG).show();

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
