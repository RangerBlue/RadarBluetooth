package com.example.kamil.br.database.model;

/**
 * Created by Kamil on 2016-09-21.
 */
public class Measurements {
    public static final String TABLE = "Measurements";

    public static final String ID_MEASUREMENTS = "idmeasurements";
    public static final String NAME = "name";
    public static final String ID_ROOMS = "idrooms";

    //nazwy pól
    private int idMeasurements;
    private String name;
    private int idRooms;

    public int getIdRooms() {
        return idRooms;
    }

    public void setIdRooms(int idRooms) {
        this.idRooms = idRooms;
    }

    public int getIdMeasurements() {
        return idMeasurements;
    }

    public void setIdMeasurements(int idMeasurements) {
        this.idMeasurements = idMeasurements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
