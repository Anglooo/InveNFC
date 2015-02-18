package com.hw.thomasfrow.invenfc;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import android.support.v4.widget.DrawerLayout;

import java.util.List;


public class showInventoryActivity extends Activity {

    private ItemDataSource dataSource;
    private View view2;

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    //private ListView mDrawerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_inventory);

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
                }

                return false;
            }


        });

        //toolbar.setNavigationIcon(R.mipmap.navigation_black_icon);
        toolbar.setNavigationIcon(R.drawable.ic_action_name);

       /* mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());*/


    }


    public void drawInterface(){
        dataSource = new ItemDataSource();
        dataSource.open();


        List<Item> items = dataSource.getItemsByOwner(0);

        System.out.println(items.toString());
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
                System.out.println("the numer is: ");
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
        @SuppressWarnings("unchecked") final
        Item item = null;
        switch (view.getId()) {
            case R.id.fabAdd:

                addItemInterface();

                break;

        }
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

                        System.out.println("a");
                        EditText nameEdit = (EditText)view2.findViewById(R.id.enterName);
                        EditText roomEdit = (EditText)view2.findViewById(R.id.enterRoom);
                        EditText brandEdit = (EditText)view2.findViewById(R.id.enterBrand);
                        EditText modelEdit = (EditText)view2.findViewById(R.id.enterModel);
                        EditText commentEdit = (EditText)view2.findViewById(R.id.enterComment);

                        int owner = 0;

                        dataSource.createItem(owner, nameEdit.getText().toString(), roomEdit.getText().toString(), brandEdit.getText().toString(), modelEdit.getText().toString(), commentEdit.getText().toString());
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

    }
}
