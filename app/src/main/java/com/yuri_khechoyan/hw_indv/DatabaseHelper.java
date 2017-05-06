package com.yuri_khechoyan.hw_indv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "CUSTOMERS";

    // Table columns
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String PHONENUM = "phonenumber";
    public static String POSINLINE = "position";

    // Database Information
    static final String DB_NAME = "CUSTOMERS.DB";

    // database version
    static final int DB_VERSION = 2;

    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY, " + NAME + " TEXT NOT NULL, " + PHONENUM + " TEXT);";
            /*+POSINLINE + "INTEGER*/

    //Creates Constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    //Method for updating DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
