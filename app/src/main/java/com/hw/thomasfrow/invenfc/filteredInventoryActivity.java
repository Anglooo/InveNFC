package com.hw.thomasfrow.invenfc;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;


public class filteredInventoryActivity extends ActionBarActivity {

    private ItemDataSource dataSource;
    private View view2;
    private View viewSearch;
    private int ownerID;
    private int ownID;
    private boolean isFiltered = false;
    private ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_inventory);

        Bundle bundle = getIntent().getExtras();

         values = new ContentValues();
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            String sValue = value.toString();

            Log.i("FILTER",sValue);
            Log.i("FILTER",key);
            values.put(key, sValue);
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("UI","Going back to inventory");
                dataSource.close();
                Intent intent = new Intent(getApplicationContext(), showInventoryActivity.class);
                startActivity(intent);
            }
        });


        drawFilterInterface();
    }

    public void drawFilterInterface(){

        dataSource = new ItemDataSource();
        dataSource.open();

        ImageButton closeFilterButton = (ImageButton)findViewById(R.id.button_close_filter);
        closeFilterButton.setVisibility(View.VISIBLE);


        List<Item> items = dataSource.filterItems(ownID, values);


        LayoutInflater inflater = LayoutInflater.from(this);

        LinearLayout inside = (LinearLayout)findViewById(R.id.list_item);
        inside.removeAllViews();

        if(items.size() != 0){

            for(Item it : items){

                View itemView = inflater.inflate(R.layout.item_in_inventory,null, false);
                itemView.setClickable(true);

                TextView outName = (TextView) itemView.findViewById(R.id.nameView);
                String name = it.getName();
                if(name.length() > 10 ){
                    name = it.getName().substring(0,7);
                    name = name.concat("...");
                }
                outName.setText(name);


                TextView outOwner = (TextView) itemView.findViewById(R.id.ownerView);
                String ownerID = Integer.toString(it.getOwnerID());
                outOwner.setText(ownerID);


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

                TextView outBrand = (TextView) itemView.findViewById(R.id.brandView);
                String brand = it.getBrand();
                System.out.print(brand.length());
                if(brand.length() > 10 ){
                    brand = it.getBrand().substring(0,7);
                    brand = brand.concat("...");
                }
                outBrand.setText(brand);

                TextView outModel = (TextView) itemView.findViewById(R.id.modelView);
                String model = it.getModel();
                if(model.length() > 10 ){
                    model = it.getModel().substring(0,7);
                    model = model.concat("...");
                }
                outModel.setText(model);

                final int id1 = it.getId();

                inside.addView(itemView);

                ImageButton trashButton = (ImageButton)itemView.findViewById(R.id.imageButtonDelete);

                trashButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteInterface(id1);

                    }
                });



                itemView.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(getApplicationContext(), "Item has been deleted", Toast.LENGTH_LONG).show();
                        drawFilterInterface();

                    }

                })
                .show();

    }

    private void filterItemInterface(){

        LayoutInflater inflater = this.getLayoutInflater();
        view2 = inflater.inflate(R.layout.dialog_search_item, null);
        new AlertDialog.Builder(this)
                .setTitle("Edit Item")
                .setView(view2)

                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        final EditText nameEdit = (EditText)view2.findViewById(R.id.enterFilterName);
                        final EditText roomEdit = (EditText)view2.findViewById(R.id.enterFilterRoom);
                        final EditText brandEdit = (EditText)view2.findViewById(R.id.enterFilterBrand);
                        final EditText modelEdit = (EditText)view2.findViewById(R.id.enterFilterModel);
                        final EditText commentEdit = (EditText)view2.findViewById(R.id.enterFilterComment);

                        ContentValues updateItem = new ContentValues();

                        CheckBox checkFilterName = (CheckBox)view2.findViewById(R.id.checkFilterName);

                        System.out.print(checkFilterName.isChecked());

                        boolean itemHasUpdated = false;

                        if(checkFilterName.isChecked()){
                            String editedName = nameEdit.getText().toString();
                            if(editedName.trim().length() == 0){
                                Toast.makeText(getApplicationContext(),"Please insert a value to edit in Name",Toast.LENGTH_SHORT).show();
                            }else{
                                updateItem.put(MySQLiteHelper.NAME,editedName);
                                itemHasUpdated = true;

                            }

                        }

                        CheckBox checkFilterRoom = (CheckBox)view2.findViewById(R.id.checkFilterRoom);

                        if(checkFilterRoom.isChecked()){
                            String editedRoom = roomEdit.getText().toString();
                            if(editedRoom.trim().length() == 0){
                                Toast.makeText(getApplicationContext(),"Please insert a value to edit in Room",Toast.LENGTH_SHORT).show();
                            }else{
                                updateItem.put(MySQLiteHelper.ROOM,editedRoom);
                                itemHasUpdated = true;

                            }
                        }

                        CheckBox checkFilterModel = (CheckBox)view2.findViewById(R.id.checkFilterModel);

                        if(checkFilterModel.isChecked()){
                            String editedModel = modelEdit.getText().toString();
                            if(editedModel.trim().length() == 0){
                                Toast.makeText(getApplicationContext(),"Please insert a value to edit in Model",Toast.LENGTH_SHORT).show();
                            }else{
                                updateItem.put(MySQLiteHelper.MODEL,editedModel);
                                itemHasUpdated = true;
                            }
                        }

                        CheckBox checkFilterBrand = (CheckBox)view2.findViewById(R.id.checkFilterBrand);

                        if(checkFilterBrand.isChecked()){
                            String editedBrand = brandEdit.getText().toString();
                            if(editedBrand.trim().length() == 0){
                                Toast.makeText(getApplicationContext(),"Please insert a value to edit in Brand",Toast.LENGTH_SHORT).show();
                            }else{
                                updateItem.put(MySQLiteHelper.BRAND,editedBrand);
                                itemHasUpdated = true;

                            }
                        }

                        CheckBox checkFilterComment = (CheckBox)view2.findViewById(R.id.checkFilterComment);

                        if(checkFilterComment.isChecked()){
                            String editedComment = commentEdit.getText().toString();
                            if(editedComment.trim().length() == 0){
                                Toast.makeText(getApplicationContext(),"Please insert a value to edit in Comment",Toast.LENGTH_SHORT).show();
                            }else{
                                updateItem.put(MySQLiteHelper.COMMENT,editedComment);
                                itemHasUpdated = true;

                            }
                        }

                        if(itemHasUpdated){

                            values.clear();
                            values = updateItem;
                            drawFilterInterface();
                           


                        }else{
                            Toast.makeText(getApplicationContext(),"Nothing has been updated, Please use checkFilter box to select fields.",Toast.LENGTH_LONG).show();
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

    public void onClick(final View view){

        switch (view.getId()) {
            case R.id.button_search:

                filterItemInterface();

                break;

        }

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
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }


}