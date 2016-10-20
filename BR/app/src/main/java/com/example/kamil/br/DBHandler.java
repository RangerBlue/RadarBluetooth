package com.example.kamil.br;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 05.05.16.
 */
public class DBHandler extends SQLiteOpenHelper {

    // wersja bazy
    private static final int DATABASE_VERSION = 1;

    // nazwa bazy
    private static final String DATABASE_NAME = "dataDB_8";

    // nazwa tabeli
    public final String TABLE_DB = "tableDB_8";

    // nazwy kolumn w tabeli
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String RSSI = "rssi";
    public static final String TIME = "time";
    public static final String DIRECTION = "direction";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DB_TABLE = "CREATE TABLE " + TABLE_DB+ "("
                + ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"
                + RSSI + " TEXT," + TIME + " TEXT," + DIRECTION + " TEXT" + ")";
        Log.d("Tworzenie bazy: ", "stworzono");
        db.execSQL(CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DB);
        // Creating tables again
        onCreate(db);
    }

    public void insert(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, record.getName());
        values.put(RSSI, record.getRssi());
        values.put(TIME, record.getTime());
        values.put(DIRECTION, record.getDirection());
        db.insert(TABLE_DB, null, values);
        db.close(); // Closing database connection
    }

    // Getting one record

    public Record select(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DB, new String[]{ID,
                        NAME, RSSI, TIME, DIRECTION}, ID+ "=?",
                new String[]{String.valueOf(id)}, null, null, null,null); //moze jeszcze jeden null
        if (cursor != null)
            cursor.moveToFirst();

        Record element = new Record(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)),Long.parseLong(cursor.getString(3)),Integer.parseInt(cursor.getString(4)));

        return element;
    }

    public List<Record> getAll() {
        List<Record> recordsList = new ArrayList<Record>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DB;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Record element = new Record();
                element.setId(Integer.parseInt(cursor.getString(0)));
                element.setName(cursor.getString(1));
                element.setRssi(Integer.parseInt(cursor.getString(2)));
                element.setTime(Long.parseLong(cursor.getString(3)));
                element.setDirection(Integer.parseInt(cursor.getString(4)));
                // Adding contact to list
                recordsList.add(element);
            } while (cursor.moveToNext());
        }

        // return contact list
        return recordsList;
    }


    // Getting Count
    public int selectCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DB;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    // Updating a
    public int update(Record element) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, element.getName());
        values.put(RSSI, element.getRssi());
        values.put(TIME, element.getTime());
        values.put(DIRECTION, element.getDirection());

        // updating row
        return db.update(TABLE_DB, values, ID + " = ?",
                new String[]{String.valueOf(element.getId())});
    }


    // Deleting


    public void delete(Record element) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DB, ID + " = ?",
                new String[] { String.valueOf(element.getId()) });
        db.close();
    }

}