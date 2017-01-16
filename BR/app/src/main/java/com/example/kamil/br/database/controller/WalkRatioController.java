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
 * obsługuje tabelę walkratio
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
        db.close();
    }

    /**
     * usuwa rekord o danym id
     * @param id id rekordu
     * @param context kontekst aplikacji
     */
    public static void deleteWhereId(int id, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String delete =
                " DELETE FROM " + WalkRatio.TABLE +
                        " WHERE WalkRatio." + WalkRatio.ID_WALKRATIO+"="+Integer.toString(id);
        db.execSQL(delete);
        db.close();
    }

    /**
     * zwraca rekord o danym danym id pokoju
     * @param context kontekst aplikacji
     * @param idRooms id pokoju
     * @return
     */
    public static WalkRatio selectWalkRatioWhereRoomId(Context context, int idRooms)
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

    /**
     * zwraca całą tabele
     * @param context kontekst aplikacji
     * @return tabela w formie listy
     */
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

    /**
     * drukuje listę do logu
     * @param list lista
     */
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
