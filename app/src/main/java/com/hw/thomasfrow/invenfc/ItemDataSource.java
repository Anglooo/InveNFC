package com.hw.thomasfrow.invenfc;

/**
 * Created by Thomas on 10/02/15.
 */
        import java.util.ArrayList;
        import java.util.List;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.util.Log;

public class ItemDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.ID,
            MySQLiteHelper.OWNERID,
            MySQLiteHelper.NAME,
            MySQLiteHelper.ROOM,
            MySQLiteHelper.BRAND,
            MySQLiteHelper.MODEL,
            MySQLiteHelper.COMMENT,
            MySQLiteHelper.TAG,
            MySQLiteHelper.PHOTO};

    public ItemDataSource() {
        dbHelper = new MySQLiteHelper(invenfc.getAppContext());
    }
    
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Item createItem(String ownerID, String name, String room, String brand, String model, String comment ) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.OWNERID, ownerID);
        values.put(MySQLiteHelper.NAME, name);
        values.put(MySQLiteHelper.ROOM, room);
        values.put(MySQLiteHelper.BRAND, brand);
        values.put(MySQLiteHelper.MODEL, model);
        values.put(MySQLiteHelper.COMMENT, comment);
        values.put(MySQLiteHelper.TAG,0);
        values.put(MySQLiteHelper.PHOTO,0);


        long insertId = database.insert(MySQLiteHelper.DATABASE_TABLE, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.DATABASE_TABLE,
                allColumns, MySQLiteHelper.ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Item newItem = cursorToItem(cursor);
        cursor.close();
        return newItem;
    }

    public void deleteItem(Item item) {
        long id = item.getId();
        database.delete(MySQLiteHelper.DATABASE_TABLE, MySQLiteHelper.ID
                + " = " + id, null);
    }

    public void deleteItemByID(int id) {
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

    public List<Item> filterItems(String ownerID, ContentValues content) {
        List<Item> items = new ArrayList<Item>();

        String query = ("SELECT * FROM Items WHERE ownerID = '" + ownerID + "'");
        String newQuery = "";

        for (String key : content.keySet()) {
            String myKey = key;
            String myValue = content.get(key).toString();
            newQuery = query.concat(" AND " +  myKey + " = '" + myValue + "'");
        }

        Cursor cursor = database.rawQuery(newQuery, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item item = cursorToItem(cursor);
            items.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        String queryLike2 = ("SELECT * FROM Items WHERE ownerID = '" + ownerID + "'");

        for (String key : content.keySet()) {
            String myKey = key;
            String myValue = content.get(key).toString();
            queryLike2 = query.concat(" AND " +  myKey + " LIKE '%" + myValue + "%'");
        }

        cursor = database.rawQuery(queryLike2, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item item = cursorToItem(cursor);

            if(!isContainedIn(item, items)){
                items.add(item);
            }
            cursor.moveToNext();
        }
        cursor.close();

        Log.i("FILTER", queryLike2);

        String queryLike1 = ("SELECT * FROM Items WHERE ownerID = '" + ownerID + "'");

        for (String key : content.keySet()) {
            String myKey = key;
            String myValue = content.get(key).toString();
            queryLike1 = query.concat(" AND " +  myKey + " LIKE '%" + myValue + "'");
        }

        cursor = database.rawQuery(queryLike1, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item item = cursorToItem(cursor);

            if(!isContainedIn(item, items)){
                items.add(item);
            }
            cursor.moveToNext();
        }
        cursor.close();

        Log.i("FILTER", queryLike1);


        
        
        return items;
    }
    
    public boolean isContainedIn(Item item, List<Item> items){
        
        int max = items.size();
        int i = 0;
        while(i < max ){
            if (items.get(i).getId() == item.getId()){
                return true;
            }
            i++;
        }
        return false;
        
        
    }

    public List<Item> getItemsByOwner(String ownerID) {
        List<Item> items = new ArrayList<Item>();

        Cursor cursor = database.rawQuery("SELECT * FROM Items WHERE ownerID = '"+ ownerID+ "'", null);

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

        if(query == null){
            Log.i("cursor","returned null");
            return null;
        }
        query.moveToFirst();
        Item item =  cursorToItem(query);
        query.close();
        return item;

    }

    private Item cursorToItem(Cursor cursor) {
        Item item = new Item();

        //Log.i("cursor",Integer.toString(cursor.getColumnCount()));

        Log.i("cursor",Integer.toString(cursor.getCount()));

        if(cursor.getCount() == 0){
            return null;
        }

        item.setId(cursor.getInt(0));
        item.setOwnerID(cursor.getString(1));
        item.setName(cursor.getString(2));
        item.setRoom(cursor.getString(3));
        item.setBrand(cursor.getString(4));
        item.setModel(cursor.getString(5));
        item.setComment(cursor.getString(6));
        item.setTag(cursor.getInt(7));

        return item;
    }

    public int updateItem(int ID, ContentValues content){

        int success = database.update(MySQLiteHelper.DATABASE_TABLE, content, MySQLiteHelper.ID+"="+ID, null);

        return success;
    }

}

