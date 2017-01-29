package com.example.kamil.br.database.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.kamil.br.database.DBHandler;
import com.example.kamil.br.database.model.Rooms;

import java.util.ArrayList;
import java.util.List;

/**
 * obsługa tabeli rooms
 * Created by Kamil on 2016-09-21.
 */
public class RoomsController {

    private final String TAG = RoomsController.class.getSimpleName();

    public static String createTable() {
        return "CREATE TABLE " + Rooms.TABLE + "(" +
                Rooms.ID_ROOMS + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Rooms.WALK_RATIO + " REAL," +
                Rooms.NAME + " TEXT, " +
                Rooms.A_VALUE + " INTEGER, "+
                Rooms.N_VALUE + " REAL )";
    }

    public void insert(Rooms room, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Rooms.NAME, room.getName());
        values.put(Rooms.WALK_RATIO, room.getWalkRatio());
        values.put(Rooms.A_VALUE, room.getA());
        values.put(Rooms.N_VALUE, room.getN());
        db.insert(Rooms.TABLE, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Usuwa pokój
     * @param @param id pokoju do usunięcia
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
        db.close();
    }

    /**
     * Usuwa pokój wraz ze wszystkimi kluczami obcymi w innych tabelach
     * @param @param idRoom id pokoju do usunięcia
     * @param @context kontekst aplikacji
     */
    public void deleteRoomAndAllDependencies(int idRoom, Context context)
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
    public static Float selectWalkRatioWhereId(Context context, int idRoom)
    {
        List<Float> rooms = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "Rooms." + Rooms.WALK_RATIO +
                        " FROM " + Rooms.TABLE +
                        " WHERE Rooms." + Rooms.ID_ROOMS+"="+Integer.toString(idRoom);

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Float type = cursor.getFloat(cursor.getColumnIndex(Rooms.WALK_RATIO));
                rooms.add(type);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return rooms.get(0);
    }

    /**
     * Zwraca pokój o danym id
     * @param context kontekst aplikacji
     * @param idRoom identyfikator pokoju
     * @return
     */
    public static Rooms selectWhereId(Context context, int idRoom)
    {
        List<Rooms> rooms = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "Rooms." + Rooms.ID_ROOMS +
                        ", Rooms." + Rooms.NAME +
                        ", Rooms." + Rooms.WALK_RATIO +
                        " FROM " + Rooms.TABLE +
                        " WHERE Rooms." + Rooms.ID_ROOMS+"="+Integer.toString(idRoom);

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Rooms room= new Rooms();
                room.setIdRooms(cursor.getInt(cursor.getColumnIndex(Rooms.ID_ROOMS)));
                room.setName(cursor.getString(cursor.getColumnIndex(Rooms.NAME)));
                room.setWalkRatio(cursor.getFloat(cursor.getColumnIndex(Rooms.WALK_RATIO)));
                rooms.add(room);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return rooms.get(0);
    }

