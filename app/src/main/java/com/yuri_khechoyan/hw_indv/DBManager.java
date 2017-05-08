package com.yuri_khechoyan.hw_indv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static com.yuri_khechoyan.hw_indv.DatabaseHelper.POSINLINE;
import static com.yuri_khechoyan.hw_indv.DatabaseHelper.TABLE_NAME;
import static com.yuri_khechoyan.hw_indv.DatabaseHelper._ID;

public class DBManager {
    //Creates DatabaseHelper object
    private DatabaseHelper dbHelper;
    //Initializes context for the DBManager
    private Context context;
    //Initializes SQLite database
    private SQLiteDatabase database;

    //Creates DBManager constructor
    public DBManager(Context c) {
        //Creates context for the DBManager
        context = c;
    }

    //Open method for DBManager - ModifyCustomerActivity
    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }


    //Method used to insert information ito the Database
    public long insert(String name, String phonenum) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.PHONENUM, phonenum);
        //Identifies amount of entries in Database
        long numOfRecords = DatabaseUtils.queryNumEntries(database, "CUSTOMERS;");
        //Increments the position in line counter by 1
        long position = numOfRecords+1;
        contentValue.put(DatabaseHelper.POSINLINE, position);
        database.insert(TABLE_NAME, null, contentValue);
        database.close();
        //Returns position value: Method not void
        return position;
    }

    //Method used to fetch information (previously entered current entries)
    public Cursor fetch() {
        //Creates new instance of columns in DB
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.NAME, DatabaseHelper.PHONENUM,
                DatabaseHelper.POSINLINE};
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);

        //Verify that cursor is not null - if not move cursor to the beginning
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    //Method used to update user info in ListView - ModifyCustomerActivity
    public int update(long _id, String name, String phonenum) {
        //Updates Values in Database when info is modified by user
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME, name);
        contentValues.put(DatabaseHelper.PHONENUM, phonenum);
        int i = database.update(TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        database.close();
        return i;
    }

    //Method called if customer will be deleted from the DB & ListView - ModifyCustomerActivity
    public void delete(long _id) {

        //Delete entire row: customer is removed
        database.delete(TABLE_NAME, DatabaseHelper._ID + " = " + _id, null);

        //Update positioning number (Right side): Position number is ID (Primary Key) -1
        database.execSQL("UPDATE " +TABLE_NAME +" SET " +POSINLINE +" = " +POSINLINE +"-1"
                +" WHERE " +_ID +" > " +_id);

        database.close();
    }
}