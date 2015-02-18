package com.hw.thomasfrow.invenfc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CheckBox;
import android.content.ContentValues;
import android.widget.Toast;
import android.app.Activity;
import android.widget.Toolbar;



public class viewItem extends Activity {

    private ItemDataSource dataSource;
    private View view2;
    private Item item;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item2);
        int id = getIntent().getExtras().getInt("id");

        dataSource = new ItemDataSource();
        dataSource.open();

        System.out.print(id);

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
            case R.id.fabCamera:
                Toast.makeText(getApplicationContext(),"Camera activity should be started",Toast.LENGTH_LONG).show();
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

                        int owner = 0;

                        //Item item = datasource.createItem(owner,nameEdit.getText().toString(),roomEdit.getText().toString(),brandEdit.getText().toString(),modelEdit.getText().toString(),commentEdit.getText().toString());

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_item, menu);
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
