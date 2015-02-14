package com.hw.thomasfrow.invenfc;

/**
 * Created by thomas on 10/02/15.
 */
        import java.util.ArrayList;
        import java.util.List;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;

public class ItemDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.ID,
            MySQLiteHelper.OWNERID,
            MySQLiteHelper.NAME,
            MySQLiteHelper.ROOM,
            MySQLiteHelper.MODEL,
            MySQLiteHelper.BRAND,
            MySQLiteHelper.COMMENT };

    public ItemDataSource() {
        dbHelper = new MySQLiteHelper(invenfc.getAppContext());
    }
    
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Item createItem(int ownerID, String name, String room, String brand, String model, String comment ) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.OWNERID, ownerID);
        values.put(MySQLiteHelper.NAME, name);
        values.put(MySQLiteHelper.ROOM, room);
        values.put(MySQLiteHelper.BRAND, brand);
        values.put(MySQLiteHelper.MODEL, model);
        values.put(MySQLiteHelper.COMMENT, comment);


        long insertId = database.insert(MySQLiteHelper.DATABASE_TABLE, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.DATABASE_TABLE,
                allColumns, MySQLiteHelper.ID + " = " + insertId, null,
                null, null, null);
        System.out.println(values.toString());
        cursor.moveToFirst();
        Item newItem = cursorToItem(cursor);
        cursor.close();
        return newItem;
    }

    public void deleteItem(Item item) {
        long id = item.getId();
        System.out.println("Item deleted with id: " + id);
        database.delete(MySQLiteHelper.DATABASE_TABLE, MySQLiteHelper.ID
                + " = " + id, null);
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>();

        Cursor cursor = database.query(MySQLiteHelper.DATABASE_TABLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item item = cursorToItem(cursor);
            items.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return items;
    }

    public List<Item> getItemsByOwner(int ownerID) {
        List<Item> items = new ArrayList<Item>();

        Cursor cursor = database.query(MySQLiteHelper.DATABASE_TABLE,
                allColumns, null, null, null, null, null);
        Cursor query = database.rawQuery("SELECT * FROM Items WHERE ownerID = "+ ownerID, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item item = cursorToItem(cursor);
            items.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return items;
    }

    public Item getItemByID(int id){
        Cursor query = database.rawQuery("SELECT * FROM Items WHERE id = "+id, null);

        System.out.println(query.toString());
        query.moveToFirst();
        Item item =  cursorToItem(query);
        query.close();
        return item;

    }

    private Item cursorToItem(Cursor cursor) {
        Item item = new Item();

        item.setId(cursor.getInt(0));
        item.setOwnerID(cursor.getInt(1));
        item.setName(cursor.getString(2));
        item.setRoom(cursor.getString(3));
        item.setBrand(cursor.getString(4));
        item.setModel(cursor.getString(5));
        item.setComment(cursor.getString(6));

        return item;
    }


}

