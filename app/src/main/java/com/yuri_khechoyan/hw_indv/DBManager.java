package com.yuri_khechoyan.hw_indv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static com.yuri_khechoyan.hw_indv.DatabaseHelper.TABLE_NAME;

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

    //Method to close DB
    //public void close() {
    //   dbHelper.close();
    //}

    //Method used to insert information ito the Database
    public void insert(String name, String phonenum) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.PHONENUM, phonenum);
        database.insert(TABLE_NAME, null, contentValue);
        database.close();
    }

    //Method used to fetch information (previously entered current entries)
    public Cursor fetch() {
        //Creates new instance of columns in DB
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.NAME, DatabaseHelper.PHONENUM,
                DatabaseHelper.PHONENUM };
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);

        //Verify that cursor is not null - if not move cursor to the beginning
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    //Method used to update user info in ListView - ModifyCustomerActivity
    public int update(long _id, String name, String phonenum) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME, name);
        contentValues.put(DatabaseHelper.PHONENUM, phonenum);
        int i = database.update(TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);


        database.close();
        return i;



        //getRowFromId(context, Id);

    }

    /*
    public static long getRowFromId(Context context, int Id) {
        DatabaseHelper dataBase = new DatabaseHelper(context);
        SQLiteDatabase conn = dataBase.getWritableDatabase();
        String query = "SELECT COUNT (*) FROM " + dataBase.TABLE_NAME + " WHERE "
                + DatabaseHelper._ID + " <= " + Id;

        "SELECT ROWNUMBER(id, 4) FROM myTable WHERE aCondition == 1 ORDER BY id

        return conn.compileStatement(query).simpleQueryForLong();
    }

    */

    //Method called if customer will be deleted from the DB & ListView - ModifyCustomerActivity
    public void delete(long _id) {
        database.delete(TABLE_NAME, DatabaseHelper._ID + " = " + _id, null);

        database.close();


    }

}