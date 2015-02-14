package com.hw.thomasfrow.invenfc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.List;


public class showInventoryActivity extends ActionBarActivity {

    private ItemDataSource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_inventory);

        dataSource = new ItemDataSource();
        dataSource.open();

        //List<Item> items = dataSource.getAllItems();
        List<Item> items = dataSource.getItemsByOwner(0);


        LayoutInflater inflater = LayoutInflater.from(this);

        LinearLayout inside = (LinearLayout)findViewById(R.id.list_item);

        for(Item it : items){

            View itemView = inflater.inflate(R.layout.item_in_inventory,null, false);
            itemView.setClickable(true);

            TextView outName = (TextView) itemView.findViewById(R.id.nameView);
            outName.setText(it.getName());
            System.out.println(it.toString());
            TextView outOwner = (TextView) itemView.findViewById(R.id.ownerView);
            outOwner.setText(Integer.toString(it.getOwnerID()));
            TextView outID = (TextView) itemView.findViewById(R.id.idView);
            outID.setText(Integer.toString(it.getId()));
            TextView outRoom = (TextView) itemView.findViewById(R.id.roomView);
            outRoom.setText(it.getRoom());
            TextView outBrand = (TextView) itemView.findViewById(R.id.brandView);
            outBrand.setText(it.getBrand());
            TextView outModel = (TextView) itemView.findViewById(R.id.modelView);
            outModel.setText(it.getModel());

            final int id1 = it.getId();

            inside.addView(itemView);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), viewItem.class);
                    intent.putExtra("id", id1);
                    startActivity(intent);
                }
            });

        }

        dataSource.close();



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
}
