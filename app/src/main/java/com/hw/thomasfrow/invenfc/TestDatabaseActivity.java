package com.hw.thomasfrow.invenfc;

import java.util.List;
import java.util.Random;

import android.content.DialogInterface;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.view.LayoutInflater;


public class TestDatabaseActivity extends ListActivity {
    private ItemDataSource datasource;
    private View view2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_database);

        datasource = new ItemDataSource();
        datasource.open();

        List<Item> values = datasource.getAllItems();

        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this,
                android.R.layout.simple_list_item_1, values);

        setListAdapter(adapter);

    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(final View view) {
        @SuppressWarnings("unchecked") final
        ArrayAdapter<Item> adapter = (ArrayAdapter<Item>) getListAdapter();
        Item item = null;
        switch (view.getId()) {
            case R.id.add:

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

                                Item item = datasource.createItem(owner,nameEdit.getText().toString(),roomEdit.getText().toString(),brandEdit.getText().toString(),modelEdit.getText().toString(),commentEdit.getText().toString());
                                adapter.add(item);
                            }
                        })

                        .setCancelable(true)

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        })
                        .show();

                break;

            case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    item = (Item) getListAdapter().getItem(0);
                    datasource.deleteItem(item);
                    adapter.remove(item);
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

