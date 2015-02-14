package com.hw.thomasfrow.invenfc;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.widget.TextView;



public class viewItem extends ActionBarActivity {

    private ItemDataSource dataSource;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        int id = getIntent().getExtras().getInt("id");

        dataSource = new ItemDataSource();
        dataSource.open();

        System.out.print(id);


        Item item = dataSource.getItemByID(id);

        TextView editView;

        editView = (TextView)findViewById(R.id.outIDView);
        editView.setText(Integer.toString(item.getId()));

        editView = (TextView)findViewById(R.id.outNameView);
        editView.setText(item.getName());

        editView = (TextView)findViewById(R.id.outBrandView);
        editView.setText(item.getBrand());

        editView = (TextView)findViewById(R.id.outModelView);
        editView.setText(item.getModel());

        editView = (TextView)findViewById(R.id.outRoomView);
        editView.setText(item.getRoom());

        editView = (TextView)findViewById(R.id.outCommentView);
        editView.setText(item.getComment());


        dataSource.close();

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
