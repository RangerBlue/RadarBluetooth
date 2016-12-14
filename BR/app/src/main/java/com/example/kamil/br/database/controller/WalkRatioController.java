package com.example.kamil.br.database.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.kamil.br.database.DBHandler;
import com.example.kamil.br.database.model.WalkRatio;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil on 2016-12-14.
 */

public class WalkRatioController {

    private final String TAG = RoomsController.class.getSimpleName();

    public static String createTable() {
        return "CREATE TABLE " + WalkRatio.TABLE + "(" +
                WalkRatio.ID_WALKRATIO + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WalkRatio.VALUE + " REAL," +
                WalkRatio.ID_ROOMS + " INTEGER )";
    }

    public void insert(WalkRatio walkRatio, Context context)
    {
        SQLiteDatabase db =new DBHandler(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WalkRatio.VALUE, walkRatio.getValue());
        values.put(WalkRatio.ID_ROOMS, walkRatio.getIdRooms());
        db.insert(WalkRatio.TABLE, null, values);
        db.close(); // Closing database connection
    }

    public void deleteWhereId(int id, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String delete =
                " DELETE FROM " + WalkRatio.TABLE +
                        " WHERE WalkRatio." + WalkRatio.ID_WALKRATIO+"="+Integer.toString(id);
        db.execSQL(delete);
        Log.d(TAG, "deleted record from walkratio");
        db.close(); // Closing database connection
    }

    public static WalkRatio selectWalkRatioWherePathDataId(Context context, int idRooms)
    {
        List<WalkRatio> walkRatio = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "WalkRatio." + WalkRatio.ID_WALKRATIO +
                        ", WalkRatio." + WalkRatio.VALUE +
                        ", WalkRatio." + WalkRatio.ID_ROOMS +
                        " FROM " + WalkRatio.TABLE +
                        " WHERE WalkRatio." + WalkRatio.ID_ROOMS+"="+Integer.toString(idRooms);

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                WalkRatio walkRatio_= new WalkRatio();
                walkRatio_.setIdWalkRatio(cursor.getInt(cursor.getColumnIndex(WalkRatio.ID_WALKRATIO)));
                walkRatio_.setValue(cursor.getFloat(cursor.getColumnIndex(WalkRatio.VALUE)));
                walkRatio_.setIdRooms(cursor.getInt(cursor.getColumnIndex(WalkRatio.ID_ROOMS)));
                walkRatio.add(walkRatio_);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return walkRatio.get(0);
    }

    public static List<WalkRatio> selectAll(Context context)
    {
        List<WalkRatio> walkRatio = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "WalkRatio." + WalkRatio.ID_WALKRATIO +
                        ", WalkRatio." + WalkRatio.VALUE +
                        ", WalkRatio." + WalkRatio.ID_ROOMS +
                        " FROM " + WalkRatio.TABLE;

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                WalkRatio walkRatio_= new WalkRatio();
                walkRatio_.setIdWalkRatio(cursor.getInt(cursor.getColumnIndex(WalkRatio.ID_WALKRATIO)));
                walkRatio_.setValue(cursor.getFloat(cursor.getColumnIndex(WalkRatio.VALUE)));
                walkRatio_.setIdRooms(cursor.getInt(cursor.getColumnIndex(WalkRatio.ID_ROOMS)));
                walkRatio.add(walkRatio_);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return walkRatio;
    }

    //debug only
    public static void printAllTableToLog(ArrayList<WalkRatio> list)
    {
        for ( WalkRatio element : list )
        {
            Log.d("Tabela WalkRatio: ",
                    "\t|ID| "+String.valueOf(element.getIdWalkRatio()) +
                            "\t|Value| "+ String.valueOf(element.getValue()) +
                            "\t|IdRooms| "+ String.valueOf(element.getIdRooms())
            ) ;
        }
    }
}
