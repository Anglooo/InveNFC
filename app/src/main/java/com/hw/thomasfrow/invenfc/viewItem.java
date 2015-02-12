package com.hw.thomasfrow.invenfc;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.widget.TextView;



public class viewItem extends ActionBarActivity {

    private ItemDataSource datasource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        int id = getIntent().getExtras().getInt("id");
        System.out.println(id);

        datasource = new ItemDataSource(this);
        datasource.open();

        Item item = datasource.getItemByID(id);

        TextView nameView = new TextView(this);
        nameView = (TextView)findViewById(R.id.enterName);
        nameView.append(item.getName());



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
