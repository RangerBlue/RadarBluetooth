package com.example.kamil.br.database.model;

/**
 * Created by Kamil on 2016-12-14.
 *
 */

public class WalkRatio
{
    public static final String TABLE = "WalkRatio";

    public static final String ID_WALKRATIO = "idwalkratio";
    public static final String VALUE = "value";
    public static final String ID_ROOMS = "idrooms";

    //nazwy pól
    /**
     * identyfikator
     */
    private int idWalkRatio;

    /**
     * zależność między czas przejścia określonej krawędzi o pewnej długości pixeli do ilości tych pixeli
     */
    private float value;

    /**
     * klucz obcy do room
     */
    private int idRooms;

    public int getIdWalkRatio() {
        return idWalkRatio;
    }

    public void setIdWalkRatio(int idWalkRatio) {
        this.idWalkRatio = idWalkRatio;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getIdRooms() {
        return idRooms;
    }

    public void setIdRooms(int idRooms) {
        this.idRooms = idRooms;
    }
}
