package com.hw.thomasfrow.invenfc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "InvenfcDatabase";
    public static final int DATABASE_VERSION = 1;


    public static final String DATABASE_TABLE = "Items";

    public static final String ID = "id";
    public static final String OWNERID = "ownerID";
    public static final String NAME = "name";
    public static final String ROOM = "room";
    public static final String BRAND = "brand";
    public static final String MODEL = "model";
    public static final String COMMENT = "comment";
    public static final String TAG = "tag";
    public static final String PHOTO = "photo";



    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + DATABASE_TABLE + " ("
            + ID + " integer primary key autoincrement, "
            + OWNERID + " text not null, "
            + NAME + " text not null,"
            + ROOM + " text not null,"
            + BRAND + " text not null,"
            + MODEL + " text not null,"
            + COMMENT + " text not null,"
            + TAG + " integer not null,"
            + PHOTO + " integer not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

}