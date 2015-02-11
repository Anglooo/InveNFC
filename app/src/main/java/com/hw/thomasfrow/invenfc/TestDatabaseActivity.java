package com.hw.thomasfrow.invenfc;

import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class TestDatabaseActivity extends ListActivity {
    private ItemDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        datasource = new ItemDataSource(this);
        datasource.open();

        List<Item> values = datasource.getAllItems();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Item> adapter = (ArrayAdapter<Item>) getListAdapter();
        Item comment = null;
        switch (view.getId()) {
            case R.id.add:
                String[] comments = new String[]{"Cool", "Very nice", "Hate it"};
                int nextInt = new Random().nextInt(3);
                // save the new comment to the database
                comment = datasource.createItem(comments[nextInt]);
                adapter.add(comment);
                break;
            case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    comment = (Item) getListAdapter().getItem(0);
                    datasource.deleteItem(comment);
                    adapter.remove(comment);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

} 

