package com.example.kamil.br.database.model;

/**
 * Created by Kamil on 2016-09-21.
 */
public class Rooms
{
    public static final String TABLE = "Rooms";

    public static final String ID_ROOMS = "idrooms";
    public static final String NAME = "name";

    //nazwy pól
    private int idRooms;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdRooms() {
        return idRooms;
    }

    public void setIdRooms(int idRooms) {
        this.idRooms = idRooms;
    }
}
