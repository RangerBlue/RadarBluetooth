package com.example.kamil.br.database.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.kamil.br.activities.MainActivity;
import com.example.kamil.br.database.DBHandler;
import com.example.kamil.br.database.model.BluetoothResults;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil on 2016-09-21.
 * Można wywalić ustawianie czasu gdy rekord nie jest pusty
 */
public class BluetoothResultsController {

    private final static String TAG = BluetoothResultsController.class.getSimpleName();

    public static String createTable() {
        return "CREATE TABLE " + BluetoothResults.TABLE + "(" +
                BluetoothResults.ID_BLUETOOTHRESULTS + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BluetoothResults.NAME + " TEXT," +
                BluetoothResults.ADDRESS + " TEXT," +
                BluetoothResults.RSSI + " INTEGER," +
                BluetoothResults.TIME + " TEXT," +
                BluetoothResults.EGDENUMBER + " INTEGER," +
                BluetoothResults.ID_MEASUREMENTS + " INTEGER, "+
                BluetoothResults.ID_ROOMS + " INTEGER )";
    }

    public void insert(BluetoothResults bluetoothResult, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BluetoothResults.NAME, bluetoothResult.getName());
        values.put(BluetoothResults.ADDRESS, bluetoothResult.getName());
        values.put(BluetoothResults.RSSI, bluetoothResult.getRssi());
        values.put(BluetoothResults.TIME, bluetoothResult.getTime());
        values.put(BluetoothResults.EGDENUMBER, bluetoothResult.getEdgeNumber());
        values.put(BluetoothResults.ID_MEASUREMENTS, bluetoothResult.getIdMeasurements());
        values.put(BluetoothResults.ID_ROOMS, bluetoothResult.getIdRooms());
        Log.d("w insercie", String.valueOf(bluetoothResult.getIdRooms()));
        db.insert(BluetoothResults.TABLE, null, values);
        db.close(); // Closing database connection
    }

    public List<BluetoothResults> selectAll(Context context)
    {
        List<BluetoothResults> bluetoothResult = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "BluetoothResults." + BluetoothResults.ID_BLUETOOTHRESULTS +
                        ", BluetoothResults." + BluetoothResults.NAME +
                        ", BluetoothResults." + BluetoothResults.ADDRESS +
                        ", BluetoothResults." + BluetoothResults.RSSI +
                        ", BluetoothResults." + BluetoothResults.TIME +
                        ", BluetoothResults." + BluetoothResults.EGDENUMBER +
                        ", BluetoothResults." + BluetoothResults.ID_MEASUREMENTS +
                        ", BluetoothResults." + BluetoothResults.ID_ROOMS +
                        " FROM " + BluetoothResults.TABLE ;

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                BluetoothResults result = new BluetoothResults();
                result.setIdBluetoothResults(cursor.getInt(cursor.getColumnIndex(BluetoothResults.ID_BLUETOOTHRESULTS)));
                result.setName(cursor.getString(cursor.getColumnIndex(BluetoothResults.NAME)));
                result.setAddress(cursor.getString(cursor.getColumnIndex(BluetoothResults.ADDRESS)));
                result.setRssi(cursor.getInt(cursor.getColumnIndex(BluetoothResults.RSSI)));
                result.setTime(cursor.getLong(cursor.getColumnIndex(BluetoothResults.TIME)));
                result.setEdgeNumber(cursor.getInt(cursor.getColumnIndex(BluetoothResults.EGDENUMBER)));
                result.setIdMeasurements(cursor.getInt(cursor.getColumnIndex(BluetoothResults.ID_MEASUREMENTS)));
                result.setIdMeasurements(cursor.getInt(cursor.getColumnIndex(BluetoothResults.ID_ROOMS)));
                bluetoothResult.add(result);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bluetoothResult;
    }

    public List<BluetoothResults> selectBluetoothResultsWhereIdRoomsAndIdMeasurements(Context context, int idRooms, int idMeasurement)
    {
        List<BluetoothResults> bluetoothResult = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "BluetoothResults." + BluetoothResults.ID_BLUETOOTHRESULTS +
                        ", BluetoothResults." + BluetoothResults.NAME +
                        ", BluetoothResults." + BluetoothResults.ADDRESS +
                        ", BluetoothResults." + BluetoothResults.RSSI +
                        ", BluetoothResults." + BluetoothResults.TIME +
                        ", BluetoothResults." + BluetoothResults.EGDENUMBER +
                        ", BluetoothResults." + BluetoothResults.ID_MEASUREMENTS +
                        ", BluetoothResults." + BluetoothResults.ID_ROOMS +
                        " FROM " + BluetoothResults.TABLE +
                        " WHERE BluetoothResults." + BluetoothResults.ID_ROOMS+"="+Integer.toString(idRooms)+
                        " AND BluetoothResults." + BluetoothResults.ID_MEASUREMENTS+"="+Integer.toString(idMeasurement);

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                BluetoothResults result = new BluetoothResults();
                result.setIdBluetoothResults(cursor.getInt(cursor.getColumnIndex(BluetoothResults.ID_BLUETOOTHRESULTS)));
                result.setName(cursor.getString(cursor.getColumnIndex(BluetoothResults.NAME)));
                result.setAddress(cursor.getString(cursor.getColumnIndex(BluetoothResults.ADDRESS)));
                result.setRssi(cursor.getInt(cursor.getColumnIndex(BluetoothResults.RSSI)));
                result.setTime(cursor.getLong(cursor.getColumnIndex(BluetoothResults.TIME)));
                result.setEdgeNumber(cursor.getInt(cursor.getColumnIndex(BluetoothResults.EGDENUMBER)));
                result.setIdMeasurements(cursor.getInt(cursor.getColumnIndex(BluetoothResults.ID_MEASUREMENTS)));
                result.setIdRooms(cursor.getInt(cursor.getColumnIndex(BluetoothResults.ID_ROOMS)));
                bluetoothResult.add(result);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bluetoothResult;
    }

    public ArrayList<String> selectNameDistinct(Context context, int idRooms, int idMeasurement)
    {
        ArrayList<String> results = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT DISTINCT " +
                        "BluetoothResults." + BluetoothResults.ADDRESS +
                        " FROM " + BluetoothResults.TABLE+
                        " WHERE BluetoothResults." + BluetoothResults.ID_ROOMS+"="+Integer.toString(idRooms)+
                        " AND BluetoothResults." + BluetoothResults.ID_MEASUREMENTS+"="+Integer.toString(idMeasurement);;

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String result = cursor.getString(cursor.getColumnIndex(BluetoothResults.ADDRESS));
                results.add(result);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return results;
    }

    /**
     * Usuwa pomiary danego pokoju
     * @param idRoom id pokoju do usunięcia
     * @param context kontekst aplikacji
     */
    public static void deleteWhereIdRooms(int idRoom, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String delete =
                " DELETE FROM " + BluetoothResults.TABLE +
                        " WHERE BluetoothResults." + BluetoothResults.ID_ROOMS+"="+Integer.toString(idRoom);
        db.execSQL(delete);
        Log.d(TAG, "deleted record in bluetoothResults");
        db.close(); // Closing database connection
    }

    /**
     * Usuwa pomiary mapowania
     * @param idMeasurement id pokoju do usunięcia
     * @param context kontekst aplikacji
     */
    public static void deleteWhereIdMeasurement(int idMeasurement, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String delete =
                " DELETE FROM " + BluetoothResults.TABLE +
                        " WHERE BluetoothResults." + BluetoothResults.ID_MEASUREMENTS+"="+Integer.toString(idMeasurement);
        db.execSQL(delete);
        Log.d(TAG, "deleted record in bluetoothResults");
        db.close(); // Closing database connection
    }

    /**
     * Usuwa pomiary mapowania
     * @param idbtResult id do usunięcia
     * @param context kontekst aplikacji
     */
    public static void deleteWhereId(int idbtResult, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String delete =
                " DELETE FROM " + BluetoothResults.TABLE +
                        " WHERE BluetoothResults." + BluetoothResults.ID_BLUETOOTHRESULTS+"="+Integer.toString(idbtResult);
        db.execSQL(delete);
        Log.d(TAG, "deleted record in bluetoothResults");
        db.close(); // Closing database connection
    }

    //debug only
    public static void printAllTableToLog(ArrayList<BluetoothResults> list)
    {

        DecimalFormat format = new DecimalFormat();
        format.setMinimumFractionDigits(2);
        for ( BluetoothResults element : list )
        {
            Log.d("Table BResults: ",
                            "\t|ID| "+ element.getIdBluetoothResults() +
                            "\t|Name| "+ element.getName() +
                            "\t|Address| "+ element.getAddress() +
                            "\t|RSSI| "+ element.getRssi() +
                            "\t|Time| "+ element.getTime() +
                            "\t|EdgeNumber| "+ element.getEdgeNumber() +
                            "\t|IdMeasurements| "+ element.getIdMeasurements() +
                            "\t|IdRooms| "+ element.getIdRooms()
            ) ;
        }
    }
}
