package com.example.kamil.br;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kamil on 18.04.16.
 */
public class PomocnikBD extends SQLiteOpenHelper {


    public final static int WERSJA_BAZY = 1;
    public final static String ID = "_id";
    public final static String NAZWA_BAZY = "bazaDanych";
    public final static String NAZWA_TABELI = "tabela1";
    public final static String KOLUMNA1 = "kolumna1";
    public final static String KOLUMNA2 = "kolumna2";
    public final static String KOLUMNA3 = "kolumna3";
    public final static String TW_BAZY = "CREATE TABLE " + NAZWA_TABELI + " ( "+ID+" integer primary key autoincrement, " + KOLUMNA1 + " text not null, " + KOLUMNA2 + " text "+KOLUMNA3+ " text); ";
    public final static String KAS_BAZY = "DROP TABLE IF EXISTS "+ NAZWA_TABELI;

    public PomocnikBD( Context context ){
        super(context,NAZWA_BAZY,null,WERSJA_BAZY,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TW_BAZY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(KAS_BAZY);
        onCreate(db);
    }




}