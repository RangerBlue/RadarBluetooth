package com.example.kamil.br.database.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.kamil.br.activities.MainActivity;
import com.example.kamil.br.database.DBHandler;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.database.model.Rooms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil on 2016-09-21.
 */
public class RoomsController {

    private final String TAG = RoomsController.class.getSimpleName();

    public static String createTable() {
        return "CREATE TABLE " + Rooms.TABLE + "(" +
                Rooms.ID_ROOMS + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Rooms.TYPE + " REAL," +
                Rooms.NAME + " TEXT )";
    }

    public void insert(Rooms room, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Rooms.NAME, room.getName());
        values.put(Rooms.TYPE, room.getType());
        db.insert(Rooms.TABLE, null, values);
        db.close(); // Closing database connection
        Log.d("Insert record: ", room.TABLE+" "+ room.getName());
        Log.d("Insert record: ", room.TYPE+" "+ room.getType());
    }

    /**
     * Usuwa pokój
     * @param @param idRoom id pokoju do usunięcia
     * @param @context kontekst aplikacji
     */
    public void deleteWhereIdRooms(int idRoom, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String delete =
                " DELETE FROM " + Rooms.TABLE +
                " WHERE Rooms." + Rooms.ID_ROOMS+"="+Integer.toString(idRoom);
        db.execSQL(delete);
        Log.d(TAG, "deleted record from rooms");
        db.close(); // Closing database connection
    }

    /**
     * Usuwa pokój wraz ze wszystkimi kluczami obcymi w innych tabelach
     * @param @param idRoom id pokoju do usunięcia
     * @param @context kontekst aplikacji
     */
    public void deleteRoomAndAllDependences(int idRoom, Context context)
    {
        deleteWhereIdRooms(idRoom, context);
        PathDataController.deleteWhereIdRooms(idRoom, context);
        BluetoothResultsController.deleteWhereIdRooms(idRoom, context);
        MeasurementsController.deleteWhereIdRooms(idRoom, context);
    }

    /**
     * Zwraca typ pokoju
     * @param context kontekst aplikacji
     * @param idRoom identyfikator pokoju
     * @return
     */
    public static Float selectTypeWhereId(Context context, int idRoom)
    {
        List<Float> rooms = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "Rooms." + Rooms.TYPE +
                        " FROM " + Rooms.TABLE +
                        " WHERE Rooms." + Rooms.ID_ROOMS+"="+Integer.toString(idRoom);

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Float type = cursor.getFloat(cursor.getColumnIndex(Rooms.TYPE));
                Log.d("w funkcji", String.valueOf(type));
                rooms.add(type);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return rooms.get(0);
    }

    public List<Rooms> selectAll(Context context)
    {
        List<Rooms> rooms = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                "Rooms." + Rooms.ID_ROOMS +
                ", Rooms." + Rooms.NAME +
                ", Rooms." + Rooms.TYPE +
                " FROM " + Rooms.TABLE;

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Rooms room= new Rooms();
                room.setIdRooms(cursor.getInt(cursor.getColumnIndex(Rooms.ID_ROOMS)));
                room.setName(cursor.getString(cursor.getColumnIndex(Rooms.NAME)));
                room.setType(cursor.getFloat(cursor.getColumnIndex(Rooms.TYPE)));
                rooms.add(room);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return rooms;
    }

    //debug only
    public static void printAllTableToLog(ArrayList<Rooms> list)
    {
        //wypisanie rekordów z PathData nienaruszonych
        for ( Rooms element : list )
        {
            Log.d("Tabela Rooms: ",
                    "\t|ID| "+String.valueOf(element.getIdRooms()) +
                            "\t|Name| "+ String.valueOf(element.getName()) +
                            "\t|type| "+ String.valueOf(element.getType())
            ) ;
        }
    }
}