    /**
     * Zwraca nazwę pokoju
     * @param context kontekst aplikacji
     * @param idRoom identyfikator pokoju
     * @return
     */
    public static String selectNameWhereId(Context context, int idRoom)
    {
        List<String> names = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "Rooms." + Rooms.NAME+
                        " FROM " + Rooms.TABLE +
                        " WHERE Rooms." + Rooms.ID_ROOMS+"="+Integer.toString(idRoom);

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(Rooms.NAME));
                names.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return names.get(0);
    }

    /**
     * Zwraca wartość A
     * @param context kontekst aplikacji
     * @param idRoom identyfikator pokoju
     * @return
     */
    public static Integer selectAWhereId(Context context, int idRoom)
    {
        List<Integer> values = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "Rooms." + Rooms.A_VALUE+
                        " FROM " + Rooms.TABLE +
                        " WHERE Rooms." + Rooms.ID_ROOMS+"="+Integer.toString(idRoom);

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Integer a  = cursor.getInt(cursor.getColumnIndex(Rooms.A_VALUE));
                values.add(a);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return values.get(0);
    }

    /**
     * Zwraca wartość n
     * @param context kontekst aplikacji
     * @param idRoom identyfikator pokoju
     * @return
     */
    public static Float selectNWhereId(Context context, int idRoom)
    {
        List<Float> values = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "Rooms." + Rooms.N_VALUE+
                        " FROM " + Rooms.TABLE +
                        " WHERE Rooms." + Rooms.ID_ROOMS+"="+Integer.toString(idRoom);

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Float a  = cursor.getFloat(cursor.getColumnIndex(Rooms.N_VALUE));
                values.add(a);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return values.get(0);
    }

    /**
     * zwraca całą tabelę
     * @param context kontekst aplikacji
     * @return
     */
    public static List<Rooms> selectAll(Context context)
    {
        List<Rooms> rooms = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                "Rooms." + Rooms.ID_ROOMS +
                ", Rooms." + Rooms.NAME +
                ", Rooms." + Rooms.WALK_RATIO +
                ", Rooms." + Rooms.A_VALUE+
                ", Rooms." + Rooms.N_VALUE+
                " FROM " + Rooms.TABLE;

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Rooms room= new Rooms();
                room.setIdRooms(cursor.getInt(cursor.getColumnIndex(Rooms.ID_ROOMS)));
                room.setName(cursor.getString(cursor.getColumnIndex(Rooms.NAME)));
                room.setWalkRatio(cursor.getFloat(cursor.getColumnIndex(Rooms.WALK_RATIO)));
                room.setA(cursor.getInt(cursor.getColumnIndex(Rooms.A_VALUE)));
                room.setN(cursor.getFloat(cursor.getColumnIndex(Rooms.N_VALUE)));
                rooms.add(room);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return rooms;
    }

    /**
     * Sprawdza czy podana nazwa pokoju już istnieje
     * @param context kontekst aplikacji
     * @param name nazwa pokoju
     * @return
     */
    public static boolean ifNameExists(Context context, String name)
    {
        List<String> names = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "Rooms." + Rooms.NAME +
                        " FROM " + Rooms.TABLE +
                        " WHERE Rooms." + Rooms.NAME+"=?";

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, new String[] {name});
        if (cursor.moveToFirst()) {
            do {
                String name_ = cursor.getString(cursor.getColumnIndex(Rooms.NAME));
                names.add(name_);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        if(names.isEmpty())
            return false;
        else
            return true;
    }

    public static Rooms getLastRecord(Context context)
    {
        List<Rooms> rooms = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT *" +
                        " FROM " + Rooms.TABLE+
                        " WHERE Rooms."+ Rooms.ID_ROOMS+"="+
                        "(SELECT MAX(Rooms."+Rooms.ID_ROOMS+") FROM "+Rooms.TABLE+")";


        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Rooms room= new Rooms();
                room.setIdRooms(cursor.getInt(cursor.getColumnIndex(Rooms.ID_ROOMS)));
                room.setName(cursor.getString(cursor.getColumnIndex(Rooms.NAME)));
                room.setWalkRatio(cursor.getFloat(cursor.getColumnIndex(Rooms.WALK_RATIO)));
                rooms.add(room);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        if(rooms.isEmpty())
        {
            Rooms returnRooms = new Rooms();
            returnRooms.setIdRooms(-1);
            rooms.add(returnRooms);
        }
        return rooms.get(0);
    }

    public void update(Rooms room, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Rooms.NAME, room.getName());
        values.put(Rooms.WALK_RATIO, room.getWalkRatio());

        String where = " Rooms." + Rooms.ID_ROOMS+"="+Integer.toString(room.getIdRooms());

        db.update(Rooms.TABLE, values, where,  null);
        Log.d(TAG, " Updated row number: "+room.getIdRooms());
    }



    /**
     * wypisuje do logu listę
     * @param list  lista
     */
    public static void printAllTableToLog(ArrayList<Rooms> list)
    {
        for ( Rooms element : list )
        {
            Log.d("Tabela Rooms: ",
                    "\t|ID| "+String.valueOf(element.getIdRooms()) +
                            "\t|Name| "+ String.valueOf(element.getName()) +
                            "\t|Walkratio| "+ String.valueOf(element.getWalkRatio()) +
                            "\t|A| "+ String.valueOf(element.getA()) +
                            "\t|N| "+ String.valueOf(element.getN())
            ) ;
        }
    }
}
